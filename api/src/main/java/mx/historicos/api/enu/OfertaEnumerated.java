/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.enu;

/**
 *
 * @author 43700118
 */
public class OfertaEnumerated {
    
    public static enum OFERTA_TIPO {

        PRODUCTO("Producto"), SERVICIO("Servicio"), EXPERIENCIA("Experiencia");

        private OFERTA_TIPO(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
        private String title;
    }
    
}
