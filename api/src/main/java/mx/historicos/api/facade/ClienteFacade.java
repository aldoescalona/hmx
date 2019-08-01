package mx.historicos.api.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.historicos.api.bean.Cliente;
import mx.historicos.api.bean.Operador;
import mx.historicos.api.conf.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
public class ClienteFacade extends AbstractFacade<Cliente> {

    public ClienteFacade() {
        super(Cliente.class);
    }

    public Cliente getCliente(String user, String pass) {

        Cliente ent = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Cliente.class);
            criteria.add(Restrictions.eq("correo", user));
            criteria.add(Restrictions.sqlRestriction("{alias}.passsword = password(?)", pass, StringType.INSTANCE));

            ent = (Cliente) criteria.uniqueResult();

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

        return ent;
    }

    public String cifraPasssword(String passs) {

        String pcif = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            SQLQuery query = session.createSQLQuery("SELECT password(:p)");
            query.setString("p", passs);
            pcif = (String) query.uniqueResult();

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
        return pcif;
    }

}
