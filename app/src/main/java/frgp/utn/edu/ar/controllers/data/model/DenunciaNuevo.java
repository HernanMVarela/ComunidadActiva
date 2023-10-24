package frgp.utn.edu.ar.controllers.data.model;

import java.util.Date;

public class DenunciaNuevo {

    private int id;

    private Usuario denunciante;
    private EstadoDenuncia estado;
    private String titulo;
    private String descripcion;
    private Date fecha_creacion;

    public DenunciaNuevo(int id, Usuario denunciante, EstadoDenuncia estado, String titulo, String descripcion, Date fecha_creacion) {
        this.id = id;
        this.denunciante = denunciante;
        this.estado = estado;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
    }

    public DenunciaNuevo() {
    }

    @Override
    public String toString() {
        return "Denuncia{" +
                "id=" + id +
                ", denunciante=" + denunciante +
                ", estado=" + estado +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
