package frgp.utn.edu.ar.controllers.data.model;

import java.io.Serializable;
import java.util.Date;

public class Denuncia implements Serializable {
    private Publicacion publicacion;
    private TipoDenuncia tipo;
    private Usuario denunciante;
    private EstadoDenuncia estado;
    private String titulo;
    private String descripcion;
    private Date fecha_creacion;

    public Denuncia(Publicacion publicacion, TipoDenuncia tipo, Usuario denunciante, EstadoDenuncia estado, String titulo, String descripcion, Date fecha_creacion) {
        this.publicacion = publicacion;
        this.tipo = tipo;
        this.denunciante = denunciante;
        this.estado = estado;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
    }

    public Denuncia() {
    }

    @Override
    public String toString() {
        return "Denuncia{" +
                "publicacion=" + publicacion +
                ", tipo=" + tipo +
                ", denunciante=" + denunciante +
                ", estado=" + estado +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public TipoDenuncia getTipo() {
        return tipo;
    }

    public void setTipo(TipoDenuncia tipo) {
        this.tipo = tipo;
    }

    public Usuario getDenunciante() {
        return denunciante;
    }

    public void setDenunciante(Usuario denunciante) {
        this.denunciante = denunciante;
    }

    public EstadoDenuncia getEstado() {
        return estado;
    }

    public void setEstado(EstadoDenuncia estado) {
        this.estado = estado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}
