
package mx.historicos.api.facade;

import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Visita;
import mx.historicos.api.conf.HibernateUtil;
import mx.historicos.api.enu.VisitaEnumerated;
import mx.historicos.api.v2.model.Busqueda;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
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
public class VisitaFacade extends AbstractFacade<Visita> {

     public VisitaFacade() {
        super(Visita.class);
    }

    public List<Visita> getVisitasUsuario(Long usuarioId) {
        List<Visita> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Visita.class);
            criteria.add(Restrictions.eq("usuarioId.id", usuarioId));
            criteria.setFetchMode("lugarId", FetchMode.JOIN);
            criteria.setFetchMode("lugarId.imagenLogoId", FetchMode.JOIN);
            criteria.addOrder(Order.desc("fecha"));
            criteria.setMaxResults(20);
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
    
    
    public List<Visita> getVisitasDashboard(Lugar lugar) {
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -24);
        
        List<Visita> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Visita.class);
            criteria.add(Restrictions.eq("lugarId", lugar));
            criteria.setFetchMode("usuarioId", FetchMode.JOIN);
            criteria.addOrder(Order.desc("fecha"));
            criteria.add(Restrictions.ge("fecha", cal.getTime()));
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
    
    public List<Visita> getBuscaVisitas(Lugar lugar, Busqueda bus) {
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -24);
        
        List<Visita> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Visita.class);
            criteria.add(Restrictions.eq("lugarId", lugar));
            criteria.add(Restrictions.like("ticket", bus.getTermino(), MatchMode.ANYWHERE));
            criteria.setFetchMode("usuarioId", FetchMode.JOIN);
            criteria.addOrder(Order.desc("fecha"));
            criteria.add(Restrictions.ge("fecha", cal.getTime()));
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
    
    public List<Visita> getVisitasPendientes(Lugar lugar) {
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -24);
        
        List<Visita> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Visita.class);
            criteria.add(Restrictions.eq("lugarId", lugar));
            criteria.add(Restrictions.eq("estado", VisitaEnumerated.ESTADO.PENDIENTE));
            criteria.setFetchMode("usuarioId", FetchMode.JOIN);
            criteria.addOrder(Order.desc("fecha"));
            criteria.add(Restrictions.ge("fecha", cal.getTime()));
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
    
    public List<Visita> getVisitasPendientesJob() {
        List<Visita> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -1435);
            
            Criteria criteria = session.createCriteria(Visita.class);
            criteria.add(Restrictions.eq("estado", VisitaEnumerated.ESTADO.PENDIENTE));
            criteria.add(Restrictions.lt("fecha", cal.getTime()));
            criteria.setMaxResults(100);
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
    
    public Visita validaExiste(Visita visita) {
        Visita old = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -30);
            
            Criteria criteria = session.createCriteria(Visita.class);
            criteria.add(Restrictions.eq("ticket", visita.getTicket()));
            criteria.add(Restrictions.eq("lugarId", visita.getLugarId()));
            criteria.add(Restrictions.eq("estado", VisitaEnumerated.ESTADO.ACEPTADO));
            criteria.add(Restrictions.gt("fecha", cal.getTime()));
            criteria.setMaxResults(1);
            old = (Visita) criteria.uniqueResult();
            
            session.update(visita);

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
        return old;
    }
}
