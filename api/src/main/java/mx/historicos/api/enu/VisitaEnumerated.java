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
public class VisitaEnumerated {

    public static enum ESTADO {

        PENDIENTE("Pendiente", ""), ACEPTADO("Aceptada", "Aceptar"), ACEPTADO_AUTOMATICO("Aceptada", ""), RECHAZADA("Rechazada", "Rechazar");

        private ESTADO(String title, String actionTitle) {
            this.title = title;
            this.actionTitle = actionTitle;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getActionTitle() {
            return actionTitle;
        }

        public void setActionTitle(String actionTitle) {
            this.actionTitle = actionTitle;
        }

        @Override
        public String toString() {
            return title;
        }
        private String title;
        private String actionTitle;
    }
}
