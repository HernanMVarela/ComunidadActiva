package frgp.utn.edu.ar.entidades;

public class TipoUsuario {
    private int ID;
    private String Tipo;

    public TipoUsuario(int ID, String tipo) {
        this.ID = ID;
        Tipo = tipo;
    }

    public TipoUsuario() {
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
