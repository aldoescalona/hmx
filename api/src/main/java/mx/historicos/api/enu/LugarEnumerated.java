/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.enu;

/**
 *
 * @author Aldo
 */
public class LugarEnumerated {

        
    public static enum ESTADO_LGR {

        PENDIENTE("Pendiente"), ACTIVO("Activo"), INACTIVO("Inactivo"), SUSPENDIDO("Suspendido");

        private ESTADO_LGR(String title) {
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
    
    public static enum LUGAR_TIPO {

        ADMINISTRADO("Administrado"), NO_ADMINISTRADO("No Administrado");

        private LUGAR_TIPO(String title) {
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
