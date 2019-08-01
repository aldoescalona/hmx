/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.model.usr;

import java.util.List;
import mx.historicos.api.bean.Compra;
import mx.historicos.api.bean.PagoCompra;

/**
 *
 * @author 43700118
 */
public class ComprarOfertaResponse {
    
    private Boolean ok;
    private Integer codigo;
    private String mensaje;
    
    private Compra compra;
    private List<PagoCompra> pagos;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public List<PagoCompra> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagoCompra> pagos) {
        this.pagos = pagos;
    }
    
}
