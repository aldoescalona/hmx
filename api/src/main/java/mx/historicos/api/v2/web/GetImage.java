/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.historicos.api.conf.Globales;

/**
 *
 * @author 43700118
 */
@WebServlet(name = "GetImage", urlPatterns = {"/GetImage/*", "/imgStream/*"})
public class GetImage extends HttpServlet {
    
    @EJB
    private Globales globales;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("image/png");
        File f = new File(globales.getFilePath(), request.getPathInfo());
        
        System.out.println("FILE: " + f.getAbsolutePath());

        BufferedImage bi = ImageIO.read(f);
        OutputStream os = response.getOutputStream();
        ImageIO.write(bi, "png", os);
    }
}
