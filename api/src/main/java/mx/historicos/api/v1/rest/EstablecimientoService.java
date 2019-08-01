/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v1.rest;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
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
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Oferta;
import mx.historicos.api.bean.Segmento;
import mx.historicos.api.bean.UsuarioPuntos;
import mx.historicos.api.facade.LugarFacade;
import mx.historicos.api.facade.OfertaFacade;
import mx.historicos.api.facade.SegmentoFacade;
import mx.historicos.api.facade.UsuarioPuntosFacade;
import mx.historicos.api.security.Secured;
import mx.historicos.api.security.UserContext;
import mx.historicos.api.util.APIUtil;
import mx.historicos.api.v1.model.EstablecimientoOferta;
import mx.historicos.api.v1.model.EstablecimientoRoot;
import mx.historicos.api.v1.model.SegmentoLugar;
import mx.historicos.api.v1.model.SegmentoRoot;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * REST Web Service
 *
 * @author 43700118
 */
@Path("establecimiento")
public class EstablecimientoService {

    
    @EJB
    private LugarFacade lugarFacade;
    
    @EJB
    private OfertaFacade ofertaFacade;
    
    @EJB
    private UsuarioPuntosFacade usuarioPuntosFacade;
    
    @EJB
    private SegmentoFacade segmentoFacade;
    
    private static Logger logger = Logger.getLogger(EstablecimientoService.class);
    @Context
    private UriInfo context;

    @GET
    @Path("/{giroId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response establecimientos(@PathParam("giroId") final Long giroId, @Context SecurityContext securityContext) throws IOException {
        SegmentoRoot root = new SegmentoRoot();
        try {
            
            UserContext user = (UserContext)securityContext.getUserPrincipal();
            Long uid = user.getId();
            
            Segmento g = segmentoFacade.get(giroId);
            List<Lugar> list = lugarFacade.getLugares(uid, giroId);

            root.setSegmento(g.getNombre());
            List<SegmentoLugar> children = new ArrayList();
            for (Lugar item : list) {
                SegmentoLugar eve = new SegmentoLugar();
                eve.setCalificacion(String.format("%.1f", item.getCalificacion()));
                eve.setCalificacionGraf(item.getCalificacion().intValue());
                eve.setDireccion(item.getDireccion());
                eve.setId(item.getId());
                eve.setNombre(item.getNombre());
                eve.setTelefono(item.getTelefono());
                if(item.getImagenLogoId() != null){
                    eve.setImagenUrl(item.getImagenLogoId().getImgC());
                }
                
                children.add(eve);
            }
            root.setLugares(children);
            root.setEstatus("ok");
        } catch (Exception ex) {
            logger.error(ex, ex);
            ex.printStackTrace();
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
    @Path("/ver/{eid}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response establecimiento(@PathParam("eid") final Long eId, @Context SecurityContext securityContext) throws IOException {
        EstablecimientoRoot root = new EstablecimientoRoot();
        try {

            UserContext user = (UserContext)securityContext.getUserPrincipal();
            Long uid = user.getId();
            
            
            Lugar parent = lugarFacade.get(eId, "imagenLogoId");
            UsuarioPuntos usuarioPuntos = usuarioPuntosFacade.getPuntos(parent, uid);

            Junction cron = Restrictions.conjunction();
            cron.add(Restrictions.eq("estado", Boolean.TRUE));
            cron.add(Restrictions.eq("lugarId", parent));

            List<Oferta> list = ofertaFacade.list(cron, Order.asc("nombre"), "imagenId");
            
            root.setToken(UUID.randomUUID().toString());
            List<EstablecimientoOferta> children = new ArrayList();
            for (Oferta item : list) {
                EstablecimientoOferta eve = new EstablecimientoOferta(item.getId(), item.getNombre(), item.getDescripcion(), item.getCondiciones(), item.getPrecio().floatValue(), (item.getImagenId() ==null?null:item.getImagenId().getImgC()), (item.getCalificacion() == null?null:item.getCalificacion().floatValue()));
                children.add(eve);
            }

            root.setCalificacion(String.format("%.1f", parent.getCalificacion()));
            root.setCalificacionGraf(parent.getCalificacion().intValue());
            root.setDireccion(parent.getDireccion());
            root.setId(parent.getId());
            root.setMapaUrl(parent.getUbicacionUrl());
            root.setNombre(parent.getNombre());
            root.setDescripcion(parent.getDescripcion());
            
            
            if(parent.getImagenLogoId() != null){
                root.setImagenUrl(parent.getImagenLogoId().getImgB());
            }
            
            if (usuarioPuntos == null) {
                root.setPuntos(0);
            } else {
                root.setPuntos(usuarioPuntos.getPuntos() - usuarioPuntos.getRetenidos());
            }
            root.setTelefono(parent.getTelefono());
            root.setOfertas(children);
            root.setEstatus("ok");
        } catch (Exception ex) {
            logger.error(ex, ex);
            ex.printStackTrace();
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
