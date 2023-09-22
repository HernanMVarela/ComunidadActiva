package frgp.utn.edu.ar.entidades;

public class TipoDenuncia {
    private int id;
    private String tipo;

    public TipoDenuncia(int id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public TipoDenuncia() {
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
