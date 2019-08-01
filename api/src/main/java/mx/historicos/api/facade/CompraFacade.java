package mx.historicos.api.facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.historicos.api.bean.Compra;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Parametro;
import mx.historicos.api.conf.HibernateUtil;
import mx.historicos.api.v2.model.Busqueda;
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
public class CompraFacade extends AbstractFacade<Compra> {

    public CompraFacade() {
        super(Compra.class);
    }

    public List<Compra> getComprasUsuario(Long usuarioId) {
        List<Compra> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Compra.class);
            criteria.add(Restrictions.eq("usuarioId.id", usuarioId));
            criteria.setFetchMode("lugarId", FetchMode.JOIN);
            criteria.setFetchMode("lugarId.imagenLogoId", FetchMode.JOIN);
            criteria.setFetchMode("ofertaId", FetchMode.JOIN);
            criteria.setFetchMode("ofertaId.imagenId", FetchMode.JOIN);
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

    public List<Compra> getComprasDashboard(Lugar lugar) {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -24);

        List<Compra> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Compra.class);
            criteria.add(Restrictions.eq("lugarId", lugar));
            criteria.setFetchMode("ofertaId", FetchMode.JOIN);
            criteria.setFetchMode("ofertaId.imagenId", FetchMode.JOIN);
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

    public List<Compra> getBuscaCompras(Lugar lugar, Busqueda busqueda) {
        Integer folio;
        try {
            folio = Integer.valueOf(busqueda.getTermino());
        } catch (Exception e) {
            return new ArrayList();
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -24);

        List<Compra> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Compra.class);
            criteria.add(Restrictions.eq("lugarId", lugar));
            criteria.add(Restrictions.eq("folio", folio));
            criteria.setFetchMode("ofertaId", FetchMode.JOIN);
            criteria.setFetchMode("ofertaId.imagenId", FetchMode.JOIN);
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

}
