package frgp.utn.edu.controllers.data.model;

import java.util.Date;

public class AtencionDenuncia {
    private Publicacion publicacion;
    private TipoDenuncia tipo;
    private Usuario moderador;
    private Date fecha;
    private String Comentario;

    public AtencionDenuncia(Publicacion publicacion, TipoDenuncia tipo, Usuario moderador, Date fecha, String comentario) {
        this.publicacion = publicacion;
        this.tipo = tipo;
        this.moderador = moderador;
        this.fecha = fecha;
        Comentario = comentario;
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
                ", Comentario='" + Comentario + '\'' +
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
        return Comentario;
    }
    public void setComentario(String comentario) {
        Comentario = comentario;
    }
}
