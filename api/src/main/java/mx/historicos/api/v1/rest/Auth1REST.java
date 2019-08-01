/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v1.rest;

import javax.ejb.EJB;
import javax.naming.AuthenticationException;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import mx.historicos.api.bean.Usuario;
import mx.historicos.api.enu.UsuarioEnumerated;
import mx.historicos.api.facade.UsuarioFacade;
import mx.historicos.api.model.Credencial;
import mx.historicos.api.model.Token;
import mx.historicos.api.security.JWTKey;
import mx.historicos.api.util.APIUtil;
import mx.historicos.api.v1.model.LoginFacebookRespuesta;
import mx.historicos.api.v1.model.LoginFacebookRoot;
import mx.historicos.api.v1.model.NuevoRegistroRespuesta;
import mx.historicos.api.v1.model.NuevoRegistroRoot;
import org.apache.log4j.Logger;

/**
 *
 * @author 43700118
 */
@Path("/auth")
public class Auth1REST {

    @EJB
    private UsuarioFacade usuarioFacade;

    @EJB
    private JWTKey jwtkey;
    
    private static Logger logger = Logger.getLogger(Auth1REST.class);

    @POST
    @Path("/loginRoot")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUserRoot(Credencial authu) {
        try {
            String username = authu.getUsername();
            String password = authu.getPassword();

            Usuario usr = authenticate(username, password);
            String token = jwtkey.token(usr.getId(), username, null, null);

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
    
    @POST
    @Path("/registro")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response nuevoRegistro(@Context SecurityContext secContext, NuevoRegistroRoot registro) {

        NuevoRegistroRespuesta root;
        try {

            root = validaRegistro(registro);

            if (root == null) {
                root = new NuevoRegistroRespuesta();

                String username = registro.getCorreo();
                String password = registro.getContrasena();

                Usuario usuario = new Usuario();
                usuario.setEmail(username);
                usuario.setContrasena(password);
                usuario.setNombre(registro.getNombre());
                // usuario.setSexo(UsuarioEnumerated.GENERO.values()[registro.getGenero()]);
                usuario.setEstado(UsuarioEnumerated.ESTADO.ACTIVO);
                usuario.setPuntos(0);
                usuario.setRetenidos(0);
                usuarioFacade.crear(usuario);

                String token = jwtkey.token(usuario.getId(), username, null, null);
                
                root.setEstatus("ok");
                root.setToken(token);
            }

        } catch (Exception e) {
            logger.error(e, e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Response.ResponseBuilder rb = Response.ok(root);
        Response resp = rb.header("Pragma", "no-cache")
                .header("Expires", "-1")
                .header("X-Cached-Result", "true")
                .header("X-Cache-Remaining", "99")
                .header("X-Cache-Expires", APIUtil.getExpiresHeader())
                .header("Date", APIUtil.getDateHeader())
                .build();
        return resp;
    }
    
    @POST
    @Path("/loginFacebook")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response nuevoRegistroFacebook(@Context SecurityContext secContext, LoginFacebookRoot registro) {

        LoginFacebookRespuesta root = new LoginFacebookRespuesta();
        try {

            Usuario fbusuario = usuarioFacade.getUsuarioFacebookId(registro.getUid());

            if (fbusuario == null) {
                fbusuario = new Usuario();
                fbusuario.setFacebookid(registro.getUid());
                fbusuario.setNombre(registro.getNombre());
                fbusuario.setEmail(registro.getCorreo());
                fbusuario.setImagenURL(registro.getImagen());
                fbusuario.setEstado(UsuarioEnumerated.ESTADO.ACTIVO);
                fbusuario.setPuntos(0);
                fbusuario.setRetenidos(0);
                usuarioFacade.crear(fbusuario);
            }

            String token = jwtkey.token(fbusuario.getId(), registro.getCorreo(), null, null);

            root.setEstatus("ok");
            root.setToken(token);

        } catch (Exception e) {
            logger.error(e, e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Response.ResponseBuilder rb = Response.ok(root);
        Response resp = rb.header("Pragma", "no-cache")
                .header("Expires", "-1")
                .header("X-Cached-Result", "true")
                .header("X-Cache-Remaining", "99")
                .header("X-Cache-Expires", APIUtil.getExpiresHeader())
                .header("Date", APIUtil.getDateHeader())
                .build();
        return resp;
    }
    
    private NuevoRegistroRespuesta validaRegistro(NuevoRegistroRoot registro) throws NamingException {

        NuevoRegistroRespuesta root = null;

        String username = registro.getCorreo();
        String password = registro.getContrasena();

        // String email = StringUtils.trimToNull(username);
        String email = username;

        if (email == null) {
            root = new NuevoRegistroRespuesta();
            root.setEstatus("err");
            root.setMensaje("Ingrese un correo electónico");
            return root;
        }

        if (registro.getNombre() == null) {
            root = new NuevoRegistroRespuesta();
            root.setEstatus("err");
            root.setMensaje("Ingrese un nombre");
            return root;
        }

        /*if (registro.getGenero() == null) {
            root = new NuevoRegistroRespuesta();
            root.setEstatus("err");
            root.setMensaje("Seleccone su sexo");
            return root;
        }*/

        Usuario us = usuarioFacade.getUsuarioCorreo(email);
        if (us != null) {
            root = new NuevoRegistroRespuesta();
            root.setEstatus("err");
            root.setMensaje("El usuario ya esta registrado");
            return root;
        }

        if (password == null || password.length() < 8) {
            root = new NuevoRegistroRespuesta();
            root.setEstatus("err");
            root.setMensaje("Ingrese una contraseña de al menos 8 caracteres");
            return root;
        }

        return root;
    }

}
