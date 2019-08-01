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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import mx.historicos.api.bean.Compra;
import mx.historicos.api.facade.CompraFacade;
import mx.historicos.api.security.Secured;
import mx.historicos.api.security.UserContext;
import mx.historicos.api.util.APIUtil;
import mx.historicos.api.v1.model.ComprasCompra;
import mx.historicos.api.v1.model.ComprasRoot;
import org.apache.log4j.Logger;

/**
 *
 * @author 43700118
 */
@Path("/compra")
public class CompraService {

    private static Logger logger = Logger.getLogger(CompraService.class);

    @EJB
    private CompraFacade compraFacade;
    
    @GET
    @Path("/lista")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response compras(@Context SecurityContext securityContext) throws IOException {
        ComprasRoot root = new ComprasRoot();
        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            Long uid = user.getId();

            System.out.println("UID:  " + uid);
            List<Compra> list = compraFacade.getComprasUsuario(uid);

            List<ComprasCompra> children = new ArrayList();
            for (Compra item : list) {
                ComprasCompra eve = new ComprasCompra();
                eve.setNombre(item.getOfertaId().getNombre());
                eve.setId(item.getId());
                eve.setLugar(item.getLugarId().getNombre());
                eve.setMonto(item.getPuntos());
                eve.setFecha(APIUtil.dateToDateString(item.getFecha()));
                eve.setEstado(item.getEstado().ordinal());
                eve.setFolio(item.getFolio());
                
                if(item.getOfertaId().getImagenId() != null){
                    eve.setImagenUrl(item.getOfertaId().getImagenId().getImgB());
                }
                
                children.add(eve);
            }
            root.setCompras(children);
            root.setEstatus("ok");
        } catch (Exception ex) {
            ex.printStackTrace();
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
