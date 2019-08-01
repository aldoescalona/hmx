/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v1.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 43700118
 */
@XmlRootElement
public class EstablecimientoOferta {
    
    private Long id;
    private String nombre;
    private String descripcion;
    private String condiciones;
    private Float precio;
    private String imagenUrl;
    private Float calificacion;

    public EstablecimientoOferta(Long id, String nombre, String descripcion, String condiciones, Float precio, String imagenUrl, Float calificacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.condiciones = condiciones;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
        this.calificacion = calificacion;
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
    
    
    
}
