package frgp.utn.edu.ar.controllers.data.model;

import java.sql.Timestamp;
import java.util.Date;

import frgp.utn.edu.ar.controllers.utils.LogsEnum;

public class Logs {
    private int ID;
    private int IdUser;
    private LogsEnum accion;
    private String descripcion;
    private Timestamp fecha;

    public Logs(int ID, int idUser, LogsEnum accion, String descripcion, Timestamp fecha) {
        this.ID = ID;
        IdUser = idUser;
        this.accion = accion;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(ID).append("\n");
        sb.append("IdUser: ").append(IdUser).append("\n");
        sb.append("Accion: ").append(accion).append("\n");
        sb.append("Descripcion: ").append(descripcion).append("\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        return sb.toString();
    }

    public Logs() {
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

    public LogsEnum getAccion() {
        return accion;
    }

    public void setAccion(LogsEnum accion) {
        this.accion = accion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
