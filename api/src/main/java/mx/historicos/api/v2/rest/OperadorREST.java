/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.rest;

import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import mx.historicos.api.bean.Compra;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Oferta;
import mx.historicos.api.bean.Operador;
import mx.historicos.api.bean.Visita;
import mx.historicos.api.enu.CompraEnumerated;
import mx.historicos.api.enu.VisitaEnumerated;
import mx.historicos.api.facade.CompraFacade;
import mx.historicos.api.facade.OperadorFacade;
import mx.historicos.api.facade.VisitaFacade;
import mx.historicos.api.security.Secured;
import mx.historicos.api.security.UserContext;
import mx.historicos.api.util.APIUtil;
import mx.historicos.api.v2.model.Busqueda;
import mx.historicos.api.v2.model.ope.DashboardOperador;
import mx.historicos.api.v2.model.RespuestaSimple;
import mx.historicos.api.v2.model.RespuestaSimpleForm;
import org.apache.log4j.Logger;

/**
 *
 * @author 43700118
 */
@Path("operador")
public class OperadorREST {

    @EJB
    private OperadorFacade operadorFacade;

    @EJB
    private VisitaFacade visitaFacade;

    @EJB
    private CompraFacade compraFacade;

    private static final Logger logger = Logger.getLogger(OperadorREST.class);

    public OperadorREST() {
    }

