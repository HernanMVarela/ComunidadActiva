package frgp.utn.edu.ar.entidades;

import java.util.Date;

public class Publicacion {
    private int ID;
    private String titulo;
    private String descripcion;
    private Ubicacion ubicacion;
    private Date fecha;
    private Usuario owner;

    public Publicacion(int ID, String titulo, String descripcion, Ubicacion ubicacion, Date fecha, Usuario owner) {
        this.ID = ID;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.owner = owner;
    }

    public Publicacion() {
    }

    @Override
    public String toString() {
        return "Publicacion{" +
                "ID=" + ID +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", ubicacion=" + ubicacion +
                ", fecha=" + fecha +
                ", owner=" + owner +
                '}';
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuario getOwner() {
        return owner;
    }

    public void setOwner(Usuario owner) {
        this.owner = owner;
    }
}
