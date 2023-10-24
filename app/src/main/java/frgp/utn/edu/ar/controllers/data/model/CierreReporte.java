package frgp.utn.edu.ar.controllers.data.model;

import android.graphics.Bitmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CierreReporte {
    private Reporte reporte;
    private Usuario user;
    private String motivo;
    private Bitmap imagen;
    private String fecha_cierre; // Cambiado a String
    private EstadoReporte estado;

    public CierreReporte(Reporte reporte, Usuario user, String motivo, Bitmap imagen, String fecha_cierre, EstadoReporte estado) {
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

    // Agrega métodos para convertir entre Date y String
    public Date getFechaCierreAsDate() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return format.parse(fecha_cierre);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setFechaCierreFromDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        fecha_cierre = format.format(date);
    }

    public EstadoReporte getEstado() {
        return estado;
    }

    public void setEstado(EstadoReporte estado) {
        this.estado = estado;
    }
}
