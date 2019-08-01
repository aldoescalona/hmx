/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v1.model;

/**
 *
 * @author 43700118
 */
public class VisitaRespuestaRoot {
    
    private String estatus;
    private String mensaje;
    private String mensajeLugar;

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensajeLugar() {
        return mensajeLugar;
    }

    public void setMensajeLugar(String mensajeLugar) {
        this.mensajeLugar = mensajeLugar;
    }
    
    
}
