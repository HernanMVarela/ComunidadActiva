package frgp.utn.edu.ar.entidades;

import android.location.Location;

import java.util.Date;

public class Publicacion {
    private int id;
    private String titulo;
    private Location location;
    private Date fecha;
    private Usuario owner;

    public Publicacion(int id, String titulo, Location location, Date fecha, Usuario owner) {
        this.id = id;
        this.titulo = titulo;
        this.location = location;
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
                ", Location='" + location +
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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
