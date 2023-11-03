package frgp.utn.edu.ar.controllers.data.model;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CierreDenuncia {

    private Denuncia denuncia;
    private Usuario user;
    private String motivo;
    private String fecha_cierre; // Cambiado a String
    private EstadoDenuncia estado;

    public CierreDenuncia(Denuncia denuncia, Usuario user, String motivo, String fecha_cierre, EstadoDenuncia estado) {
        this.denuncia = denuncia;
        this.user = user;
        this.motivo = motivo;
        this.fecha_cierre = fecha_cierre;
        this.estado = estado;
    }

    public CierreDenuncia() {
    }
    @Override
    public String toString() {
        return "CierreReporte{" +
                "denuncia=" + denuncia +
                ", user=" + user +
                ", motivo='" + motivo + '\'' +
                ", fecha_cierre=" + fecha_cierre +
                ", estado=" + estado +
                '}';
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
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
    public EstadoDenuncia getEstado() {
        return estado;
    }

    public void setEstado(EstadoDenuncia estado) {
        this.estado = estado;
    }

}
