/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.rest;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
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
import mx.historicos.api.bean.Admin;
import mx.historicos.api.bean.Ciudad;
import mx.historicos.api.bean.Cliente;
import mx.historicos.api.bean.CodigoPostal;
import mx.historicos.api.bean.Evento;
import mx.historicos.api.bean.Imagen;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Operador;
import mx.historicos.api.enu.ClienteEnumerated;
import mx.historicos.api.enu.LugarEnumerated;
import mx.historicos.api.facade.AdminFacade;
import mx.historicos.api.facade.ClienteFacade;
import mx.historicos.api.facade.CodigoPostalFacade;
import mx.historicos.api.facade.EventoFacade;
import mx.historicos.api.facade.ImagenFacade;
import mx.historicos.api.facade.LugarFacade;
import mx.historicos.api.facade.OfertaFacade;
import mx.historicos.api.facade.OperadorFacade;
import mx.historicos.api.security.Secured;
import mx.historicos.api.security.UserContext;
import mx.historicos.api.util.APIUtil;
import mx.historicos.api.util.KeyGenerator;
import mx.historicos.api.v2.model.CUDResponse;
import mx.historicos.api.v2.model.adm.RegistroForm;
import mx.historicos.api.v2.model.adm.RegistroResponse;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author 43700118
 */
@Path("admin")
public class AdminREST {

    @EJB
    private AdminFacade adminFacade;

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

    @EJB
    private CodigoPostalFacade codigoPostalFacade;

    private static Logger logger = Logger.getLogger(AdminREST.class);

    public AdminREST() {
    }

