/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v1.rest;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import mx.historicos.api.bean.Evento;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.facade.EventoFacade;
import mx.historicos.api.security.Secured;
import mx.historicos.api.security.UserContext;
import mx.historicos.api.util.APIUtil;
import mx.historicos.api.v1.model.CarteleraEvento;
import mx.historicos.api.v1.model.CarteleraRoot;
import mx.historicos.api.v1.model.EventoRoot;
import org.apache.log4j.Logger;

/**
 * REST Web Service
 *
 * @author 43700118
 */
@Path("cartelera")
public class CarteleraService {

    private static Logger logger = Logger.getLogger(CarteleraService.class);
    
    @EJB
    private EventoFacade eventoFacade;
    
    @Context
    private UriInfo context;

    @Context
    private HttpServletResponse response;

    @Context
    private HttpServletRequest request;

    @GET
    @Path("/lista")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response cartelera(@Context SecurityContext securityContext) throws IOException {

        CarteleraRoot root = new CarteleraRoot();

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();
            Long uid = user.getId();

            List<Evento> list = eventoFacade.getCartelera(uid);

            List<CarteleraEvento> children = new ArrayList();
            for (Evento item : list) {
                CarteleraEvento eve = new CarteleraEvento();
                eve.setId(item.getId());
                eve.setFechaHora(APIUtil.formatDateTime(item.getFechaInicio()));
                if(item.getImagenId() != null){
                    eve.setImagenUrl(item.getImagenId().getImgA());
                }

                Lugar esta = item.getLugarId();
                eve.setTitulo(item.getTitulo());
                eve.setLugarNombre(esta.getNombre());
                eve.setLugarMapaUrl(esta.getUbicacionUrl());
                eve.setLugarDireccion(esta.getDireccion());
                eve.setLugarId(esta.getId());
                eve.setLugarTelefono(esta.getTelefono());
                if(esta.getImagenLogoId() != null){
                    eve.setLugarImagenUrl(esta.getImagenLogoId().getImgC());
                }
                
                children.add(eve);

            }
            root.setEventos(children);
            root.setEstatus("ok");

        } catch (Exception ex) {
            logger.error(ex, ex);
            root.setEstatus("err-n500");
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

    @GET
    @Path("/evento/{eventoId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response evento(@PathParam("eventoId") final Long eveId, @Context SecurityContext securityContext) throws IOException {

        EventoRoot root = new EventoRoot();

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();
            Long uid = user.getId();

            Evento evento = eventoFacade.get(eveId, "imagenId", "lugarId", "lugarId.imagenLogoId");
            root.setFechaHora(APIUtil.formatDateTime(evento.getFechaInicio()));
            if(evento.getImagenId() != null){
                root.setImagenUrl(evento.getImagenId().getImgA());
            }

            Lugar esta = evento.getLugarId();
            root.setTitulo(evento.getTitulo());
            root.setLugarNombre(esta.getNombre());
            root.setLugarMapaUrl(esta.getUbicacionUrl());
            root.setLugarDireccion(esta.getDireccion());
            root.setLugarTelefono(esta.getTelefono());
            if(esta.getImagenLogoId() != null){
                root.setLugarImagenUrl(esta.getImagenLogoId().getImgB());
            }

            root.setEstatus("ok");

        } catch (Exception ex) {
            logger.error(ex, ex);
            root.setEstatus("err-n500");
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
}
