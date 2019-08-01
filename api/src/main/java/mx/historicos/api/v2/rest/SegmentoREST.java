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
import mx.historicos.api.bean.Segmento;
import mx.historicos.api.facade.SegmentoFacade;
import mx.historicos.api.security.Secured;
import mx.historicos.api.security.UserContext;
import mx.historicos.api.util.APIUtil;
import mx.historicos.api.v2.model.CUDResponse;
import mx.historicos.api.v2.model.usr.RPaginatorResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author 43700118
 */
@Path("segmento")
public class SegmentoREST {

    private static Logger logger = Logger.getLogger(SegmentoREST.class);

    @EJB
    private SegmentoFacade segmentoFacade;

    public SegmentoREST() {
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response create(Segmento entity, @Context SecurityContext securityContext) {
        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            entity.setEstado(Boolean.TRUE);
            entity.setOrden(0);
            segmentoFacade.create(entity);

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
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response edit(@PathParam("id") Long id, Segmento entity, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            segmentoFacade.edit(entity);

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

    @DELETE
    @Path("{id}")
    @Secured
    public Response remove(@PathParam("id") Long id, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Segmento segmento = segmentoFacade.get(id);
            segmento.setEstado(Boolean.FALSE);
            segmentoFacade.edit(segmento);

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

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response find(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            Segmento segmento = segmentoFacade.find(id);
            segmento.setLugarList(null);

            CUDResponse root = new CUDResponse();
            root.setOk(Boolean.TRUE);
            root.setId(id.toString());

            Response.ResponseBuilder rb = Response.ok(segmento);
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
        // return 
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response findAll(@Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            List<Segmento> list = segmentoFacade.findAll();
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
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response findRange(@PathParam("from") Integer from, @PathParam("to") Integer to, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            List<Segmento> list = segmentoFacade.findRange(new int[]{from, to});
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
    @Path("page/{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response findRangePage(@PathParam("from") Integer from, @PathParam("to") Integer to, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            RPaginatorResponse root = new RPaginatorResponse();
            root.setOk(Boolean.TRUE);
            root.setCodigo(0);

            List<Segmento> list = segmentoFacade.findRange(new int[]{from, to});
            Integer c = segmentoFacade.count();

            for (Segmento segmento : list) {
                segmento.setLugarList(null);
            }

            root.setTotalResultados(c);
            root.setList(list);
            root.setDesde(from);
            root.setHasta(to);

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