    @GET
    @Path("dashboard")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response find(@Context SecurityContext securityContext) {

        DashboardOperador root = new DashboardOperador();
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("operador")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Operador op = operadorFacade.get(user.getId(), "lugarId", "lugarId.imagenLogoId");
            Lugar lugar = op.getLugarId();
            List<Visita> visitas = visitaFacade.getVisitasDashboard(lugar);
            List<Compra> compras = compraFacade.getComprasDashboard(lugar);

            lugar.setCiudadId(null);
            lugar.setCodigoPostalId(null);
            lugar.setEventoList(null);
            lugar.setImagenPortadaId(null);
            lugar.setOfertaList(null);
            lugar.setPropietarioId(null);
            lugar.setSegmentoId(null);

            for (Visita v : visitas) {
                v.setLugarId(null);
                v.setOperadorId(null);
                v.getUsuarioId().setContrasena(null);
            }

            for (Compra c : compras) {
                c.setLugarId(null);
                c.setOperadorId(null);
                c.getUsuarioId().setContrasena(null);
                Oferta oferta = c.getOfertaId();
                oferta.setLugarId(null);
            }

            root.setLugar(lugar);
            root.setVisitas(visitas);
            root.setCompras(compras);

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

    @PUT
    @Path("visita/acepta")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response aceptarVisita(RespuestaSimpleForm srs, @Context SecurityContext securityContext) {
        return cambiaEstadoVisita(srs, securityContext, VisitaEnumerated.ESTADO.ACEPTADO);
    }

    @PUT
    @Path("visita/rechaza")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response rechazaVisita(RespuestaSimpleForm srs, @Context SecurityContext securityContext) {
        return cambiaEstadoVisita(srs, securityContext, VisitaEnumerated.ESTADO.RECHAZADA);
    }

    public Response cambiaEstadoVisita(RespuestaSimpleForm srs, @Context SecurityContext securityContext, VisitaEnumerated.ESTADO estado) {

        RespuestaSimple simple = new RespuestaSimple();

        try {

            Long id = Long.valueOf(srs.getId());
            UserContext user = (UserContext) securityContext.getUserPrincipal();
            Operador op = operadorFacade.get(user.getId());

            Visita entity = visitaFacade.get(id);

            System.out.println(" ID: " + id + ", " + entity);
            if (entity == null) {
                simple.setOk(Boolean.FALSE);
                simple.setCodigo(-10);
                simple.setMensaje("Esta visita ya fue promovida por otro operador");
            } else if (!entity.getEstado().equals(VisitaEnumerated.ESTADO.PENDIENTE)) {
                simple.setOk(Boolean.FALSE);
                simple.setCodigo(-10);
                simple.setMensaje("Esta visita ya fue promovida por otro operador");
            } else {
                entity.setEstado(estado);
                entity.setRespuesta(srs.getMotivo());
                entity.setOperadorId(op);
                entity.setFechaRespuesta(new Date());
                visitaFacade.edit(entity);
                simple.setOk(Boolean.TRUE);
            }

            Response.ResponseBuilder rb = Response.ok(simple);
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

    @PUT
    @Path("compra/acepta")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response aceptarCompra(RespuestaSimpleForm srs, @Context SecurityContext securityContext) {
        return cambiaEstadoCompra(srs, securityContext, CompraEnumerated.ESTADO.CONFIRMADA);
    }

    @PUT
    @Path("compra/rechaza")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response rechazaCompra(RespuestaSimpleForm srs, @Context SecurityContext securityContext) {
        return cambiaEstadoCompra(srs, securityContext, CompraEnumerated.ESTADO.RECHAZADA);
    }

    public Response cambiaEstadoCompra(RespuestaSimpleForm srs, @Context SecurityContext securityContext, CompraEnumerated.ESTADO estado) {

        RespuestaSimple simple = new RespuestaSimple();

        try {

            Long id = Long.valueOf(srs.getId());
            UserContext user = (UserContext) securityContext.getUserPrincipal();
            Operador op = operadorFacade.get(user.getId());

            Compra entity = compraFacade.get(id);
            System.out.println(" ID: " + id + ", " + entity);

            if (entity == null) {
                simple.setOk(Boolean.FALSE);
                simple.setCodigo(-10);
                simple.setMensaje("Esta compra ya fue promovida por otro operador");
            } else if (!entity.getEstado().equals(CompraEnumerated.ESTADO.PENDIENTE)) {
                simple.setOk(Boolean.FALSE);
                simple.setCodigo(-10);
                simple.setMensaje("Esta compra ya fue promovida por otro operador");
            } else {
                entity.setEstado(estado);
                entity.setRespuesta(srs.getMotivo());
                entity.setOperadorId(op);
                entity.setFechaRespuesta(new Date());
                compraFacade.edit(entity);
                simple.setOk(Boolean.TRUE);
            }

            Response.ResponseBuilder rb = Response.ok(simple);
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
    @Path("buscar")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response buscar(Busqueda bus, @Context SecurityContext securityContext) {
       DashboardOperador root = new DashboardOperador();
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("operador")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Operador op = operadorFacade.get(user.getId(), "lugarId", "lugarId.imagenLogoId");
            Lugar lugar = op.getLugarId();
            List<Visita> visitas = visitaFacade.getBuscaVisitas(lugar, bus);
            List<Compra> compras = compraFacade.getBuscaCompras(lugar, bus);

            lugar.setCiudadId(null);
            lugar.setCodigoPostalId(null);
            lugar.setEventoList(null);
            lugar.setImagenPortadaId(null);
            lugar.setOfertaList(null);
            lugar.setPropietarioId(null);
            lugar.setSegmentoId(null);

            for (Visita v : visitas) {
                v.setLugarId(null);
                v.setOperadorId(null);
                v.getUsuarioId().setContrasena(null);
            }

            for (Compra c : compras) {
                c.setLugarId(null);
                c.setOperadorId(null);
                c.getUsuarioId().setContrasena(null);
                Oferta oferta = c.getOfertaId();
                oferta.setLugarId(null);
            }

            root.setLugar(lugar);
            root.setVisitas(visitas);
            root.setCompras(compras);

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
    @Path("visitas/pendientes")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response visitasPendientes(@Context SecurityContext securityContext) {

        DashboardOperador root = new DashboardOperador();
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("operador")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Operador op = operadorFacade.get(user.getId(), "lugarId", "lugarId.imagenLogoId");
            Lugar lugar = op.getLugarId();
            List<Visita> visitas = visitaFacade.getVisitasPendientes(lugar);

            lugar.setCiudadId(null);
            lugar.setCodigoPostalId(null);
            lugar.setEventoList(null);
            lugar.setImagenPortadaId(null);
            lugar.setOfertaList(null);
            lugar.setPropietarioId(null);
            lugar.setSegmentoId(null);

            for (Visita v : visitas) {
                v.setLugarId(null);
                v.setOperadorId(null);
                v.getUsuarioId().setContrasena(null);
            }

            root.setLugar(lugar);
            root.setVisitas(visitas);

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

}
