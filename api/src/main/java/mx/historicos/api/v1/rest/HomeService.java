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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import mx.historicos.api.bean.Segmento;
import mx.historicos.api.bean.Usuario;
import mx.historicos.api.facade.SegmentoFacade;
import mx.historicos.api.facade.UsuarioFacade;
import mx.historicos.api.security.Secured;
import mx.historicos.api.security.UserContext;
import mx.historicos.api.util.APIUtil;
import mx.historicos.api.v1.model.HomeMenu;
import mx.historicos.api.v1.model.HomeRoot;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * REST Web Service
 *
 * @author 43700118
 */
@Path("home")
public class HomeService {

    private static Logger logger = Logger.getLogger(HomeService.class);

    @EJB
    private UsuarioFacade usuarioFacade;

    @EJB
    private SegmentoFacade segmentoFacade;

    @Context
    private UriInfo context;

    @GET
    @Path("/json")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response home(@Context SecurityContext securityContext) throws IOException {

        HomeRoot root = new HomeRoot();
        try {

            UserContext user = (UserContext) securityContext.getUserPrincipal();
            Long uid = user.getId();

            Usuario usuario = usuarioFacade.get(uid);

            if (usuario == null) {
                root.setEstatus("err-u404");
            } else {
                root.setEstatus("ok");
                root.setNombre(usuario.getNombre());
                root.setPuntos(usuario.getPuntos() - usuario.getRetenidos());

                if (usuario.getImagenURL() != null) {
                    root.setImagenURL(usuario.getImagenURL());
                }

                List<Segmento> list = segmentoFacade.list(Restrictions.eq("estado", Boolean.TRUE), Order.asc("orden"));
                List<HomeMenu> menu = new ArrayList();

                for (Segmento g : list) {
                    menu.add(new HomeMenu(g.getId(), g.getNombre()));
                }
                root.setMenu(menu);
            }
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
