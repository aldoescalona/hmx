/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.rest;

import javax.ejb.EJB;
import javax.naming.AuthenticationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.historicos.api.bean.Usuario;
import mx.historicos.api.facade.UsuarioFacade;
import mx.historicos.api.model.Credencial;
import mx.historicos.api.model.Token;
import mx.historicos.api.security.JWTKey;
import mx.historicos.api.util.APIUtil;
import org.apache.log4j.Logger;

/**
 *
 * @author 43700118
 */
@Path("auth")
public class AuthUsuarioREST {

    @EJB
    private UsuarioFacade usuarioFacade;

    @EJB
    private JWTKey jwtkey;

    private static Logger logger = Logger.getLogger(AuthClienteREST.class);

    public AuthUsuarioREST() {
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUserRoot(Credencial authu) {
        try {
            String username = authu.getUsername();
            String password = authu.getPassword();

            Usuario usr = authenticate(username, password);
            String token = jwtkey.token(usr.getId(), username, "usuario.563", null);

            Token tr = new Token(token);

            Response.ResponseBuilder rb = Response.ok(tr);
            Response resp = rb.header("Pragma", "no-cache")
                    .header("Expires", "-1")
                    .header("X-Cached-Result", "true")
                    .header("X-Cache-Remaining", "99")
                    .header("X-Cache-Expires", APIUtil.getExpiresHeader())
                    .header("Date", APIUtil.getDateHeader())
                    .build();

            // System.out.println("  Token: " + token);
            return resp;
        } catch (Exception e) {
            logger.error(e, e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private Usuario authenticate(String username, String password) throws Exception {

        Usuario usuario = usuarioFacade.getUsuario(username, password);
        if (usuario == null) {
            throw new AuthenticationException();
        }
        return usuario;
    }

}
