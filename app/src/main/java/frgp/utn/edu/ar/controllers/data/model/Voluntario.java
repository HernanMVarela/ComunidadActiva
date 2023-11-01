package frgp.utn.edu.ar.controllers.data.model;

import java.util.Date;

public class Voluntario  extends Usuario{
    private Date fecha_union;
    private Date fecha_salida;
    private boolean activo;

    public Voluntario() {
    }

    public Voluntario(int id, String username, String password, int puntuacion, String nombre, String apellido, String telefono, String correo, Date fecha_nac, Date fecha_alta, EstadoUsuario estado, TipoUsuario tipo, String codigo_recuperacion, Date fecha_bloqueo, Date fecha_union, Date fecha_salida, boolean activo) {
        super(id, username, password, puntuacion, nombre, apellido, telefono, correo, fecha_nac, fecha_alta, estado, tipo, codigo_recuperacion, fecha_bloqueo);
        this.fecha_union = fecha_union;
        this.fecha_salida = fecha_salida;
        this.activo = activo;
    }

    public Date getFecha_union() {
        return fecha_union;
    }

    public void setFecha_union(Date fecha_union) {
        this.fecha_union = fecha_union;
    }

    public Date getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(Date fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
