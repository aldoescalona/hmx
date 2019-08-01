
package mx.historicos.api.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.historicos.api.bean.Admin;
import mx.historicos.api.bean.Cliente;
import mx.historicos.api.bean.Compra;
import mx.historicos.api.bean.Lugar;
import mx.historicos.api.bean.PagoCompra;
import mx.historicos.api.bean.Segmento;
import mx.historicos.api.conf.HibernateUtil;
import mx.historicos.api.enu.LugarEnumerated;
import mx.historicos.api.v1.model.CargoPuntos;
import mx.historicos.api.v2.model.usr.LugarItem;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
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
public class LugarFacade extends AbstractFacade<Lugar> {

     public LugarFacade() {
        super(Lugar.class);
    }

    public List<Lugar> getLugares(Long usrId, Long giroId) {
        List<Lugar> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Lugar.class);
            criteria.add(Restrictions.eq("segmentoId.id", giroId));
            // criteria.add(Restrictions.eq("ciudadId", giroId));
            criteria.add(Restrictions.eq("estado", LugarEnumerated.ESTADO_LGR.ACTIVO));
            criteria.setFetchMode("imagenLogoId", FetchMode.JOIN);
            criteria.add(Restrictions.eq("tipo", LugarEnumerated.LUGAR_TIPO.ADMINISTRADO));

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
    
    
    public List<Lugar> getLugares(Cliente cliente) {
        List<Lugar> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Lugar.class);
            criteria.add(Restrictions.eq("propietarioId", cliente));
            criteria.add(Restrictions.eq("estado", LugarEnumerated.ESTADO_LGR.ACTIVO));
            criteria.setFetchMode("imagenLogoId", FetchMode.JOIN);

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
    
    public List<Lugar> getLugaresAdministrados(Admin admin) {
        List<Lugar> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Lugar.class);
            // criteria.add(Restrictions.eq("propietarioId", cliente));
            criteria.setFetchMode("propietarioId", FetchMode.JOIN);
            criteria.add(Restrictions.eq("tipo", LugarEnumerated.LUGAR_TIPO.ADMINISTRADO));
            // criteria.add(Restrictions.eq("estado", LugarEnumerated.ESTADO_LGR.ACTIVO));
            criteria.setFetchMode("imagenLogoId", FetchMode.JOIN);
            

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
    
    public List<Lugar> getLugaresPublico() {
        List<Lugar> list = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Lugar.class);
            
            criteria.add(Restrictions.eq("tipo", LugarEnumerated.LUGAR_TIPO.NO_ADMINISTRADO));
            // criteria.add(Restrictions.eq("estado", LugarEnumerated.ESTADO_LGR.ACTIVO));
            criteria.setFetchMode("imagenLogoId", FetchMode.JOIN);

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
    
    public void compra(Compra compra, List<CargoPuntos> cargos) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            SQLQuery query = session.createSQLQuery("SELECT NextCompra(:estoId)");
            query.setLong("estoId", compra.getLugarId().getId());
            Number num = (Number) query.uniqueResult();
            compra.setFolio(num.intValue());

            session.save(compra);

            for (CargoPuntos cargo : cargos) {
                PagoCompra pc = new PagoCompra();
                pc.setOrigenId(new Lugar(cargo.getId()));
                pc.setPuntos(cargo.getCargo());
                pc.setCompraId(compra);
                session.save(pc);
            }

            /*HistoriaPuntos puntos = new HistoriaPuntos();
            puntos.setUsuarioId(usuario);
            puntos.setEstablecimientoId(visita.getEstablecimientoId());
            puntos.setTipo(PuntoslEnumerated.TIPO.ACUMULA);
            puntos.setPuntos(visita.getMonto().intValue());
            puntos.setFecha(new Date());
            session.save(puntos);*/
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
    
    public void comprar(Compra compra, List<PagoCompra> cargos) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            SQLQuery query = session.createSQLQuery("SELECT NextCompra(:estoId)");
            query.setLong("estoId", compra.getLugarId().getId());
            Number num = (Number) query.uniqueResult();
            compra.setFolio(num.intValue());

            session.save(compra);

            for (PagoCompra cargo : cargos) {
                cargo.setCompraId(compra);
                session.save(cargo);
            }

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
    
    
    public List<LugarItem> getLugaresItems(Long segmentoId, Long usuarioId) {
        List<LugarItem> cargos = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();

            SQLQuery q = session.createSQLQuery("SELECT l.id, l.nombre, l.direccion, l.calificacion, l.calificaciones, up.puntos - up.retenidos as puntos, i.imgC as img FROM lugar l\n"
                    + "left join imagen i on i.id = l.imagenLogoId\n"
                    + "left join usuariopuntos up on l.id = up.lugarId and up.usuarioId = :usrId where segmentoId = :segId and l.ciudadId = 150106 and estado = 1 order by l.orden");

            q.setLong("usrId", usuarioId);
            q.setLong("segId", segmentoId);
            q.addScalar("id", LongType.INSTANCE);
            q.addScalar("nombre", StringType.INSTANCE);
            q.addScalar("direccion", StringType.INSTANCE);
            q.addScalar("calificacion", BigDecimalType.INSTANCE);
            q.addScalar("calificaciones", IntegerType.INSTANCE);
            q.addScalar("puntos", IntegerType.INSTANCE);
            q.addScalar("img", StringType.INSTANCE);
            q.setResultTransformer(Transformers.aliasToBean(LugarItem.class));
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
