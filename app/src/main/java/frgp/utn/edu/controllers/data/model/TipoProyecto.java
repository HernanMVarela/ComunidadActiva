package frgp.utn.edu.controllers.data.model;

public class TipoProyecto {
    private int id;
    private String tipo;

    public TipoProyecto(int id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public TipoProyecto() {
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
