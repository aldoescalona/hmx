package mx.historicos.api.facade;

import javax.ejb.Stateless;
import mx.historicos.api.bean.Admin;
import mx.historicos.api.conf.HibernateUtil;
import org.hibernate.Criteria;
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
public class AdminFacade extends AbstractFacade<Admin> {

    public AdminFacade() {
        super(Admin.class);
    }

    public Admin getAdmin(String user, String pass) {

        Admin usr = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Admin.class);
            criteria.add(Restrictions.eq("correo", user));
            criteria.add(Restrictions.sqlRestriction("{alias}.contrasena = password(?)", pass, StringType.INSTANCE));

            usr = (Admin) criteria.uniqueResult();

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

}
