/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import mx.historicos.api.enu.LugarEnumerated;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author 43700118
 */
@Entity
@Table(name = "lugar")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lugar.findAll", query = "SELECT l FROM Lugar l")})
public class Lugar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "mx.historicos.api.conf.Generator", parameters = {
        @org.hibernate.annotations.Parameter(name = "entityId", value = "lugar_id_seq")})
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo")
    @Enumerated(EnumType.ORDINAL)
    private LugarEnumerated.LUGAR_TIPO tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "alias")
    private String alias;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "direccion")
    private String direccion;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
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
    @Size(min = 1, max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 18)
    @Column(name = "telefono")
    private String telefono;
    @Size(max = 140)
    @Column(name = "sitioWeb")
    private String sitioWeb;
    @Size(max = 50)
    @Column(name = "correo")
    private String correo;
    @Size(max = 180)
    @Column(name = "ubicacionUrl")
    private String ubicacionUrl;
    @Size(max = 25)
    @Column(name = "ubicacionLng")
    private String ubicacionLng;
    @Size(max = 25)
    @Column(name = "ubicacionLtd")
    private String ubicacionLtd;
    @Column(name = "precioMin")
    private BigDecimal precioMin;
    @Column(name = "precioMax")
    private BigDecimal precioMax;
    @Column(name = "puntosGenerados")
    private BigDecimal puntosGenerados;
    @Column(name = "puntosCanjeados")
    private BigDecimal puntosCanjeados;
    @Column(name = "puntosCanjeadosOtros")
    private BigDecimal puntosCanjeadosOtros;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orden")
    private int orden;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    @Enumerated(EnumType.ORDINAL)
    private LugarEnumerated.ESTADO_LGR estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lugarId", fetch = FetchType.LAZY)
    private List<Evento> eventoList;
    
    @JoinColumn(name = "ciudadId", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Ciudad ciudadId;
    @JoinColumn(name = "codigoPostalId", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CodigoPostal codigoPostalId;
    
    @JoinColumn(name = "segmentoId", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Segmento segmentoId;
    @JoinColumn(name = "imagenPortadaId", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Imagen imagenPortadaId;
    @JoinColumn(name = "imagenLogoId", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Imagen imagenLogoId;
    @JoinColumn(name = "propietarioId", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente propietarioId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lugarId", fetch = FetchType.LAZY)
    private List<Oferta> ofertaList;

    public Lugar() {
    }

    public Lugar(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LugarEnumerated.LUGAR_TIPO getTipo() {
        return tipo;
    }

    public void setTipo(LugarEnumerated.LUGAR_TIPO tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUbicacionUrl() {
        return ubicacionUrl;
    }

    public void setUbicacionUrl(String ubicacionUrl) {
        this.ubicacionUrl = ubicacionUrl;
    }

    public String getUbicacionLng() {
        return ubicacionLng;
    }

    public void setUbicacionLng(String ubicacionLng) {
        this.ubicacionLng = ubicacionLng;
    }

    public String getUbicacionLtd() {
        return ubicacionLtd;
    }

    public void setUbicacionLtd(String ubicacionLtd) {
        this.ubicacionLtd = ubicacionLtd;
    }

    public BigDecimal getPrecioMin() {
        return precioMin;
    }

    public void setPrecioMin(BigDecimal precioMin) {
        this.precioMin = precioMin;
    }

    public BigDecimal getPrecioMax() {
        return precioMax;
    }

    public void setPrecioMax(BigDecimal precioMax) {
        this.precioMax = precioMax;
    }

    public BigDecimal getPuntosGenerados() {
        return puntosGenerados;
    }

    public void setPuntosGenerados(BigDecimal puntosGenerados) {
        this.puntosGenerados = puntosGenerados;
    }

    public BigDecimal getPuntosCanjeados() {
        return puntosCanjeados;
    }

    public void setPuntosCanjeados(BigDecimal puntosCanjeados) {
        this.puntosCanjeados = puntosCanjeados;
    }

    public BigDecimal getPuntosCanjeadosOtros() {
        return puntosCanjeadosOtros;
    }

    public void setPuntosCanjeadosOtros(BigDecimal puntosCanjeadosOtros) {
        this.puntosCanjeadosOtros = puntosCanjeadosOtros;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public LugarEnumerated.ESTADO_LGR getEstado() {
        return estado;
    }

    public void setEstado(LugarEnumerated.ESTADO_LGR estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Evento> getEventoList() {
        return eventoList;
    }

    public void setEventoList(List<Evento> eventoList) {
        this.eventoList = eventoList;
    }

    public Ciudad getCiudadId() {
        return ciudadId;
    }

    public void setCiudadId(Ciudad ciudadId) {
        this.ciudadId = ciudadId;
    }

    public CodigoPostal getCodigoPostalId() {
        return codigoPostalId;
    }

    public void setCodigoPostalId(CodigoPostal codigoPostalId) {
        this.codigoPostalId = codigoPostalId;
    }

    public Segmento getSegmentoId() {
        return segmentoId;
    }

    public void setSegmentoId(Segmento segmentoId) {
        this.segmentoId = segmentoId;
    }

    public Imagen getImagenPortadaId() {
        return imagenPortadaId;
    }

    public void setImagenPortadaId(Imagen imagenPortadaId) {
        this.imagenPortadaId = imagenPortadaId;
    }

    public Imagen getImagenLogoId() {
        return imagenLogoId;
    }

    public void setImagenLogoId(Imagen imagenLogoId) {
        this.imagenLogoId = imagenLogoId;
    }

    public Cliente getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(Cliente propietarioId) {
        this.propietarioId = propietarioId;
    }

    @XmlTransient
    public List<Oferta> getOfertaList() {
        return ofertaList;
    }

    public void setOfertaList(List<Oferta> ofertaList) {
        this.ofertaList = ofertaList;
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
        if (!(object instanceof Lugar)) {
            return false;
        }
        Lugar other = (Lugar) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.historicos.api.bean.Lugar[ id=" + id + " ]";
    }
    
}
