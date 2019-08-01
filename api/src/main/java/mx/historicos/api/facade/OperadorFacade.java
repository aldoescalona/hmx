package mx.historicos.api.facade;

import java.util.List;
import javax.ejb.Stateless;
import mx.historicos.api.bean.Evento;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Operador;
import mx.historicos.api.bean.Usuario;
import mx.historicos.api.conf.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;

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
public class OperadorFacade extends AbstractFacade<Operador> {

    public OperadorFacade() {
        super(Operador.class);
    }

    public void crear(Operador operador) {

        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            if (!StringUtils.isEmpty(operador.getContrasena())) {
                SQLQuery query = session.createSQLQuery("SELECT password(:p)");
                query.setString("p", operador.getContrasena());
                String pcif = (String) query.uniqueResult();
                operador.setContrasena(pcif);
            }
            session.save(operador);

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
    }

    public void cambiarPass(Operador operador) {

        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            SQLQuery query = session.createSQLQuery("SELECT password(:p)");
            query.setString("p", operador.getContrasena());
            String pcif = (String) query.uniqueResult();
            
            System.out.println("PCIF: " + pcif);
            operador.setContrasena(pcif);
            session.update(operador);

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
    }

    public Operador getOperador(String user, String pass, String alias) {

        Operador usr = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Operador.class);
            criteria.add(Restrictions.eq("clave", user));
            criteria.add(Restrictions.sqlRestriction("{alias}.contrasena = password(?)", pass, StringType.INSTANCE));
            criteria.createAlias("lugarId", "lugar");
            criteria.add(Restrictions.eq("lugar.alias", alias));
            // criteria.setFetchMode("lugarId", FetchMode.JOIN);

            usr = (Operador) criteria.uniqueResult();

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

        return usr;
    }

    public List<Operador> getOperadores(Lugar lugar, Boolean estado) {
        List<Operador> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Operador.class);
            criteria.add(Restrictions.eq("lugarId", lugar));
            if (estado != null) {
                criteria.add(Restrictions.eq("estado", estado));
            }
            // criteria.setFetchMode("lugarId", FetchMode.JOIN);
            // criteria.setFetchMode("lugarId.imagenLogoId", FetchMode.JOIN);
            criteria.addOrder(Order.asc("nombre"));

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
