package frgp.utn.edu.controllers.data.model;

public class TipoPublicacion {
    private int ID;
    private String Tipo;

    public TipoPublicacion(int ID, String tipo) {
        this.ID = ID;
        Tipo = tipo;
    }

    public TipoPublicacion() {
    }

    @Override
    public String toString() {
        return ID + " - " + Tipo;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

}
