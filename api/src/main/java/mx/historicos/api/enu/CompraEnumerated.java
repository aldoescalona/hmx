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
public class CompraEnumerated {

    public static enum ESTADO {

        PENDIENTE("Pendiente", true), CONFIRMADA("Confirmada", "Confirmar"), RECHAZADA("Rechazada", "Rechazar"), RECHAZADA_AUTOMATICO("Rechazada", "");

        private ESTADO(String title) {
            this.title = title;

        }

        private ESTADO(String title, Boolean esperaRespuesta) {
            this.title = title;
            this.esperaRespuesta = esperaRespuesta;
        }

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

        public Boolean getEsperaRespuesta() {
            return esperaRespuesta;
        }

        public void setEsperaRespuesta(Boolean esperaRespuesta) {
            this.esperaRespuesta = esperaRespuesta;
        }
        
        @Override
        public String toString() {
            return title;
        }
        private String title;
        private String actionTitle;
        private Boolean esperaRespuesta = false;
    }
}
