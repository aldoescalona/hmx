/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.conf;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import mx.historicos.api.facade.ParametroFacade;

/**
 *
 * @author 43700118
 */
@Singleton
public class Globales {

   private String filePath;

    @EJB
    private ParametroFacade parametroFacade;

    @PostConstruct
    public void init() {
        try {

            filePath = parametroFacade.get("file.url").getValor();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilePath() {
        return filePath;
    }

}
