/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
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
import mx.historicos.api.bean.Compra;
import mx.historicos.api.bean.Evento;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Oferta;
import mx.historicos.api.bean.PagoCompra;
import mx.historicos.api.bean.Segmento;
import mx.historicos.api.bean.Usuario;
import mx.historicos.api.bean.UsuarioPuntos;
import mx.historicos.api.bean.Visita;
import mx.historicos.api.enu.CompraEnumerated;
import mx.historicos.api.enu.VisitaEnumerated;
import mx.historicos.api.facade.CompraFacade;
import mx.historicos.api.facade.EventoFacade;
import mx.historicos.api.facade.LugarFacade;
import mx.historicos.api.facade.OfertaFacade;
import mx.historicos.api.facade.SegmentoFacade;
import mx.historicos.api.facade.UsuarioFacade;
import mx.historicos.api.facade.UsuarioPuntosFacade;
import mx.historicos.api.facade.VisitaFacade;
import mx.historicos.api.security.Secured;
import mx.historicos.api.security.UserContext;
import mx.historicos.api.util.APIUtil;
import mx.historicos.api.v2.model.CUDResponse;
import mx.historicos.api.v2.model.usr.ComprarOfertaResponse;
import mx.historicos.api.v2.model.usr.DashboardUsuario;
import mx.historicos.api.v2.model.usr.LugarItem;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author 43700118
 */
@Path("app")
public class AppREST {

    @EJB
    private UsuarioFacade usuarioFacade;

    @EJB
    private UsuarioPuntosFacade usuarioPuntosFacade;

    @EJB
    private VisitaFacade visitaFacade;

    @EJB
    private CompraFacade compraFacade;
    
     @EJB
    private EventoFacade eventoFacade;

    @EJB
    private OfertaFacade ofertaFacade;

    @EJB
    private SegmentoFacade segmentoFacade;

    @EJB
    private LugarFacade lugarFacade;

    private static Logger logger = Logger.getLogger(AdminREST.class);

    public AppREST() {
    }

