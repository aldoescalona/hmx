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
 * @author Aldo
 */
@XmlRootElement
public class ComprasRoot {

    private String estatus;
    
    private List<ComprasCompra> compras;

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    @XmlElement
    public List<ComprasCompra> getCompras() {
        return compras;
    }

    public void setCompras(List<ComprasCompra> compras) {
        this.compras = compras;
    }
    
}
