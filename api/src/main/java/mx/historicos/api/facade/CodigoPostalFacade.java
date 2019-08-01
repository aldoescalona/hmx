
package mx.historicos.api.facade;

import java.util.List;
import javax.ejb.Stateless;
import mx.historicos.api.bean.CodigoPostal;
import mx.historicos.api.conf.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

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
public class CodigoPostalFacade extends AbstractFacade<CodigoPostal> {

     public CodigoPostalFacade() {
        super(CodigoPostal.class);
    }
     
     public List<CodigoPostal> getColonias(String code) {
        List<CodigoPostal> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(CodigoPostal.class);
            criteria.add(Restrictions.eq("codigo", code));
            criteria.setFetchMode("estadoId", FetchMode.JOIN);
            criteria.setFetchMode("ciudadId", FetchMode.JOIN);
            criteria.setFetchMode("tipoAsentamientoId", FetchMode.JOIN);

            list = criteria.list();

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

        return list;
    }
     
}