    @GET
    @Path("dashboard")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response find(@Context SecurityContext securityContext) {

        DashboardUsuario root = new DashboardUsuario();
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("usuario")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Usuario u = usuarioFacade.get(user.getId());

            u.setContrasena(null);

            List<Segmento> list = segmentoFacade.list(Restrictions.eq("estado", Boolean.TRUE), Order.asc("orden"));
            for (Segmento segmento : list) {
                segmento.setLugarList(null);
            }

            root.setUsuario(u);
            root.setSegmentos(list);

            System.out.println(" FULL: " + APIUtil.toStringUsingJackson(root));

            Response.ResponseBuilder rb = Response.ok(root);
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
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("lugares/{segmentoId}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response getLugares(@PathParam("segmentoId") Long segmentoId, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("usuario")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            List<LugarItem> list = lugarFacade.getLugaresItems(segmentoId, user.getId());
            System.out.println(" FULL: " + APIUtil.toStringUsingJackson(list));

            Response.ResponseBuilder rb = Response.ok(list);
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
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("lugar/{lugarId}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response getLugar(@PathParam("lugarId") Long lugarId, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("usuario")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Lugar lugar = lugarFacade.get(lugarId, "imagenPortadaId", "imagenLogoId");
            lugar.setCiudadId(null);
            lugar.setCodigoPostalId(null);
            lugar.setEventoList(null);
            // lugar.setOfertaList(null);
            lugar.setPropietarioId(null);
            lugar.setSegmentoId(null);

            List<Oferta> list = ofertaFacade.getOfertasApp(lugar);
            for (Oferta oferta : list) {
                oferta.setLugarId(null);
            }
            lugar.setOfertaList(list);

            UsuarioPuntos upuntos = usuarioPuntosFacade.getPuntos(lugar, user.getId());
            if (upuntos == null) {
                upuntos = new UsuarioPuntos();
                upuntos.setPuntos(0);
                upuntos.setRetenidos(0);
            }
            upuntos.setLugarId(lugar);
            upuntos.setUsuarioId(null);

            System.out.println(" FULL: " + APIUtil.toStringUsingJackson(upuntos));

            Response.ResponseBuilder rb = Response.ok(APIUtil.toStringUsingJackson(upuntos), MediaType.APPLICATION_JSON_TYPE);
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
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("oferta/{ofertaId}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response getOferta(@PathParam("ofertaId") Long ofertaId, @Context SecurityContext securityContext) {
        UserContext user = (UserContext) securityContext.getUserPrincipal();
        return compra(ofertaId, user, false);
    }

    @POST
    @Path("/comprar/{ofertaId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response comprar(@PathParam("ofertaId") final Long ofertaId, @Context SecurityContext securityContext) throws IOException {
        UserContext user = (UserContext) securityContext.getUserPrincipal();
        return compra(ofertaId, user, true);
    }

    private Response compra(Long ofertaId, UserContext user, boolean ejecuta) {
        try {

            if (!user.verificaPermiso("usuario")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Usuario usuario = usuarioFacade.get(user.getId());

            Oferta oferta = ofertaFacade.get(ofertaId, "lugarId", "imagenId", "lugarId.imagenLogoId");
            Lugar lugar = oferta.getLugarId();
            UsuarioPuntos usuarioPuntos = usuarioPuntosFacade.getPuntos(lugar, user.getId());

            Integer puntosUsuario = 0;
            if (usuarioPuntos != null) {
                puntosUsuario = usuarioPuntos.getPuntos() - usuarioPuntos.getRetenidos();
            }

            Integer precioMeta = oferta.getPrecio();
            ComprarOfertaResponse root = new ComprarOfertaResponse();

            boolean puedeComprar = precioMeta <= puntosUsuario;
            if (puedeComprar) {
                Compra compra = new Compra();
                compra.setLugarId(lugar);
                compra.setUsuarioId(usuario);
                compra.setOfertaId(oferta);
                compra.setEstado(CompraEnumerated.ESTADO.PENDIENTE);
                compra.setFecha(new Date());
                compra.setPuntos(oferta.getPrecio());

                PagoCompra pago = new PagoCompra();
                pago.setOrigenId(lugar);
                pago.setPuntos(oferta.getPrecio());

                List<PagoCompra> pagos = new ArrayList();
                pagos.add(pago);

                if (ejecuta) {
                    lugarFacade.comprar(compra, pagos);
                }

                /*for (PagoCompra p : pagos) {
                    Lugar l = p.getOrigenId();
                    l.setCiudadId(null);
                    l.setCodigoPostalId(null);
                    l.setOfertaList(null);
                    l.setEventoList(null);
                    l.setImagenPortadaId(null);
                    l.setOfertaList(null);
                    l.setPropietarioId(null);
                    l.setSegmentoId(null);
                }*/
                compra.setLugarId(null);
                compra.setUsuarioId(null);
                compra.getOfertaId().setLugarId(null);

                root.setOk(Boolean.TRUE);
                root.setMensaje("OK");
                root.setCompra(compra);
                // root.setPagos(pagos);

            } else {
                root.setOk(Boolean.FALSE);
                root.setMensaje("No tiene suficientes puntos para realizar esta compra");
            }

            System.out.println(" FULL: " + APIUtil.toStringUsingJackson(root));
            Response.ResponseBuilder rb = Response.ok(root);
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
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("visita/{lugarId}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response visita(@PathParam("lugarId") Long lugarId, Visita visita, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("usuario")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            visita.setEstado(VisitaEnumerated.ESTADO.PENDIENTE);
            visita.setFecha(new Date());
            visita.setLugarId(new Lugar(lugarId));
            visita.setUsuarioId(new Usuario(user.getId()));

            visitaFacade.create(visita);

            CUDResponse root = new CUDResponse();
            root.setOk(Boolean.TRUE);
            root.setMensaje("OK");

            System.out.println(" FULL: " + APIUtil.toStringUsingJackson(root));

            Response.ResponseBuilder rb = Response.ok(root);
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
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("visitas")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response visitas(@Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("usuario")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            List<Visita> list = visitaFacade.getVisitasUsuario(user.getId());
            for (Visita visita : list) {
                visita.setOperadorId(null);
                visita.setUsuarioId(null);
                Lugar l = visita.getLugarId();
                l.setCiudadId(null);
                l.setCodigoPostalId(null);
                l.setOfertaList(null);
                l.setEventoList(null);
                l.setImagenPortadaId(null);
                l.setOfertaList(null);
                l.setPropietarioId(null);
                l.setSegmentoId(null);
            }

            System.out.println(" FULL: " + APIUtil.toStringUsingJackson(list));

            Response.ResponseBuilder rb = Response.ok(list);
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
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("compras")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response compras(@Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("usuario")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            List<Compra> list = compraFacade.getComprasUsuario(user.getId());
            for (Compra compra : list) {
                compra.setOperadorId(null);
                compra.setUsuarioId(null);
                Lugar l = compra.getLugarId();
                l.setCiudadId(null);
                l.setCodigoPostalId(null);
                l.setOfertaList(null);
                l.setEventoList(null);
                l.setImagenPortadaId(null);
                l.setOfertaList(null);
                l.setPropietarioId(null);
                l.setSegmentoId(null);
                
                Oferta oferta = compra.getOfertaId();
                oferta.setLugarId(null);
            }

            System.out.println(" FULL: " + APIUtil.toStringUsingJackson(list));

            Response.ResponseBuilder rb = Response.ok(list);
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
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    
    @GET
    @Path("eventos")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response eventos(@Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("usuario")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            List<Evento> list = eventoFacade.getCartelera(user.getId());
            for (Evento evento : list) {
                Lugar l = evento.getLugarId();
                l.setCiudadId(null);
                l.setCodigoPostalId(null);
                l.setOfertaList(null);
                l.setEventoList(null);
                l.setImagenPortadaId(null);
                l.setOfertaList(null);
                l.setPropietarioId(null);
                l.setSegmentoId(null);
                
            }

            System.out.println(" FULL: " + APIUtil.toStringUsingJackson(list));

            Response.ResponseBuilder rb = Response.ok(list);
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
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
