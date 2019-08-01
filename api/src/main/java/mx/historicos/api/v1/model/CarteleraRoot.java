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
public class CarteleraRoot {

    private String estatus;

    private List<CarteleraEvento> eventos;

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    @XmlElement
    public List<CarteleraEvento> getEventos() {
        return eventos;
    }

    public void setEventos(List<CarteleraEvento> eventos) {
        this.eventos = eventos;
    }

}
