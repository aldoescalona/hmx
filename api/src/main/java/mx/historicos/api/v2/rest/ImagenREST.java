/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.rest;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import mx.historicos.api.bean.Evento;
import mx.historicos.api.bean.Imagen;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Oferta;
import mx.historicos.api.conf.Globales;
import mx.historicos.api.facade.EventoFacade;
import mx.historicos.api.facade.ImagenFacade;
import mx.historicos.api.facade.LugarFacade;
import mx.historicos.api.facade.OfertaFacade;
import mx.historicos.api.security.Secured;
import mx.historicos.api.security.UserContext;
import mx.historicos.api.util.APIUtil;
import mx.historicos.api.util.IOImageScalator;
import mx.historicos.api.util.AbstractImagenIO;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author 43700118
 */
@Stateless
@Path("imagen")
public class ImagenREST {

    @EJB
    private Globales globales;

    @EJB
    private ImagenFacade imagenFacade;

    @EJB
    private LugarFacade lugarFacade;

    @EJB
    private OfertaFacade ofertaFacade;

    @EJB
    private EventoFacade eventoFacade;

    public ImagenREST() {
    }

    @POST
    @Path("/upload/{entidad}/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response uploadFiles(@Context HttpServletRequest request, @Context ServletContext context, @PathParam("entidad") String entidad, @PathParam("id") Long id, @Context SecurityContext securityContext) {

        
        Imagen imagen = null;
        UserContext user = (UserContext) securityContext.getUserPrincipal();

        if (!user.verificaPermiso("cliente", "admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        AbstractImagenIO aimagen = getEntidad(entidad, id);

        if (aimagen == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        File dir = new File(globales.getFilePath());
        //  List<String> uploaded = new ArrayList();

        if (ServletFileUpload.isMultipartContent(request)) {
            final FileItemFactory factory = new DiskFileItemFactory();
            final ServletFileUpload fileUpload = new ServletFileUpload(factory);
            try {

                FileItemIterator iter = fileUpload.getItemIterator(request);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                while (iter.hasNext()) {
                    FileItemStream item = iter.next();
                    // final String itemName = item.getName();
                    // final String fieldName = item.getFieldName();

                    InputStream stream = item.openStream();

                    if (item.isFormField()) {
                        // System.out.println("Field Name: " + fieldName + ", andidate Name: " + Streams.asString(stream));
                    } else {

                        IOImageScalator scalator = new IOImageScalator(stream);

                        String uid = UUID.randomUUID().toString();
                        String a = String.format("%d/%sA.png", user.getId(), uid);
                        String b = String.format("%d/%sB.png", user.getId(), uid);
                        String c = String.format("%d/%sC.png", user.getId(), uid);

                        File destinoA = new File(dir, a);
                        File destinoB = new File(dir, b);
                        File destinoC = new File(dir, c);

                        scalator.scale(512, 512, destinoA);
                        scalator.scale(400, 400, destinoB);
                        scalator.scale(150, 150, destinoC);

                        imagen = new Imagen();
                        imagen.setImgA(a);
                        imagen.setImgB(b);
                        imagen.setImgC(c);

                        imagenFacade.create(imagen, aimagen, dir);

                        // uploaded.add(uid);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
        
        if(imagen == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Response.ResponseBuilder rb = Response.ok(imagen);
        Response resp = rb.header("Pragma", "no-cache")
                .header("Expires", "-1")
                .header("X-Cached-Result", "true")
                .header("X-Cache-Remaining", "99")
                .header("X-Cache-Expires", APIUtil.getExpiresHeader())
                .header("Date", APIUtil.getDateHeader())
                .build();

        return resp;
    }

    private AbstractImagenIO getEntidad(String entidad, Long id) {
        AbstractImagenIO img = null;

        if ("lugar-logo".equals(entidad)) {
            Lugar lugar = lugarFacade.get(id, "imagenLogoId");
            if (lugar == null) {
                return null;
            }

            img = new AbstractImagenIO<Lugar>(lugar) {
                @Override
                public Imagen getImagen() {
                    return getEntidad().getImagenLogoId();
                }

                @Override
                public void setImagen(Imagen imagen) {
                    getEntidad().setImagenLogoId(imagen);
                }
            };

        } else if ("lugar-portada".equals(entidad)) {
            Lugar lugar = lugarFacade.get(id, "imagenPortadaId");

            if (lugar == null) {
                return null;
            }
            img = new AbstractImagenIO<Lugar>(lugar) {
                @Override
                public Imagen getImagen() {
                    return getEntidad().getImagenPortadaId();
                }

                @Override
                public void setImagen(Imagen imagen) {
                    getEntidad().setImagenPortadaId(imagen);
                }
            };
        } else if ("oferta".equals(entidad)) {
            Oferta oferta = ofertaFacade.get(id, "imagenId");

            if (oferta == null) {
                return null;
            }
            img = new AbstractImagenIO<Oferta>(oferta) {
                @Override
                public Imagen getImagen() {
                    return getEntidad().getImagenId();
                }

                @Override
                public void setImagen(Imagen imagen) {
                    getEntidad().setImagenId(imagen);
                }
            };
        } else if ("evento".equals(entidad)) {
            Evento evento = eventoFacade.get(id, "imagenId");

            if (evento == null) {
                return null;
            }
            img = new AbstractImagenIO<Evento>(evento) {
                @Override
                public Imagen getImagen() {
                    return getEntidad().getImagenId();
                }

                @Override
                public void setImagen(Imagen imagen) {
                    getEntidad().setImagenId(imagen);
                }
            };
        }

        return img;
    }

}
