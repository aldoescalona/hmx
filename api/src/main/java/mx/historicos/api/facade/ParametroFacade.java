
package mx.historicos.api.facade;

import javax.ejb.Stateless;
import mx.historicos.api.bean.Parametro;

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
public class ParametroFacade extends AbstractFacade<Parametro> {

     public ParametroFacade() {
        super(Parametro.class);
    }

}
