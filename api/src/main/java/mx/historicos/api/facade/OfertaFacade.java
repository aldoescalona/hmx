package mx.historicos.api.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Oferta;
import mx.historicos.api.conf.HibernateUtil;
import mx.historicos.api.enu.LugarEnumerated;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
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
public class OfertaFacade extends AbstractFacade<Oferta> {

    public OfertaFacade() {
        super(Oferta.class);
    }

    public List<Oferta> getOfertas(Lugar lugar, Boolean estado) {
        List<Oferta> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Oferta.class);
            criteria.add(Restrictions.eq("lugarId", lugar));
            if (estado != null) {
                criteria.add(Restrictions.eq("estado", estado));
            }
            criteria.add(Restrictions.eq("baja", Boolean.FALSE));
            criteria.setFetchMode("lugarId", FetchMode.JOIN);
            criteria.setFetchMode("lugarId.imagenLogoId", FetchMode.JOIN);
            criteria.setFetchMode("imagenId", FetchMode.JOIN);
            criteria.addOrder(Order.asc("orden"));
            
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
    
    public List<Oferta> getOfertasApp(Lugar lugar) {
        List<Oferta> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Oferta.class);
            criteria.add(Restrictions.eq("lugarId", lugar));
            criteria.add(Restrictions.eq("estado", Boolean.TRUE));
            criteria.add(Restrictions.eq("baja", Boolean.FALSE));
            criteria.setFetchMode("imagenId", FetchMode.JOIN);
            criteria.addOrder(Order.asc("orden"));
            
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
