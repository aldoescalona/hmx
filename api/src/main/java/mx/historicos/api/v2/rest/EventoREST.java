/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.rest;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import mx.historicos.api.bean.Evento;
import mx.historicos.api.facade.EventoFacade;

/**
 *
 * @author 43700118
 */

@Path("evento")
public class EventoREST {

    @EJB
    private EventoFacade eventoFacade;

    public EventoREST() {
    }


    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Evento find(@PathParam("id") Long id) {
        return eventoFacade.find(id);
    }

}
