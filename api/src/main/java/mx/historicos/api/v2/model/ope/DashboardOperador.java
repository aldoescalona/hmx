/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.model.ope;

import java.util.List;
import mx.historicos.api.bean.Compra;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Visita;

/**
 *
 * @author 43700118
 */
public class DashboardOperador {
    
    private Boolean ok = Boolean.TRUE; 
    
    private Lugar lugar;
    private List<Compra> compras;
    private List<Visita> visitas;

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }

    public List<Visita> getVisitas() {
        return visitas;
    }

    public void setVisitas(List<Visita> visitas) {
        this.visitas = visitas;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }
 
    
}
