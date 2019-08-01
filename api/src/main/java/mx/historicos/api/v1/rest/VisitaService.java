/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v1.rest;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Usuario;
import mx.historicos.api.bean.Visita;
import mx.historicos.api.enu.VisitaEnumerated;
import mx.historicos.api.facade.OfertaFacade;
import mx.historicos.api.facade.VisitaFacade;
import mx.historicos.api.security.Secured;
import mx.historicos.api.security.UserContext;
import mx.historicos.api.util.APIUtil;
import mx.historicos.api.v1.model.TicketsRoot;
import mx.historicos.api.v1.model.TicketsVisita;
import mx.historicos.api.v1.model.VisitaRespuestaRoot;
import mx.historicos.api.v1.model.VisitaRoot;
import org.apache.log4j.Logger;

/**
 *
 * @author 43700118
 */
@Path("/visita")
public class VisitaService {
    
    private static Logger logger = Logger.getLogger(OfertaService.class);
    
     @EJB
    private VisitaFacade visitaFacade;

    @POST
    @Path("/registro/{eid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response registrarVisita(@Context SecurityContext securityContext, @PathParam("eid") final Long eid, VisitaRoot visitain) {

        VisitaRespuestaRoot root = new VisitaRespuestaRoot();
        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            Long uid = user.getId();

            Visita visita = new Visita();
            visita.setCalificacion(visitain.getCalificacion());
            visita.setResena(visitain.getResena());
            visita.setMonto(new BigDecimal(visitain.getMonto()));
            visita.setTicket(visitain.getTicket());
            visita.setLugarId(new Lugar(eid));
            visita.setUsuarioId(new Usuario(uid));
            visita.setFecha(new Date());
            visita.setEstado(VisitaEnumerated.ESTADO.PENDIENTE);
            visitaFacade.create(visita);

            root.setEstatus("ok");
            root.setMensaje("ok");
            root.setMensajeLugar("");

        } catch (Exception e) {
            logger.error(e, e);
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
    @Path("/lista")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response visitas(@Context SecurityContext securityContext) throws IOException {
        TicketsRoot root = new TicketsRoot();
        try {
            
            UserContext user = (UserContext)securityContext.getUserPrincipal();
            Long uid = user.getId();
            
            List<Visita> list = visitaFacade.getVisitasUsuario(uid);
            
            List<TicketsVisita> children = new ArrayList();
            for (Visita item : list) {
                TicketsVisita eve = new TicketsVisita();
                eve.setCalificacion(String.format("%d", item.getCalificacion()));
                eve.setCalificacionGraf(item.getCalificacion());
                eve.setId(item.getId());
                eve.setLugar(item.getLugarId().getNombre());
                eve.setMonto(item.getMonto().toString());
                eve.setFecha(APIUtil.dateToDateString(item.getFecha()));
                eve.setEstado(item.getEstado().ordinal());
                if(item.getLugarId().getImagenLogoId() != null){
                    eve.setImagenUrl(item.getLugarId().getImagenLogoId().getImgB());
                }
                
                children.add(eve);
            }
            root.setTickets(children);
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
