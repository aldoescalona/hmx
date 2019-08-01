/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.rest;

import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import mx.historicos.api.bean.Cliente;
import mx.historicos.api.bean.Evento;
import mx.historicos.api.bean.Imagen;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Oferta;
import mx.historicos.api.bean.Operador;
import mx.historicos.api.facade.ClienteFacade;
import mx.historicos.api.facade.EventoFacade;
import mx.historicos.api.facade.ImagenFacade;
import mx.historicos.api.facade.LugarFacade;
import mx.historicos.api.facade.OfertaFacade;
import mx.historicos.api.facade.OperadorFacade;
import mx.historicos.api.security.Secured;
import mx.historicos.api.security.UserContext;
import mx.historicos.api.util.APIUtil;
import mx.historicos.api.v2.model.CUDResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author 43700118
 */
@Path("cliente")
public class ClienteREST {

    @EJB
    private ClienteFacade clienteFacade;

    @EJB
    private LugarFacade lugarFacade;

    @EJB
    private OfertaFacade ofertaFacade;

    @EJB
    private EventoFacade eventoFacade;

    @EJB
    private OperadorFacade operadorFacade;

    @EJB
    private ImagenFacade imagenFacade;

    private static Logger logger = Logger.getLogger(ClienteREST.class);

    public ClienteREST() {
    }

