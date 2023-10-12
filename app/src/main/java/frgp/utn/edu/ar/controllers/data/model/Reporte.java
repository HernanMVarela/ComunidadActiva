package frgp.utn.edu.ar.controllers.data.model;

import android.graphics.Bitmap;

import java.util.Date;

public class Reporte extends Publicacion{
    private Bitmap imagen;
    private int puntaje;
    private EstadoReporte estado;
    private TipoReporte tipo;

    public Reporte(int id, String titulo, double latitud, double longitud, Date fecha, Usuario owner, Bitmap imagen, int puntaje, EstadoReporte estado, TipoReporte tipo) {
        super(id, titulo, latitud,longitud, fecha, owner);
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

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
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
