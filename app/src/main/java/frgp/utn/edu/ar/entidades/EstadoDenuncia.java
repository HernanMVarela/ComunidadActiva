package frgp.utn.edu.ar.entidades;

public class EstadoDenuncia {
    private int ID;
    private String Estado;

    public EstadoDenuncia(int ID, String estado) {
        this.ID = ID;
        Estado = estado;
    }

    public EstadoDenuncia() {
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
