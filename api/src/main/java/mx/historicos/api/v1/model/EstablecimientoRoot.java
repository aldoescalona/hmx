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
public class EstablecimientoRoot {
    
    private String estatus;
    private String token;
    private Long id;
    private Integer puntos;
    private String nombre;
    private String calificacion;
    private Integer calificacionGraf;
    private String direccion;
    private String mapaUrl;
    private String telefono;
    private String imagenUrl;
    private String descripcion;

    
    private List<EstablecimientoOferta> ofertas;

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public Integer getCalificacionGraf() {
        return calificacionGraf;
    }

    public void setCalificacionGraf(Integer calificacionGraf) {
        this.calificacionGraf = calificacionGraf;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMapaUrl() {
        return mapaUrl;
    }

    public void setMapaUrl(String mapaUrl) {
        this.mapaUrl = mapaUrl;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    

    @XmlElement
    public List<EstablecimientoOferta> getOfertas() {
        return ofertas;
    }

    public void setOfertas(List<EstablecimientoOferta> ofertas) {
        this.ofertas = ofertas;
    }
    
    
    
}
