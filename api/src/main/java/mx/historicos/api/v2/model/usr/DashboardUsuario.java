/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.model.usr;

import java.util.List;
import mx.historicos.api.bean.Segmento;
import mx.historicos.api.bean.Usuario;

/**
 *
 * @author 43700118
 */
public class DashboardUsuario {
    
    private Usuario usuario;
    private List<Segmento> segmentos;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Segmento> getSegmentos() {
        return segmentos;
    }

    public void setSegmentos(List<Segmento> segmentos) {
        this.segmentos = segmentos;
    }
    
    
    
    
}
