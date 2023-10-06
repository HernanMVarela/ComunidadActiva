package frgp.utn.edu.ar.entidades;

import android.location.Location;

import java.util.Date;

public class Publicacion {
    private int id;
    private String titulo;
    private double longitud;
    private double latitud;
    private Date fecha;
    private Usuario owner;

    public Publicacion(int id, String titulo, double longitud, double latitud, Date fecha, Usuario owner) {
        this.id = id;
        this.titulo = titulo;
        this.longitud = longitud;
        this.latitud = latitud;
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
                ", Location='" + longitud + " - " + latitud +
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

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
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
