/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import mx.historicos.api.enu.OfertaEnumerated.OFERTA_TIPO;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author 43700118
 */
@Entity
@Table(name = "oferta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Oferta.findAll", query = "SELECT o FROM Oferta o")})
public class Oferta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "mx.historicos.api.conf.Generator", parameters = {
        @org.hibernate.annotations.Parameter(name = "entityId", value = "oferta_id_seq")})
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo")
    @Enumerated(value = EnumType.ORDINAL)
    private OFERTA_TIPO tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 150)
    @Column(name = "condiciones")
    private String condiciones;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio")
    private Integer precio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orden")
    private Integer orden;
    @Column(name = "calificacion")
    private BigDecimal calificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "calificaciones")
    private int calificaciones;
    @Basic(optional = false)
    @NotNull
    @Column(name = "calificacion5")
    private int calificacion5;
    @Basic(optional = false)
    @NotNull
    @Column(name = "calificacion4")
    private int calificacion4;
    @Basic(optional = false)
    @NotNull
    @Column(name = "calificacion3")
    private int calificacion3;
    @Basic(optional = false)
    @NotNull
    @Column(name = "calificacion2")
    private int calificacion2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "calificacion1")
    private int calificacion1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vendidos")
    private int vendidos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private Boolean estado;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "baja")
    private Boolean baja;
    
    @JoinColumn(name = "lugarId", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Lugar lugarId;
    @JoinColumn(name = "imagenId", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Imagen imagenId;

    public Oferta() {
    }

    public Oferta(Long id) {
        this.id = id;
    }

   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OFERTA_TIPO getTipo() {
        return tipo;
    }

    public void setTipo(OFERTA_TIPO tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public BigDecimal getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(BigDecimal calificacion) {
        this.calificacion = calificacion;
    }

    public int getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(int calificaciones) {
        this.calificaciones = calificaciones;
    }

    public int getCalificacion5() {
        return calificacion5;
    }

    public void setCalificacion5(int calificacion5) {
        this.calificacion5 = calificacion5;
    }

    public int getCalificacion4() {
        return calificacion4;
    }

    public void setCalificacion4(int calificacion4) {
        this.calificacion4 = calificacion4;
    }

    public int getCalificacion3() {
        return calificacion3;
    }

    public void setCalificacion3(int calificacion3) {
        this.calificacion3 = calificacion3;
    }

    public int getCalificacion2() {
        return calificacion2;
    }

    public void setCalificacion2(int calificacion2) {
        this.calificacion2 = calificacion2;
    }

    public int getCalificacion1() {
        return calificacion1;
    }

    public void setCalificacion1(int calificacion1) {
        this.calificacion1 = calificacion1;
    }

    public int getVendidos() {
        return vendidos;
    }

    public void setVendidos(int vendidos) {
        this.vendidos = vendidos;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getBaja() {
        return baja;
    }

    public void setBaja(Boolean baja) {
        this.baja = baja;
    }

    public Lugar getLugarId() {
        return lugarId;
    }

    public void setLugarId(Lugar lugarId) {
        this.lugarId = lugarId;
    }

    public Imagen getImagenId() {
        return imagenId;
    }

    public void setImagenId(Imagen imagenId) {
        this.imagenId = imagenId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Oferta)) {
            return false;
        }
        Oferta other = (Oferta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.historicos.api.bean.Oferta[ id=" + id + " ]";
    }
    
}
