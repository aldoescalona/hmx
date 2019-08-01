/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.model.adm;

import mx.historicos.api.bean.Cliente;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Operador;

/**
 *
 * @author 43700118
 */
public class RegistroForm {
    
    private Cliente cliente;
    private Lugar lugar;
    private Operador operador;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public Operador getOperador() {
        return operador;
    }

    public void setOperador(Operador operador) {
        this.operador = operador;
    }
    
}
