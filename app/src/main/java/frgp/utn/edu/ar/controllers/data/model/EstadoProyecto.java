package frgp.utn.edu.ar.controllers.data.model;

public class EstadoProyecto {
    private int id;
    private String estado;

    public EstadoProyecto(int id, String estado) {
        this.id = id;
        this.estado = estado;
    }
    public EstadoProyecto() {
    }
    @Override
    public String toString() {
        return id + " - " + estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
