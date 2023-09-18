package frgp.utn.edu.ar.entidades;

import java.util.Date;

public class Usuario {
    private int ID;
    private String Username;
    private String Password;
    private Date Creacion;
    private int Puntaje;
    private String Nombre;
    private String Apellido;
    private String Telefono;
    private String Correo;
    private Date Fecha_Nac;
    private EstadoUsuario Estado;
    private TipoUsuario Tipo;

    public Usuario(int ID, String username, String password,Date creacion, int puntaje, String nombre, String apellido, String telefono, String correo, Date fecha_Nac, EstadoUsuario estado, TipoUsuario tipo) {
        this.ID = ID;
        Username = username;
        Password = password;
        Creacion = creacion;
        Puntaje = puntaje;
        Nombre = nombre;
        Apellido = apellido;
        Telefono = telefono;
        Correo = correo;
        Fecha_Nac = fecha_Nac;
        Estado = estado;
        Tipo = tipo;
    }

    public Usuario() {
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "ID=" + ID +
                ", Username='" + Username + '\'' +
                ", Password='" + Password + '\'' +
                ", Creacion='" + Creacion + '\'' +
                ", Puntaje=" + Puntaje +
                ", Nombre='" + Nombre + '\'' +
                ", Apellido='" + Apellido + '\'' +
                ", Telefono='" + Telefono + '\'' +
                ", Correo='" + Correo + '\'' +
                ", Fecha_Nac=" + Fecha_Nac +
                ", Estado=" + Estado +
                ", Tipo=" + Tipo +
                '}';
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Date getCreacion() {
        return Creacion;
    }

    public void setCreacion(Date creacion) {
        Creacion = creacion;
    }

    public int getPuntaje() {
        return Puntaje;
    }

    public void setPuntaje(int puntaje) {
        Puntaje = puntaje;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public Date getFecha_Nac() {
        return Fecha_Nac;
    }

    public void setFecha_Nac(Date fecha_Nac) {
        Fecha_Nac = fecha_Nac;
    }

    public EstadoUsuario getEstado() {
        return Estado;
    }

    public void setEstado(EstadoUsuario estado) {
        Estado = estado;
    }

    public TipoUsuario getTipo() {
        return Tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        Tipo = tipo;
    }
}
