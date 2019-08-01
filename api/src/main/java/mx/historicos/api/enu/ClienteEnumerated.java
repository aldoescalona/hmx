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
public class ClienteEnumerated {

    public static enum ESTADO_CTE {

        EN_PROCESO("En proceso"), ACTIVO("Activo"), INACTIVO("Inactivo"), SUSPENDIDO("Suspendido");

        private ESTADO_CTE(String title) {
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
