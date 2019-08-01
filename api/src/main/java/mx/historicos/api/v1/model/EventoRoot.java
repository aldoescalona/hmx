/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v1.model;

/**
 *
 * @author Aldo
 */
public class EventoRoot {

    private String estatus;

    private String titulo;
    private String fechaHora;
    private String imagenUrl;

    private String lugarNombre;
    private String lugarDireccion;
    private String lugarImagenUrl;
    private String lugarMapaUrl;
    private String lugarTelefono;

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getLugarNombre() {
        return lugarNombre;
    }

    public void setLugarNombre(String lugarNombre) {
        this.lugarNombre = lugarNombre;
    }

    public String getLugarDireccion() {
        return lugarDireccion;
    }

    public void setLugarDireccion(String lugarDireccion) {
        this.lugarDireccion = lugarDireccion;
    }

    public String getLugarImagenUrl() {
        return lugarImagenUrl;
    }

    public void setLugarImagenUrl(String lugarImagenUrl) {
        this.lugarImagenUrl = lugarImagenUrl;
    }

    public String getLugarMapaUrl() {
        return lugarMapaUrl;
    }

    public void setLugarMapaUrl(String lugarMapaUrl) {
        this.lugarMapaUrl = lugarMapaUrl;
    }

    public String getLugarTelefono() {
        return lugarTelefono;
    }

    public void setLugarTelefono(String lugarTelefono) {
        this.lugarTelefono = lugarTelefono;
    }

    
    
}
