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
public class UsuarioEnumerated {

    public static enum GENERO {

        MUJER("Mujer"), HOMBRE("Hombre"), OTRO("Otro");

        private GENERO(String title) {
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
    
    public static enum ESTADO {

        INACTIVO("Inactivo"), ACTIVO("Activo"), SUSPENDIDO("Suspendido");

        private ESTADO(String title) {
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
