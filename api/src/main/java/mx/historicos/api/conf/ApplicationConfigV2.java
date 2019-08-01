/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.conf;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

/**
 *
 * @author 43700118
 */
@javax.ws.rs.ApplicationPath("v2")
public class ApplicationConfigV2 extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();

        resources.add(JacksonJsonProvider.class);
        // resources.add(JSONObjectProvider.class);

        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(mx.historicos.api.v2.rest.AppREST.class);
        resources.add(mx.historicos.api.v2.rest.AuthAdminREST.class);
        resources.add(mx.historicos.api.v2.rest.AuthClienteREST.class);
        resources.add(mx.historicos.api.v2.rest.AuthUsuarioREST.class);
        resources.add(mx.historicos.api.v2.rest.AuthOperadorREST.class);
        resources.add(mx.historicos.api.v2.rest.AdminREST.class);
        resources.add(mx.historicos.api.v2.rest.ClienteREST.class);
        resources.add(mx.historicos.api.v2.rest.CodigoPostalREST.class);
        resources.add(mx.historicos.api.v2.rest.ImagenREST.class);
        resources.add(mx.historicos.api.v2.rest.UsuarioREST.class);
        resources.add(mx.historicos.api.v2.rest.OperadorREST.class);
        resources.add(mx.historicos.api.v2.rest.EventoREST.class);
        resources.add(mx.historicos.api.v2.rest.SegmentoREST.class);
        resources.add(mx.historicos.api.security.CrossOriginResourceSharingFilter.class);
        resources.add(mx.historicos.api.security.RestSecurityFilter.class);
    }

}
