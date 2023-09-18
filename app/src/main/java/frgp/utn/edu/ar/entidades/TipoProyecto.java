package frgp.utn.edu.ar.entidades;

public class TipoProyecto {
    private int ID;
    private String Tipo;

    public TipoProyecto(int ID, String tipo) {
        this.ID = ID;
        Tipo = tipo;
    }

    public TipoProyecto() {
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
