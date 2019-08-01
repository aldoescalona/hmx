
package mx.historicos.api.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.historicos.api.bean.Usuario;
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
public class UsuarioFacade extends AbstractFacade<Usuario> {

     public UsuarioFacade() {
        super(Usuario.class);
    }

    public Usuario getUsuario(String user, String pass) {

        Usuario usr = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Usuario.class);
            criteria.add(Restrictions.eq("email", user));
            criteria.add(Restrictions.sqlRestriction("{alias}.contrasena = password(?)", pass, StringType.INSTANCE));
            
            usr = (Usuario) criteria.uniqueResult();

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

    
    public void crear(Usuario usuario) {

        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            
            if(!StringUtils.isEmpty(usuario.getContrasena())){
                SQLQuery query = session.createSQLQuery("SELECT password(:p)");
                query.setString("p", usuario.getContrasena());
                String pcif = (String)query.uniqueResult();
                usuario.setContrasena(pcif);
            }
            session.save(usuario);

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
    
    public Usuario getUsuarioCorreo(String email) {

        Usuario usr = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Usuario.class);

            criteria.add(Restrictions.eq("email", email));
            criteria.setMaxResults(1);
            usr = (Usuario) criteria.uniqueResult();

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
    
    public Usuario getUsuarioFacebookId(String facebookuser) {

        Usuario usr = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Usuario.class);

            criteria.add(Restrictions.eq("facebookid", facebookuser));
            criteria.setMaxResults(1);
            usr = (Usuario) criteria.uniqueResult();

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
