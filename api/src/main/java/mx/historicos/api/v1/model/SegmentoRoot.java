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
public class SegmentoRoot {
    
    private String estatus;
    private String segmento;
    
    private List<SegmentoLugar> lugares;

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    @XmlElement
    public List<SegmentoLugar> getLugares() {
        return lugares;
    }

    public void setLugares(List<SegmentoLugar> lugares) {
        this.lugares = lugares;
    }
    
}
