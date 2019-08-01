package mx.historicos.api.facade;

import java.io.File;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import mx.historicos.api.bean.Imagen;
import mx.historicos.api.conf.Globales;
import mx.historicos.api.conf.HibernateUtil;
import mx.historicos.api.util.AbstractImagenIO;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
public class ImagenFacade extends AbstractFacade<Imagen> {

    @EJB
    private Globales globales;

    public ImagenFacade() {
        super(Imagen.class);
    }

    public void create(Imagen imagen, AbstractImagenIO imagenio, File dir) {
        Imagen old = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            session.save(imagen);

            old = imagenio.getImagen();
            imagenio.setImagen(imagen);
            session.update(imagenio.getEntidad());

            session.flush();
            tx.commit();
        } catch (RuntimeException ex) {
            if (session != null && tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        if (old != null) {
            try {

                File fa = new File(dir, old.getImgA());
                File fb = new File(dir, old.getImgB());
                File fc = new File(dir, old.getImgC());

                fa.delete();
                fb.delete();
                fc.delete();

                remove(old);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void eliminarConArchivos(Imagen imagen) {
        File dir = new File(globales.getFilePath());

        if (imagen != null) {
            try {

                File fa = new File(dir, imagen.getImgA());
                File fb = new File(dir, imagen.getImgB());
                File fc = new File(dir, imagen.getImgC());

                fa.delete();
                fb.delete();
                fc.delete();

                remove(imagen);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
