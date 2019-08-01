/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v1.rest;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import mx.historicos.api.bean.Compra;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Oferta;
import mx.historicos.api.bean.Usuario;
import mx.historicos.api.bean.UsuarioPuntos;
import mx.historicos.api.enu.CompraEnumerated;
import mx.historicos.api.facade.LugarFacade;
import mx.historicos.api.facade.OfertaFacade;
import mx.historicos.api.facade.UsuarioFacade;
import mx.historicos.api.facade.UsuarioPuntosFacade;
import mx.historicos.api.security.Secured;
import mx.historicos.api.security.UserContext;
import mx.historicos.api.util.APIUtil;
import mx.historicos.api.v1.model.CargoPuntos;
import mx.historicos.api.v1.model.CompraRoot;
import mx.historicos.api.v1.model.VerCompraPuntos;
import mx.historicos.api.v1.model.VerCompraRoot;
import org.apache.log4j.Logger;

/**
 * REST Web Service
 *
 * @author 43700118
 */
@Path("oferta")
public class OfertaService {

    private static Logger logger = Logger.getLogger(OfertaService.class);
    
    @EJB
    private UsuarioFacade usuarioFacade;
    
     @EJB
    private LugarFacade lugarFacade;
    
    @EJB
    private OfertaFacade ofertaFacade;
    
    @EJB
    private UsuarioPuntosFacade usuarioPuntosFacade;
    
    @Context
    private UriInfo context;