    @GET
    @Path("lugares")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response lugares(@Context SecurityContext securityContext) {

        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            if (!user.verificaPermiso("cliente")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Cliente cte = clienteFacade.get(user.getId());
            List<Lugar> list = lugarFacade.getLugares(cte);

            for (Lugar ent : list) {
                ent.setEventoList(null);
                ent.setImagenPortadaId(null);
                ent.setOfertaList(null);
                ent.setPropietarioId(null);
                ent.setSegmentoId(null);
                ent.setCiudadId(null);
                ent.setCodigoPostalId(null);
            }

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
    @Path("id")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response cliente(@Context SecurityContext securityContext) {

        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            if (!user.verificaPermiso("cliente")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Cliente cte = clienteFacade.get(user.getId());
            cte.setPasssword(null);
            cte.setCambiarPasssword(null);
            cte.setLugarList(null);
            
            Response.ResponseBuilder rb = Response.ok(cte);
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
    @Path("lugar/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response findLugar(@PathParam("id") Long id) {
        try {

            // Oferta oferta = ofertaFacade.get(id, "lugarId", "lugarId.imagenLogoId", "imagenId");
            // Oferta oferta = ofertaFacade.get(id, "imagenId");
            Lugar lugar = lugarFacade.get(id, "imagenLogoId", "imagenPortadaId");
            if (lugar == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            lugar.setEventoList(null);
            lugar.setOfertaList(null);
            lugar.setPropietarioId(null);
            lugar.setSegmentoId(null);
            lugar.setCiudadId(null);
            lugar.setCodigoPostalId(null);

            String out = APIUtil.toStringUsingJackson(lugar);
            System.out.println(" out: " + out);

            Response.ResponseBuilder rb = Response.ok(lugar);
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
    @Path("lugar/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response modificaLugar(@PathParam("id") Long id, Lugar entity, @Context SecurityContext securityContext) {
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("cliente")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            // Cliente cte = clienteFacade.get(user.getId());
            String out = APIUtil.toStringUsingJackson(entity);
            System.out.println(" out: " + out);

            Lugar ent = lugarFacade.get(id);

            if (ent == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            ent.setDireccion(entity.getDireccion());
            ent.setDescripcion(entity.getDescripcion());
            ent.setTelefono(entity.getTelefono());
            ent.setSitioWeb(entity.getSitioWeb());
            ent.setCorreo(entity.getCorreo());
            ent.setUbicacionUrl(entity.getUbicacionUrl());
            ent.setUbicacionLng(entity.getUbicacionLng());
            ent.setUbicacionLtd(entity.getUbicacionLtd());
            ent.setPrecioMin(entity.getPrecioMin());
            ent.setPrecioMax(entity.getPrecioMax());

            lugarFacade.edit(ent);

            CUDResponse root = new CUDResponse();
            root.setOk(Boolean.TRUE);
            root.setId(ent.getId().toString());

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

    //////   OFERTAS
    @GET
    @Path("ofertas/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response ofertasLugar(@PathParam("id") Long id, @Context SecurityContext securityContext) {

        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            if (!user.verificaPermiso("cliente", "admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Lugar lugar = lugarFacade.get(id);

            if (lugar == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            List<Oferta> list = ofertaFacade.getOfertas(lugar, null);

            for (Oferta oferta : list) {
                Lugar l = oferta.getLugarId();
                l.setEventoList(null);
                l.setImagenPortadaId(null);
                l.setOfertaList(null);
                l.setPropietarioId(null);
                l.setSegmentoId(null);
                l.setCiudadId(null);
                l.setCodigoPostalId(null);
            }

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
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("oferta/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response findOferta(@PathParam("id") Long id) {
        try {

            // Oferta oferta = ofertaFacade.get(id, "lugarId", "lugarId.imagenLogoId", "imagenId");
            Oferta oferta = ofertaFacade.get(id, "imagenId");
            if (oferta == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            oferta.setLugarId(null);

            String out = APIUtil.toStringUsingJackson(oferta);
            System.out.println(" out: " + out);

            Response.ResponseBuilder rb = Response.ok(oferta);
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
    @Path("oferta")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response createOferta(Oferta entity, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("cliente", "admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            String out = APIUtil.toStringUsingJackson(entity);
            System.out.println(" out: " + out);

            entity.setEstado(Boolean.TRUE);
            entity.setBaja(Boolean.FALSE);
            entity.setOrden(0);

            ofertaFacade.create(entity);

            CUDResponse root = new CUDResponse();
            root.setOk(Boolean.TRUE);
            root.setId(entity.getId().toString());

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
    @Path("oferta/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response editOferta(@PathParam("id") Long id, Oferta entity, @Context SecurityContext securityContext) {
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("cliente", "admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            // Cliente cte = clienteFacade.get(user.getId());
            String out = APIUtil.toStringUsingJackson(entity);
            System.out.println(" out: " + out);

            Oferta oferta = ofertaFacade.get(id);

            if (oferta == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            oferta.setTipo(entity.getTipo());
            oferta.setNombre(entity.getNombre());
            oferta.setDescripcion(entity.getDescripcion());
            oferta.setCondiciones(entity.getCondiciones());
            oferta.setPrecio(entity.getPrecio());
            oferta.setEstado(entity.getEstado());

            // entity.setEstado(Boolean.TRUE);
            ofertaFacade.edit(oferta);

            CUDResponse root = new CUDResponse();
            root.setOk(Boolean.TRUE);
            root.setId(oferta.getId().toString());

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

    @DELETE
    @Path("oferta/{id}")
    @Secured
    public Response removeOferta(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("cliente", "admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            // Cliente cte = clienteFacade.get(user.getId());
            System.out.println(" out: " + id);

            Oferta oferta = ofertaFacade.get(id);
            if (oferta == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            oferta.setBaja(Boolean.TRUE);

            ofertaFacade.edit(oferta);

            CUDResponse root = new CUDResponse();
            root.setOk(Boolean.TRUE);
            root.setId(id.toString());

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

    //////   EVENTOS
    @GET
    @Path("eventos/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response eventosLugar(@PathParam("id") Long id, @Context SecurityContext securityContext) {

        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            if (!user.verificaPermiso("cliente")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Lugar lugar = lugarFacade.get(id);

            if (lugar == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            List<Evento> list = eventoFacade.getEventos(lugar, null);

            for (Evento evento : list) {
                evento.setLugarId(null);
                /*Lugar l = evento.getLugarId();
                l.setEventoList(null);
                l.setImagenPortadaId(null);
                l.setEventoList(null);
                l.setOperadorList(null);
                l.setPropietarioId(null);
                l.setSegmentoId(null);*/
            }

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
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("evento/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response findEvento(@PathParam("id") Long id) {
        try {

            Evento evento = eventoFacade.get(id, "lugarId", "lugarId.imagenLogoId", "imagenId");
            if (evento == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            evento.setLugarId(null);

            String out = APIUtil.toStringUsingJackson(evento);
            System.out.println(" out: " + out);

            Response.ResponseBuilder rb = Response.ok(evento);
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
    @Path("evento")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response createEvento(Evento entity, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("cliente")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            String out = APIUtil.toStringUsingJackson(entity);
            System.out.println(" out: " + out);

            entity.setEstado(Boolean.TRUE);

            eventoFacade.create(entity);

            CUDResponse root = new CUDResponse();
            root.setOk(Boolean.TRUE);
            root.setId(entity.getId().toString());

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
    @Path("evento/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response editEvento(@PathParam("id") Long id, Evento entity, @Context SecurityContext securityContext) {
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("cliente", "admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            // Cliente cte = clienteFacade.get(user.getId());
            String out = APIUtil.toStringUsingJackson(entity);
            System.out.println(" out: " + out);

            Evento evento = eventoFacade.get(id);

            if (evento == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            evento.setTipo(entity.getTipo());
            evento.setTitulo(entity.getTitulo());
            evento.setFechaInicio(entity.getFechaInicio());
            evento.setFechaFin(entity.getFechaFin());
            evento.setEstado(entity.getEstado());

            // entity.setEstado(Boolean.TRUE);
            eventoFacade.edit(evento);

            CUDResponse root = new CUDResponse();
            root.setOk(Boolean.TRUE);
            root.setId(evento.getId().toString());

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

    @DELETE
    @Path("evento/{id}")
    @Secured
    public Response removeEvento(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("cliente", "admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            System.out.println(" out: " + id);

            Evento evento = eventoFacade.get(id);
            if (evento == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            Imagen img = evento.getImagenId();
            eventoFacade.remove(evento);
            imagenFacade.eliminarConArchivos(img);

            CUDResponse root = new CUDResponse();
            root.setOk(Boolean.TRUE);
            root.setId(id.toString());

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

    //////   OPERADORES
    @GET
    @Path("operadores/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response operadoresLugar(@PathParam("id") Long id, @Context SecurityContext securityContext) {

        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            if (!user.verificaPermiso("cliente", "admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Lugar lugar = lugarFacade.get(id);

            if (lugar == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            List<Operador> list = operadorFacade.getOperadores(lugar, Boolean.TRUE);

            for (Operador operador : list) {
                operador.setLugarId(null);
                operador.setContrasena(null);
                /*Lugar l = operador.getLugarId();
                l.setOperadorList(null);
                l.setImagenPortadaId(null);
                l.setOperadorList(null);
                l.setOperadorList(null);
                l.setPropietarioId(null);
                l.setSegmentoId(null);*/
            }

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
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("operador/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response findOperador(@PathParam("id") Long id) {
        try {

            Operador operador = operadorFacade.get(id);
            if (operador == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            operador.setLugarId(null);
            operador.setContrasena(null);

            String out = APIUtil.toStringUsingJackson(operador);
            System.out.println(" out: " + out);

            Response.ResponseBuilder rb = Response.ok(operador);
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
    @Path("operador")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response createOperador(Operador entity, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("cliente", "admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            String out = APIUtil.toStringUsingJackson(entity);
            System.out.println(" out: " + out);

            entity.setEstado(Boolean.TRUE);

            operadorFacade.crear(entity);

            CUDResponse root = new CUDResponse();
            root.setOk(Boolean.TRUE);
            root.setId(entity.getId().toString());

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
    @Path("operador/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response editOperador(@PathParam("id") Long id, Operador entity, @Context SecurityContext securityContext) {
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("cliente", "admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            // Cliente cte = clienteFacade.get(user.getId());
            String out = APIUtil.toStringUsingJackson(entity);
            System.out.println(" out: " + out);

            Operador operador = operadorFacade.get(id);

            if (operador == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            operador.setClave(entity.getClave());
            // operador.setContrasena(entity.getContrasena());
            operador.setNombre(entity.getNombre());
            operador.setEstado(entity.getEstado());

            // entity.setEstado(Boolean.TRUE);
            operadorFacade.edit(operador);

            CUDResponse root = new CUDResponse();
            root.setOk(Boolean.TRUE);
            root.setId(operador.getId().toString());

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

    @DELETE
    @Path("operador/{id}")
    @Secured
    public Response removeOperador(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("cliente", "admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            System.out.println(" out: " + id);

            Operador operador = operadorFacade.get(id);
            if (operador == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            // operadorFacade.remove(operador);
            operador.setEstado(Boolean.FALSE);
            operadorFacade.edit(operador);

            CUDResponse root = new CUDResponse();
            root.setOk(Boolean.TRUE);
            root.setId(id.toString());

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
    @Path("operador/cambiarpass/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response changePassOperador(@PathParam("id") Long id, Operador entity, @Context SecurityContext securityContext) {
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("cliente", "admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            // Cliente cte = clienteFacade.get(user.getId());
            String out = APIUtil.toStringUsingJackson(entity);
            System.out.println(" out: " + out);

            Operador operador = operadorFacade.get(id);
            
            if (operador == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            operador.setContrasena(entity.getContrasena());
            operadorFacade.cambiarPass(operador);

            CUDResponse root = new CUDResponse();
            root.setOk(Boolean.TRUE);
            root.setId(operador.getId().toString());

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
