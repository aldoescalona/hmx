/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.util;

import mx.historicos.api.bean.Imagen;

/**
 *
 * @author 43700118
 */
public abstract class AbstractImagenIO<E> {
    
    private E entidad;
    

    public AbstractImagenIO(E entidad) {
        this.entidad = entidad;
    }

    public E getEntidad() {
        return entidad;
    }
    
    public abstract Imagen getImagen();
    public abstract void setImagen(Imagen imagen);
    
    public boolean existe(){
        return getImagen() != null;
    }
}
