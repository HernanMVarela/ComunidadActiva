package frgp.utn.edu.ar.controllers.data.model;

import java.sql.Timestamp;
import java.util.Date;

public class Notificacion {
    private int ID;
    private int IdUser;
    private String descripcion;
    private Timestamp fecha;
    private boolean lectura;


    public Notificacion(int ID, int idUser, String descripcion, Timestamp fecha, boolean lectura) {
        this.ID = ID;
        IdUser = idUser;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.lectura = lectura;
    }

    public Notificacion() {

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(ID).append("\n");
        sb.append("IdUser: ").append(IdUser).append("\n");
        sb.append("Descripcion: ").append(descripcion).append("\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        sb.append("Lectura: ").append(lectura).append("\n");
        return sb.toString();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getIdUser() {
        return IdUser;
    }

    public void setIdUser(int idUser) {
        IdUser = idUser;
    }

     public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public boolean getLectura() {
        return lectura;
    }

    public void setLectura(boolean lectura) {
        this.lectura = lectura;
    }
}
