package frgp.utn.edu.ar.entidades;

import android.location.Location;

import java.sql.Blob;
import java.util.Date;

public class Reporte extends Publicacion{
    private Blob imagen;
    private int puntaje;
    private EstadoReporte estado;
    private TipoReporte tipo;

    public Reporte(int id, String titulo, Location location, Date fecha, Usuario owner, Blob imagen, int puntaje, EstadoReporte estado, TipoReporte tipo) {
        super(id, titulo, location, fecha, owner);
        this.imagen = imagen;
        this.puntaje = puntaje;
        this.estado = estado;
        this.tipo = tipo;
    }

    public Reporte() {
    }

    @Override
    public String toString() {
        return "Reporte{" + super.toString() +
                "imagen=" + imagen +
                ", puntaje=" + puntaje +
                ", estado=" + estado +
                ", tipo=" + tipo +
                '}';
    }

    public Blob getImagen() {
        return imagen;
    }

    public void setImagen(Blob imagen) {
        this.imagen = imagen;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public EstadoReporte getEstado() {
        return estado;
    }

    public void setEstado(EstadoReporte estado) {
        this.estado = estado;
    }

    public TipoReporte getTipo() {
        return tipo;
    }

    public void setTipo(TipoReporte tipo) {
        this.tipo = tipo;
    }
}
