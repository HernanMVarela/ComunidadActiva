package frgp.utn.edu.ar.entidades;

public class EstadoUsuario {
    private int ID;
    private String Estado;

    public EstadoUsuario(int ID, String estado) {
        this.ID = ID;
        Estado = estado;
    }

    public EstadoUsuario() {
    }

    @Override
    public String toString() {
        return ID + " - " + Estado;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }
}
