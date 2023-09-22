package frgp.utn.edu.ar.entidades;

import java.util.Date;

public class Publicacion {
    private int id;
    private String titulo;
    private String Location;
    private Date fecha;
    private Usuario owner;

    public Publicacion(int id, String titulo, String location, Date fecha, Usuario owner) {
        this.id = id;
        this.titulo = titulo;
        Location = location;
        this.fecha = fecha;
        this.owner = owner;
    }

    public Publicacion() {
    }

    @Override
    public String toString() {
        return "Publicacion{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", Location='" + Location + '\'' +
                ", fecha=" + fecha +
                ", owner=" + owner +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
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
