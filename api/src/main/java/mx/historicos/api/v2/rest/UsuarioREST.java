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
import javax.ws.rs.core.SecurityContext;
import mx.historicos.api.bean.Usuario;
import mx.historicos.api.facade.UsuarioFacade;

/**
 *
 * @author 43700118
 */

@Path("usuario")
public class UsuarioREST {

    @EJB
    private UsuarioFacade usuarioFacade;
    

    public UsuarioREST() {
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public void create_(Usuario entity){
        usuarioFacade.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Usuario entity) {
        usuarioFacade.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        usuarioFacade.remove(usuarioFacade.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Usuario find(@PathParam("id") Long id) {
        return usuarioFacade.find(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    // @Secured
    public List<Usuario> findAll(@Context SecurityContext securityContext) {
        
        System.out.println(" " + usuarioFacade);
        System.out.println(" securityContext.getUserPrincipal() : " + securityContext.getUserPrincipal());
        
        return usuarioFacade.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Usuario> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return usuarioFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(usuarioFacade.count());
    }

    
}
