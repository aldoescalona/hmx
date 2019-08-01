package mx.historicos.api.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.Segmento;
import mx.historicos.api.bean.UsuarioPuntos;
import mx.historicos.api.conf.HibernateUtil;
import mx.historicos.api.v1.model.CargoPuntos;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
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
public class UsuarioPuntosFacade extends AbstractFacade<UsuarioPuntos> {

    public UsuarioPuntosFacade() {
        super(UsuarioPuntos.class);
    }

    public UsuarioPuntos getPuntos(Lugar lugar, Long usuarioId) {
        UsuarioPuntos ent = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(UsuarioPuntos.class);
            criteria.add(Restrictions.eq("usuarioId.id", usuarioId));
            criteria.add(Restrictions.eq("lugarId", lugar));
            criteria.setMaxResults(1);
            ent = (UsuarioPuntos) criteria.uniqueResult();

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

    public List<CargoPuntos> getPuntajesConevenios(Lugar establecimiento, Long usuarioId) {
        List<CargoPuntos> cargos = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            SQLQuery q = session.createSQLQuery("SELECT e.id as id, e.nombre as nombre, (u.puntos - u.retenidos) as puntos, c.tasa as tasa, IF(:propietarioId = usuarioId, 0, 1) as pertenencia FROM usuariopuntos u\n"
                    + "inner join convenio c on c.relacionadoId = u.lugarId\n"
                    + "inner join lugar e on e.id = u.lugarId\n"
                    + "where c.lugarId = :eid and u.usuarioId = :uid and e.estado = 1\n"
                    + "order by tasa desc, pertenencia asc, u.puntos desc limit 10");

            q.setLong("eid", establecimiento.getId());
            q.setLong("uid", usuarioId);
            q.setLong("propietarioId", establecimiento.getPropietarioId().getId());
            q.addScalar("id", LongType.INSTANCE);
            q.addScalar("nombre", StringType.INSTANCE);
            q.addScalar("puntos", IntegerType.INSTANCE);
            q.addScalar("tasa", FloatType.INSTANCE);
            // q.addScalar("cargo", IntegerType.INSTANCE);
            q.setResultTransformer(Transformers.aliasToBean(CargoPuntos.class));
            cargos = q.list();

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

        return cargos;

    }


}
