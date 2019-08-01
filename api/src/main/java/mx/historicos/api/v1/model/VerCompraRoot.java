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
public class VerCompraRoot {
    
    private String estatus;
    
    private Long id;
    private String nombre;
    private String descripcion;
    private String condiciones;
    private Float precio;
    private String imagenUrl;
    private Float calificacion;
    private Integer cargoTotal;
    private Boolean puedeComprar;
    private String mensaje;
    
    private List<VerCompraPuntos> puntos;

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Float calificacion) {
        this.calificacion = calificacion;
    }

    public Integer getCargoTotal() {
        return cargoTotal;
    }

    public void setCargoTotal(Integer cargoTotal) {
        this.cargoTotal = cargoTotal;
    }

    public Boolean getPuedeComprar() {
        return puedeComprar;
    }

    public void setPuedeComprar(Boolean puedeComprar) {
        this.puedeComprar = puedeComprar;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @XmlElement
    public List<VerCompraPuntos> getPuntos() {
        return puntos;
    }

    public void setPuntos(List<VerCompraPuntos> puntos) {
        this.puntos = puntos;
    }
    
}
