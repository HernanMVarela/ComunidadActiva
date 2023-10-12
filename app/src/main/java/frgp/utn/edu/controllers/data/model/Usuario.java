package frgp.utn.edu.controllers.data.model;

import java.util.Date;

public class Usuario {
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

    public Usuario() {
    }

    public Usuario(int id, String username, String password, int puntuacion, String nombre, String apellido, String telefono, String correo, Date fecha_nac, Date fecha_alta, EstadoUsuario estado, TipoUsuario tipo) {
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
}

