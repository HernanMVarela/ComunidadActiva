package frgp.utn.edu.ar.controllers.data.model;

import java.io.Serializable;
import java.util.Date;

public class Usuario implements Serializable {
    private int id;
    private String username;
    private String password;
    private int puntuacion;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private Date fecha_nac;
    private Date fecha_alta;
    private EstadoUsuario estado;
    private TipoUsuario tipo;
    private String codigo_recuperacion;
    private Date fecha_bloqueo;

    public Usuario() {
    }

    public Usuario(int id, String username, String password, int puntuacion, String nombre, String apellido, String telefono, String correo, Date fecha_nac, Date fecha_alta, EstadoUsuario estado, TipoUsuario tipo, String codigo_recuperacion, Date fecha_bloqueo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.puntuacion = puntuacion;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.fecha_nac = fecha_nac;
        this.fecha_alta = fecha_alta;
        this.estado = estado;
        this.tipo = tipo;
        this.codigo_recuperacion = codigo_recuperacion;
        this.fecha_bloqueo = fecha_bloqueo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(Date fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public Date getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(Date fecha_alta) {
        this.fecha_alta = fecha_alta;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public String getCodigo_recuperacion() {
        return codigo_recuperacion;
    }

    public void setCodigo_recuperacion(String codigo_recuperacion) {
        this.codigo_recuperacion = codigo_recuperacion;
    }

    public Date getFecha_bloqueo() {
        return fecha_bloqueo;
    }

    public void setFecha_bloqueo(Date fecha_bloqueo) {
        this.fecha_bloqueo = fecha_bloqueo;
    }

    @Override
    public String toString() {
        //Create a string builder
        StringBuilder sb = new StringBuilder();
        //Append the values from the object to the string builder
        sb.append("ID: " + this.id + "\n");
        sb.append("USERNAME: " + this.username + "\n");
        sb.append("PASSWORD: " + this.password + "\n");
        sb.append("PUNTUACION: " + this.puntuacion + "\n");
        sb.append("NOMBRE: " + this.nombre + "\n");
        sb.append("APELLIDO: " + this.apellido + "\n");
        sb.append("TELEFONO: " + this.telefono + "\n");
        sb.append("CORREO: " + this.correo + "\n");
        sb.append("FECHA_NAC: " + this.fecha_nac + "\n");
        sb.append("FECHA_ALTA: " + this.fecha_alta + "\n");
        sb.append("ESTADO: " + this.estado + "\n");
        sb.append("TIPO: " + this.tipo + "\n");
        sb.append("CODIGO_RECUPERACION: " + this.codigo_recuperacion + "\n");
        sb.append("FECHA_BLOQUEO: " + this.fecha_bloqueo + "\n");
        //Return the string builder as a string
        return sb.toString();
    }
}

