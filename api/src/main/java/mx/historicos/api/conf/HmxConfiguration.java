/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.conf;


import mx.historicos.api.bean.Admin;
import mx.historicos.api.bean.Ciudad;
import mx.historicos.api.bean.Click;
import mx.historicos.api.bean.Cliente;
import mx.historicos.api.bean.CodigoPostal;
import mx.historicos.api.bean.Compra;
import mx.historicos.api.bean.EntidadFederativa;
import mx.historicos.api.bean.Evento;
import mx.historicos.api.bean.HistoriaPuntos;
import mx.historicos.api.bean.Imagen;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Oferta;
import mx.historicos.api.bean.Operador;
import mx.historicos.api.bean.PagoCompra;
import mx.historicos.api.bean.Parametro;
import mx.historicos.api.bean.Segmento;
import mx.historicos.api.bean.TipoAsentamiento;
import mx.historicos.api.bean.Usuario;
import mx.historicos.api.bean.UsuarioPuntos;
import mx.historicos.api.bean.Visita;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author 43700118
 */
public class HmxConfiguration extends Configuration {


    public HmxConfiguration() {
        super();    
        init();
    }

    private void init() {

        this.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        this.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        this.setProperty("hibernate.connection.datasource", "jdbc/hmx");
        
        this.addAnnotatedClass(Admin.class);
        this.addAnnotatedClass(Ciudad.class);
        this.addAnnotatedClass(Click.class);
        this.addAnnotatedClass(Cliente.class);
        this.addAnnotatedClass(CodigoPostal.class);
        this.addAnnotatedClass(Compra.class);
        this.addAnnotatedClass(EntidadFederativa.class);
        this.addAnnotatedClass(Evento.class);
        this.addAnnotatedClass(HistoriaPuntos.class);
        this.addAnnotatedClass(Imagen.class);
        this.addAnnotatedClass(Lugar.class);
        this.addAnnotatedClass(Oferta.class);
        this.addAnnotatedClass(Operador.class);
        this.addAnnotatedClass(PagoCompra.class);
        this.addAnnotatedClass(Segmento.class);
        this.addAnnotatedClass(TipoAsentamiento.class);
        this.addAnnotatedClass(Usuario.class);
        this.addAnnotatedClass(UsuarioPuntos.class);
        this.addAnnotatedClass(Visita.class);
        this.addAnnotatedClass(Parametro.class);
        
    }
}
