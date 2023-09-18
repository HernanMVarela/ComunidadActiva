package frgp.utn.edu.ar.entidades;

public class Denuncia {
    private Publicacion publicacion;
    private TipoPublicacion tipo;
    private Usuario denunciante;
    private EstadoDenuncia estado;
    private String Titutlo;
    private String Descripcion;

    public Denuncia(Publicacion publicacion, TipoPublicacion tipo, Usuario denunciante, EstadoDenuncia estado, String titutlo, String descripcion) {
        this.publicacion = publicacion;
        this.tipo = tipo;
        this.denunciante = denunciante;
        this.estado = estado;
        Titutlo = titutlo;
        Descripcion = descripcion;
    }

    public Denuncia() {
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public TipoPublicacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoPublicacion tipo) {
        this.tipo = tipo;
    }

    public Usuario getDenunciante() {
        return denunciante;
    }

    public void setDenunciante(Usuario denunciante) {
        this.denunciante = denunciante;
    }

    public EstadoDenuncia getEstado() {
        return estado;
    }

    public void setEstado(EstadoDenuncia estado) {
        this.estado = estado;
    }

    public String getTitutlo() {
        return Titutlo;
    }

    public void setTitutlo(String titutlo) {
        Titutlo = titutlo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
