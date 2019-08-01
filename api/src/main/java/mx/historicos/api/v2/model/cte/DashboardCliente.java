/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.model.cte;

import java.util.List;
import mx.historicos.api.bean.Lugar;

/**
 *
 * @author 43700118
 */
public class DashboardCliente {
    
    private Boolean ok = Boolean.TRUE; 
    
    private List<Lugar> lugares;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public List<Lugar> getLugares() {
        return lugares;
    }

    public void setLugares(List<Lugar> lugares) {
        this.lugares = lugares;
    }
}
