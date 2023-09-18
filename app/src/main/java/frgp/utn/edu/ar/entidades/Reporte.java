package frgp.utn.edu.ar.entidades;

import java.sql.Blob;
import java.util.Date;

public class Reporte extends Publicacion {
    private Blob Imagen;
    private Date Fecha;
    private int Puntaje;
    private TipoReporte Tipo;
    private EstadoReporte Estado;

    public Reporte() {
    }

    public Reporte(int ID, String titulo, String descripcion, Ubicacion ubicacion, Date fecha, Usuario owner, Blob imagen, Date fecha1, int puntaje, TipoReporte tipo, EstadoReporte estado) {
        super(ID, titulo, descripcion, ubicacion, fecha, owner);
        Imagen = imagen;
        Fecha = fecha1;
        Puntaje = puntaje;
        Tipo = tipo;
        Estado = estado;
    }

    @Override
    public String toString() {
        return "Reporte{" +
                "Imagen=" + Imagen +
                ", Fecha=" + Fecha +
                ", Puntaje=" + Puntaje +
                ", Tipo=" + Tipo +
                ", Estado=" + Estado +
                '}';
    }

    public Blob getImagen() {
        return Imagen;
    }

    public void setImagen(Blob imagen) {
        Imagen = imagen;
    }

    @Override
    public Date getFecha() {
        return Fecha;
    }

    @Override
    public void setFecha(Date fecha) {
        Fecha = fecha;
    }

    public int getPuntaje() {
        return Puntaje;
    }

    public void setPuntaje(int puntaje) {
        Puntaje = puntaje;
    }

    public TipoReporte getTipo() {
        return Tipo;
    }

    public void setTipo(TipoReporte tipo) {
        Tipo = tipo;
    }

    public EstadoReporte getEstado() {
        return Estado;
    }

    public void setEstado(EstadoReporte estado) {
        Estado = estado;
    }
}