    @GET
    @Path("id")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response cliente(@Context SecurityContext securityContext) {

        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Admin cte = adminFacade.get(user.getId());
            cte.setContrasena(null);
            cte.setCambioContrasena(null);

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

    ////////  CLIENTES ////////////
    @GET
    @Path("clientes")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response clientes(@Context SecurityContext securityContext) {

        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            List<Cliente> list = clienteFacade.findAll();

            for (Cliente cliente : list) {
                cliente.setLugarList(null);
                cliente.setPasssword(null);
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
    @Path("cliente/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response getCliente(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Cliente cliente = clienteFacade.get(id);
            if (cliente == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            cliente.setLugarList(null);
            cliente.setPasssword(null);

            String out = APIUtil.toStringUsingJackson(cliente);
            System.out.println(" out: " + out);

            Response.ResponseBuilder rb = Response.ok(cliente);
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
    @Path("registro")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response createCliente(RegistroForm formin, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            String out = APIUtil.toStringUsingJackson(formin);
            System.out.println(" out: " + out);

            String pas = KeyGenerator.generate(9, KeyGenerator.CHARSET.ALPHANUMERIC);
            String cif = clienteFacade.cifraPasssword(pas);

            Cliente cliente = formin.getCliente();
            cliente.setEstado(ClienteEnumerated.ESTADO_CTE.ACTIVO);
            cliente.setCambiarPasssword(Boolean.TRUE);
            cliente.setPasssword(cif);
            clienteFacade.create(cliente);

            Lugar lugar = formin.getLugar();
            lugar.setPropietarioId(cliente);

            CodigoPostal codigop = codigoPostalFacade.get(lugar.getCodigoPostalId().getId(), "ciudadId");
            Ciudad ciudad = codigop.getCiudadId();
            lugar.setCiudadId(ciudad);
            lugar.setCodigoPostalId(codigop);
            // entity.setPropietarioId(entity);
            lugar.setTipo(LugarEnumerated.LUGAR_TIPO.ADMINISTRADO);
            lugar.setCalificacion(BigDecimal.ZERO);
            lugar.setCalificaciones(0);
            lugar.setCalificacion1(0);
            lugar.setCalificacion2(0);
            lugar.setCalificacion3(0);
            lugar.setCalificacion4(0);
            lugar.setCalificacion5(0);
            lugar.setOrden(0);
            lugar.setEstado(LugarEnumerated.ESTADO_LGR.PENDIENTE);
            lugarFacade.create(lugar);

            String oppas = KeyGenerator.generate(9, KeyGenerator.CHARSET.ALPHANUMERIC);
            String opcif = clienteFacade.cifraPasssword(pas);

            Operador operador = formin.getOperador();
            operador.setEstado(Boolean.TRUE);
            operador.setLugarId(lugar);
            operador.setContrasena(opcif);
            operadorFacade.crear(operador);

            RegistroResponse root = new RegistroResponse();
            root.setOk(Boolean.TRUE);
            root.setClienteContrasena(pas);
            root.setClienteId(cliente.getId());
            root.setOperadorContrasena(oppas);
            root.setOperadorId(operador.getId());
            root.setAlias(lugar.getAlias());
            root.setLugarId(lugar.getId());

            Response.ResponseBuilder rb = Response.ok(root);
            Response resp = rb.header("Pragma", "no-cache")
                    .header("Expires", "-1")
                    .header("X-Cached-Result", "true")
                    .header("X-Cache-Remaining", "99")
                    .header("X-Cache-Expires", APIUtil.getExpiresHeader())
                    .header("Date", APIUtil.getDateHeader())
                    .build();

            return resp;
        } catch (EJBException e) {
            logger.error(e, e);
            if (e.getCause() != null && e.getCause() instanceof ConstraintViolationException) {
                RegistroResponse root = new RegistroResponse();
                root.setOk(Boolean.FALSE);
                root.setMensaje("Dato restringido verifique que no exista el correo del cliente");
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity(root).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error(e, e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("cliente/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response editCliente(@PathParam("id") Long id, Cliente entity, @Context SecurityContext securityContext) {
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            // Cliente cte = clienteFacade.get(user.getId());
            String out = APIUtil.toStringUsingJackson(entity);
            System.out.println(" out: " + out);

            Cliente cliente = clienteFacade.get(id);

            if (cliente == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            cliente.setNombre(entity.getNombre());
            // cliente.setCorreo(entity.getCorreo());

            // entity.setEstado(Boolean.TRUE);
            clienteFacade.edit(cliente);

            CUDResponse root = new CUDResponse();
            root.setOk(Boolean.TRUE);
            root.setId(cliente.getId().toString());

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
    @Path("cliente/{id}")
    @Secured
    public Response removeCliente(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            System.out.println(" out: " + id);

            Cliente cliente = clienteFacade.get(id);
            if (cliente == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            // clienteFacade.remove(cliente);
            cliente.setEstado(ClienteEnumerated.ESTADO_CTE.INACTIVO);
            clienteFacade.edit(cliente);

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

    ////////  LUGARES ////////////
    @GET
    @Path("lugares")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response lugares(@Context SecurityContext securityContext) {

        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Admin adm = adminFacade.get(user.getId());
            List<Lugar> list = lugarFacade.getLugaresAdministrados(adm);

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
    @Path("lugares/publico")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response lugaresPublico(@Context SecurityContext securityContext) {

        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Admin cte = adminFacade.get(user.getId());
            List<Lugar> list = lugarFacade.getLugaresPublico();

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
    @Path("lugar/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response getLugar(@PathParam("id") Long id) {
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

    @POST
    @Path("lugar")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response createLugar(Lugar entity, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            CodigoPostal codigop = codigoPostalFacade.get(entity.getCodigoPostalId().getId(), "ciudadId");
            Ciudad ciudad = codigop.getCiudadId();

            String out = APIUtil.toStringUsingJackson(entity);
            System.out.println(" out: " + out);

            entity.setCiudadId(ciudad);
            entity.setCodigoPostalId(codigop);
            // entity.setPropietarioId(entity);
            entity.setTipo(LugarEnumerated.LUGAR_TIPO.ADMINISTRADO);
            entity.setCalificacion(BigDecimal.ZERO);
            entity.setCalificaciones(0);
            entity.setCalificacion1(0);
            entity.setCalificacion2(0);
            entity.setCalificacion3(0);
            entity.setCalificacion4(0);
            entity.setCalificacion5(0);
            entity.setOrden(0);
            entity.setEstado(LugarEnumerated.ESTADO_LGR.PENDIENTE);

            lugarFacade.create(entity);

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
        } catch (EJBException e) {
            logger.error(e, e);
            if (e.getCause() != null && e.getCause() instanceof ConstraintViolationException) {
                CUDResponse root = new CUDResponse();
                root.setOk(Boolean.FALSE);
                root.setMensaje("Dato restringido verifique que no exista el correo del cliente");
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity(root).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error(e, e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("lugar/no_admin")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response createLugarNoadm(Lugar entity, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            CodigoPostal codigop = codigoPostalFacade.get(entity.getCodigoPostalId().getId(), "ciudadId");
            Ciudad ciudad = codigop.getCiudadId();

            String out = APIUtil.toStringUsingJackson(entity);
            System.out.println(" out: " + out);

            entity.setCiudadId(ciudad);
            entity.setCodigoPostalId(codigop);
            // entity.setPropietarioId(entity);
            entity.setTipo(LugarEnumerated.LUGAR_TIPO.NO_ADMINISTRADO);
            entity.setCalificacion(BigDecimal.ZERO);
            entity.setCalificaciones(0);
            entity.setCalificacion1(0);
            entity.setCalificacion2(0);
            entity.setCalificacion3(0);
            entity.setCalificacion4(0);
            entity.setCalificacion5(0);
            entity.setOrden(0);
            entity.setEstado(LugarEnumerated.ESTADO_LGR.ACTIVO);

            lugarFacade.create(entity);

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
        } catch (EJBException e) {
            logger.error(e, e);
            if (e.getCause() != null && e.getCause() instanceof ConstraintViolationException) {
                CUDResponse root = new CUDResponse();
                root.setOk(Boolean.FALSE);
                root.setMensaje("Dato restringido verifique que no exista el correo del cliente");
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity(root).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
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

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            // Cliente cte = clienteFacade.get(user.getId());
            String out = APIUtil.toStringUsingJackson(entity);
            System.out.println(" out: " + out);

            Lugar ent = lugarFacade.get(id);

            if (ent == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            ent.setNombre(entity.getNombre());
            ent.setAlias(entity.getAlias());
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

    @PUT
    @Path("lugar/activacion/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response activaLugar(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Lugar ent = lugarFacade.get(id);

            if (ent == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            ent.setEstado(LugarEnumerated.ESTADO_LGR.ACTIVO);
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

    @PUT
    @Path("lugar/suspencion/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response suspendeLugar(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Lugar ent = lugarFacade.get(id);

            if (ent == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            ent.setEstado(LugarEnumerated.ESTADO_LGR.SUSPENDIDO);
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

    @PUT
    @Path("lugar/pendiente/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response pendienteLugar(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Lugar ent = lugarFacade.get(id);

            if (ent == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            ent.setEstado(LugarEnumerated.ESTADO_LGR.PENDIENTE);
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

    //////   EVENTOS
    @GET
    @Path("eventos/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response eventosLugar(@PathParam("id") Long id, @Context SecurityContext securityContext) {

        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            if (!user.verificaPermiso("admin")) {
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
    public Response findEvento(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

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

            if (!user.verificaPermiso("admin")) {
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

            if (!user.verificaPermiso("admin")) {
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

            if (!user.verificaPermiso("admin")) {
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

}