    @GET
    @Path("/{pseId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response verCompra(@PathParam("pseId") final Long pseId, @Context SecurityContext securityContext) throws IOException {
        VerCompraRoot root = new VerCompraRoot();
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();
            Long uid = user.getId();


            Oferta pse = ofertaFacade.get(pseId, "lugarId", "lugarId.propietarioId", "imagenId");
            Lugar currentEstablecimiento = pse.getLugarId();
            UsuarioPuntos usuarioPuntos = usuarioPuntosFacade.getPuntos(currentEstablecimiento, uid);

            if (usuarioPuntos == null) {
                usuarioPuntos = new UsuarioPuntos();
                usuarioPuntos.setPuntos(0);
                usuarioPuntos.setRetenidos(0);
            }

            if(pse.getCalificacion() != null){
                root.setCalificacion(pse.getCalificacion().floatValue());
            }
            root.setCondiciones(pse.getCondiciones());
            root.setDescripcion(pse.getDescripcion());
            root.setId(pseId);
            root.setNombre(pse.getNombre());
            root.setPrecio(pse.getPrecio().floatValue());
            if(pse.getImagenId() != null){
                root.setImagenUrl(pse.getImagenId().getImgB());
            }

            List<VerCompraPuntos> cargos = new ArrayList();

            Integer puntosestablecimiento = usuarioPuntos.getPuntos() - usuarioPuntos.getRetenidos();
            Integer precioMeta = pse.getPrecio();
            Integer cargototal = 0;

            VerCompraPuntos cargo = new VerCompraPuntos();

            cargo.setTasa(1f);
            cargo.setOrigen(currentEstablecimiento.getNombre());
            cargo.setPuntos(puntosestablecimiento);
            if (precioMeta <= puntosestablecimiento) {
                cargo.setCargo(precioMeta);
                cargototal += precioMeta;
            } else {
                cargo.setCargo(puntosestablecimiento);
                cargototal += puntosestablecimiento;
            }
            cargos.add(cargo);

            if (precioMeta > cargototal) {
                List<CargoPuntos> puntajes = usuarioPuntosFacade.getPuntajesConevenios(currentEstablecimiento, uid);
                for (CargoPuntos cp : puntajes) {
                    VerCompraPuntos vcp = new VerCompraPuntos();
                    vcp.setOrigen(cp.getNombre());
                    vcp.setPuntos(cp.getPuntos());
                    vcp.setTasa(cp.getTasa());
                    vcp.setOrigenId(cp.getId());

                    int porCubrirCrudo = precioMeta - cargototal;
                    int tiene = (int) (cp.getPuntos() * cp.getTasa());

                    if (tiene <= porCubrirCrudo) {
                        cargototal += tiene;
                        vcp.setCargo(vcp.getPuntos());
                        cargos.add(vcp);
                    } else {
                        cargototal += porCubrirCrudo;
                        vcp.setCargo((int) (porCubrirCrudo / vcp.getTasa()));
                        cargos.add(vcp);
                        break;
                    }
                }
            }
            boolean puedeComprar = precioMeta <= cargototal;
            
            if(!puedeComprar){
                root.setMensaje("Saldo insuficiente");
            }
            
            root.setPuedeComprar(puedeComprar);
            root.setCargoTotal(cargototal);
            root.setPuntos(cargos);
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

    @POST
    @Path("/comprar/{pseId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response comprar(@PathParam("pseId") final Long pseId, @Context SecurityContext securityContext) throws IOException {

        CompraRoot root = new CompraRoot();
        try {
            UserContext user = (UserContext) securityContext.getUserPrincipal();
            Long uid = user.getId();


            Oferta p = ofertaFacade.get(pseId, "lugarId", "imagenId");
            Lugar e = p.getLugarId();
            UsuarioPuntos usuarioPuntos = usuarioPuntosFacade.getPuntos(e, uid);
            

            Integer puntosestablecimiento = usuarioPuntos.getPuntos() - usuarioPuntos.getRetenidos();
            Integer precioMeta = p.getPrecio();
            Integer cargototal = 0;

            List<CargoPuntos> cargos = new ArrayList();

            CargoPuntos cargo = new CargoPuntos();
            cargo.setId(e.getId());
            cargo.setTasa(1f);
            cargo.setNombre(e.getNombre());
            cargo.setPuntos(puntosestablecimiento);
            if (precioMeta <= puntosestablecimiento) {
                cargo.setCargo(precioMeta);
                cargototal += precioMeta;
            } else {
                cargo.setCargo(puntosestablecimiento);
                cargototal += puntosestablecimiento;
            }
            cargos.add(cargo);

            Lugar esta = lugarFacade.get(e.getId(), "propietarioId");
            if (precioMeta > cargototal) {
                List<CargoPuntos> puntajes = usuarioPuntosFacade.getPuntajesConevenios(esta, uid);
                for (CargoPuntos cp : puntajes) {

                    int porCubrirCrudo = precioMeta - cargototal;
                    int tiene = (int) (cp.getPuntos() * cp.getTasa());

                    if (tiene <= porCubrirCrudo) {
                        cargototal += tiene;
                        cp.setCargo(cp.getPuntos());
                        cargos.add(cp);
                    } else {
                        cargototal += porCubrirCrudo;
                        cp.setCargo((int) (porCubrirCrudo / cp.getTasa()));
                        cargos.add(cp);
                        break;
                    }
                }
            }
            boolean puedeComprar = precioMeta <= cargototal;
            if (precioMeta > cargototal) {
                root.setComprado(Boolean.FALSE);
                root.setMensaje("No tiene suficientes puntos para realizar esta compra");
            } else {
                Compra compra = new Compra();
                compra.setLugarId(e);
                compra.setUsuarioId(new Usuario(uid));
                compra.setOfertaId(p);
                compra.setEstado(CompraEnumerated.ESTADO.PENDIENTE);
                compra.setFecha(new Date());
                compra.setPuntos(p.getPrecio().intValue());

                lugarFacade.compra(compra, cargos);

                root.setNombre(p.getNombre());
                root.setFolio(compra.getFolio().toString());
                root.setComprado(Boolean.TRUE);
                
                if(p.getImagenId() != null){
                    root.setImagenUrl(p.getImagenId().getImgA());
                }
            }
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
