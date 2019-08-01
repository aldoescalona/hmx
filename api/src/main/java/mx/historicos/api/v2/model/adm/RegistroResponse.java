/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.model.adm;

/**
 *
 * @author 43700118
 */
public class RegistroResponse {

    private Boolean ok;
    private Integer codigo;
    private String mensaje;
    
    private Long clienteId;
    private Long lugarId;
    private Long operadorId;
    
    private String alias;
    private String clienteUsername;
    private String clienteContrasena;
    
    private String operadorUsername;
    private String operadorContrasena;

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

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getLugarId() {
        return lugarId;
    }

    public void setLugarId(Long lugarId) {
        this.lugarId = lugarId;
    }

    public Long getOperadorId() {
        return operadorId;
    }

    public void setOperadorId(Long operadorId) {
        this.operadorId = operadorId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getClienteUsername() {
        return clienteUsername;
    }

    public void setClienteUsername(String clienteUsername) {
        this.clienteUsername = clienteUsername;
    }

    public String getClienteContrasena() {
        return clienteContrasena;
    }

    public void setClienteContrasena(String clienteContrasena) {
        this.clienteContrasena = clienteContrasena;
    }

    public String getOperadorUsername() {
        return operadorUsername;
    }

    public void setOperadorUsername(String operadorUsername) {
        this.operadorUsername = operadorUsername;
    }

    public String getOperadorContrasena() {
        return operadorContrasena;
    }

    public void setOperadorContrasena(String operadorContrasena) {
        this.operadorContrasena = operadorContrasena;
    }
    

       

}
