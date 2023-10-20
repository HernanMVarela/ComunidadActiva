package frgp.utn.edu.ar.controllers.data.model;

import java.io.Serializable;

public class TipoReporte implements Serializable {
    private int id;
    private String tipo;

    public TipoReporte(int id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }
    public TipoReporte() {
    }
    @Override
    public String toString() {
        return id + " - " + tipo;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
