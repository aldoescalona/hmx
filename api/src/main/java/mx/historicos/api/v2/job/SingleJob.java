/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.job;


import java.util.Date;
import java.util.List;
import mx.historicos.api.bean.Visita;
import mx.historicos.api.enu.VisitaEnumerated;
import mx.historicos.api.facade.VisitaFacade;

/**
 *
 * @author Aldo
 */
public class SingleJob extends VisitaFacade {

    private boolean busy = false;
    private static SingleJob instance = new SingleJob();

    private SingleJob() {
        super();
    }

    public static SingleJob getInstance() {
        return instance;
    }

    public void run() {

        if (busy) {
            System.out.println(" JOB OCUPADO ");
            return;
        }
        try {
            busy = true;
            
            List<Visita> list = this.getVisitasPendientesJob();

            for (Visita visita : list) {

                visita = this.get(visita.getId());
                if (!visita.getEstado().equals(VisitaEnumerated.ESTADO.PENDIENTE)) {
                    continue;
                }

                visita.setFechaRespuesta(new Date());
                Visita test = this.validaExiste(visita);
                if (test == null) {
                    visita.setEstado(VisitaEnumerated.ESTADO.ACEPTADO);
                    visita.setRespuesta(" -- OK --");
                } else {
                    visita.setEstado(VisitaEnumerated.ESTADO.RECHAZADA);
                    visita.setRespuesta(" -- Ticket ya existe -- ");
                }

                this.edit(visita);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            busy = false;
        }
    }

}
