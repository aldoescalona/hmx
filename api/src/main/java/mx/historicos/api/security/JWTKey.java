/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import mx.historicos.api.facade.ParametroFacade;

/**
 *
 * @author 43700118
 */
@Singleton
public class JWTKey {

    private Key key;

    @EJB
    private ParametroFacade parametroFacade;

    @PostConstruct
    public void init() {
        // key = MacProvider.generateKey();
        // keytool -genseckey -keystore historicos-keystore.pfx -storetype jceks -keyalg AES -keysize 128 -alias historicos-k-store -storepass historicospass
        try {

            String keystorePath = parametroFacade.get("jwt.keystore.archivo").getValor();
            String keystoreTipo = parametroFacade.get("jwt.keystore.tipo").getValor();
            String keystoreAlias = parametroFacade.get("jwt.keystore.alias").getValor();
            String keystorePass = parametroFacade.get("jwt.keystore.pass").getValor();

            InputStream keystoreStream = new FileInputStream(keystorePath);
            KeyStore keystore = KeyStore.getInstance(keystoreTipo);
            keystore.load(keystoreStream, keystorePass.toCharArray());
            if (!keystore.containsAlias(keystoreAlias)) {
                throw new RuntimeException("No se encontro el alias de la llave " + keystoreAlias);
            }
            key = keystore.getKey(keystoreAlias, keystorePass.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Key getKey() {
        return key;
    }

    public String token(Object id, String username, String roles, Integer horas) {

        Date issueDate = new Date();
        JwtBuilder builder = Jwts.builder();

        if (roles != null) {
            builder.claim("roles", roles);
        }

        if (horas != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(issueDate);
            calendar.add(Calendar.HOUR, horas);
            Date expireDate = calendar.getTime();
            builder.setExpiration(expireDate);
        }

        builder.setId(id.toString())
                .setSubject(username)
                .setIssuer("http://www.historicos.mx")
                .setIssuedAt(issueDate)
                .signWith(SignatureAlgorithm.HS512, key).claim("id", id);

        return builder.compact();
    }

}
