package frgp.utn.edu.ar.controllers.data.model;

import java.util.Date;

public class AtencionDenuncia  {
    private Publicacion publicacion;
    private TipoDenuncia tipo;
    private Usuario moderador;
    private Date fecha;
    private String comentario;
    private EstadoDenuncia estado;

    public AtencionDenuncia(Publicacion publicacion, TipoDenuncia tipo, Usuario moderador, Date fecha, String comentario, EstadoDenuncia estado) {
        this.publicacion = publicacion;
        this.tipo = tipo;
        this.moderador = moderador;
        this.fecha = fecha;
        this.comentario = comentario;
        this.estado = estado;
    }

    public AtencionDenuncia() {
    }

    @Override
    public String toString() {
        return "AtencionDenuncia{" +
                "publicacion=" + publicacion +
                ", tipo=" + tipo +
                ", moderador=" + moderador +
                ", fecha=" + fecha +
                ", Comentario='" + comentario + '\'' +
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
    public Usuario getModerador() {
        return moderador;
    }
    public void setModerador(Usuario moderador) {
        this.moderador = moderador;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public String getComentario() {
        return comentario;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public EstadoDenuncia getEstado() {
        return estado;
    }

    public void setEstado(EstadoDenuncia estado) {
        this.estado = estado;
    }
}
