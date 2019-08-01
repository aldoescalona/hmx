
package mx.historicos.api.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.historicos.api.bean.Evento;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Oferta;
import mx.historicos.api.conf.HibernateUtil;
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
public class EventoFacade extends AbstractFacade<Evento> {

    
     public EventoFacade() {
        super(Evento.class);
    }

    public List<Evento> getCartelera(Long usrId) {
        List<Evento> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Evento.class);
            criteria.setFetchMode("imagenId", FetchMode.JOIN);
            criteria.setFetchMode("lugarId", FetchMode.JOIN);
            criteria.setFetchMode("lugarId.imagenLogoId", FetchMode.JOIN);
            criteria.setMaxResults(25);
            criteria.addOrder(Order.desc("fechaInicio"));
            
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
    
    
    public List<Evento> getEventos(Lugar lugar, Boolean estado) {
        List<Evento> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Evento.class);
            criteria.add(Restrictions.eq("lugarId", lugar));
            if (estado != null) {
                criteria.add(Restrictions.eq("estado", estado));
            }
            // criteria.setFetchMode("lugarId", FetchMode.JOIN);
            // criteria.setFetchMode("lugarId.imagenLogoId", FetchMode.JOIN);
            criteria.setFetchMode("imagenId", FetchMode.JOIN);
            criteria.addOrder(Order.asc("fechaInicio"));
            
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
