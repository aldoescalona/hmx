/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v1.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 43700118
 */
@XmlRootElement
public class TicketsRoot {
    
    private String estatus;
    
    private List<TicketsVisita> tickets;

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    @XmlElement
    public List<TicketsVisita> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketsVisita> tickets) {
        this.tickets = tickets;
    }
   
    
}
