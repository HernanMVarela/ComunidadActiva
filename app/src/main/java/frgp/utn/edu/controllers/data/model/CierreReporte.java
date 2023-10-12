package frgp.utn.edu.controllers.data.model;

import android.graphics.Bitmap;

import java.util.Date;

public class CierreReporte {
    private Reporte reporte;
    private Usuario user;
    private String motivo;
    private Bitmap imagen;
    private Date fecha_cierre;
    private EstadoReporte estado;

    public CierreReporte(Reporte reporte, Usuario user, String motivo, Bitmap imagen, Date fecha_cierre, EstadoReporte estado) {
        this.reporte = reporte;
        this.user = user;
        this.motivo = motivo;
        this.imagen = imagen;
        this.fecha_cierre = fecha_cierre;
        this.estado = estado;
    }

    public CierreReporte() {
    }

    @Override
    public String toString() {
        return "CierreReporte{" +
                "reporte=" + reporte +
                ", user=" + user +
                ", motivo='" + motivo + '\'' +
                ", imagen=" + imagen +
                ", fecha_cierre=" + fecha_cierre +
                ", estado=" + estado +
                '}';
    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public Date getFecha_cierre() {
        return fecha_cierre;
    }

    public void setFecha_cierre(Date fecha_cierre) {
        this.fecha_cierre = fecha_cierre;
    }

    public EstadoReporte getEstado() {
        return estado;
    }

    public void setEstado(EstadoReporte estado) {
        this.estado = estado;
    }
}
