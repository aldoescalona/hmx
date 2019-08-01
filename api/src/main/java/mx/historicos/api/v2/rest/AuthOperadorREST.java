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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import mx.historicos.api.security.JWTKey;
import mx.historicos.api.bean.Operador;
import mx.historicos.api.facade.OperadorFacade;
import mx.historicos.api.model.Credencial;
import mx.historicos.api.model.Token;
import mx.historicos.api.security.Secured;
import mx.historicos.api.security.UserContext;
import mx.historicos.api.util.APIUtil;
import org.apache.log4j.Logger;

/**
 *
 * @author 43700118
 */
@Path("auth/operador")
public class AuthOperadorREST {

    @EJB
    private OperadorFacade operadorFacade;

    @EJB
    private JWTKey jwtkey;

    public static int HORAS = 3;
    private static Logger logger = Logger.getLogger(AuthOperadorREST.class);

    public AuthOperadorREST() {
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUserRoot(Credencial authu) {
        try {

            String username = authu.getUsername();
            String password = authu.getPassword();
            String alias = authu.getAlias();

            Operador op = authenticate(username, password, alias);
            String token = jwtkey.token(op.getId(), username, "operador.638", HORAS);

            Token r = new Token(token);
            System.out.println(" CTE: " + op + ", " + jwtkey);

            Response.ResponseBuilder rb = Response.ok(r);
            Response resp = rb.header("Pragma", "no-cache")
                    .header("Expires", "-1")
                    .header("X-Cached-Result", "true")
                    .header("X-Cache-Remaining", "99")
                    .header("X-Cache-Expires", APIUtil.getExpiresHeader())
                    .header("Date", APIUtil.getDateHeader())
                    .build();

            return resp;
        } catch (Exception e) {
            logger.error(e, e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @POST
    @Path("/renovar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response renovarToken(@Context SecurityContext securityContext) {
        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            if (!user.verificaPermiso("operador")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Operador op = operadorFacade.get(user.getId());
            String token = jwtkey.token(op.getId(), user.getUsername(), "operador.638", HORAS);

            Token r = new Token(token);

            Response.ResponseBuilder rb = Response.ok(r);
            Response resp = rb.header("Pragma", "no-cache")
                    .header("Expires", "-1")
                    .header("X-Cached-Result", "true")
                    .header("X-Cache-Remaining", "99")
                    .header("X-Cache-Expires", APIUtil.getExpiresHeader())
                    .header("Date", APIUtil.getDateHeader())
                    .build();

            return resp;
        } catch (Exception e) {
            logger.error(e, e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private Operador authenticate(String username, String password, String alias) throws Exception {

        Operador ent = operadorFacade.getOperador(username, password, alias);
        if (ent == null) {
            throw new AuthenticationException();
        }
        return ent;
    }

}
