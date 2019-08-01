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
import mx.historicos.api.bean.CodigoPostal;
import mx.historicos.api.facade.CodigoPostalFacade;
import mx.historicos.api.security.Secured;
import mx.historicos.api.security.UserContext;
import mx.historicos.api.util.APIUtil;
import mx.historicos.api.v2.model.CUDResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author 43700118
 */
@Path("codigoPostal")
public class CodigoPostalREST {

    private static Logger logger = Logger.getLogger(CodigoPostalREST.class);

    @EJB
    private CodigoPostalFacade codigoPostalFacade;

    public CodigoPostalREST() {
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response create(CodigoPostal entity, @Context SecurityContext securityContext) {
        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            codigoPostalFacade.create(entity);

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
    public Response edit(@PathParam("id") Long id, CodigoPostal entity, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            codigoPostalFacade.edit(entity);

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

            CodigoPostal codigoPostal = codigoPostalFacade.get(id);
            codigoPostalFacade.edit(codigoPostal);

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
    @Path("id/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response find(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            CodigoPostal codigoPostal = codigoPostalFacade.get(id, "ciudadId", "estadoId");
            codigoPostal.getCiudadId().setEstadoId(null);

            CUDResponse root = new CUDResponse();
            root.setOk(Boolean.TRUE);
            root.setId(id.toString());

            Response.ResponseBuilder rb = Response.ok(codigoPostal);
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
    @Path("{code}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response findCode(@PathParam("code") String code, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            
            List<CodigoPostal> list = codigoPostalFacade.getColonias(code);
            
            for (CodigoPostal cp : list) {
                cp.getCiudadId().setEstadoId(null);
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
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response findRange(@PathParam("from") Integer from, @PathParam("to") Integer to, @Context SecurityContext securityContext) {

        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();

            if (!user.verificaPermiso("admin")) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            List<CodigoPostal> list = codigoPostalFacade.findRange(new int[]{from, to});
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
