
package mx.historicos.api.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.historicos.api.bean.Parametro;
import mx.historicos.api.bean.Segmento;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 43700118
 */
@Stateless
public class SegmentoFacade extends AbstractFacade<Segmento> {

     public SegmentoFacade() {
        super(Segmento.class);
    }
     
}
