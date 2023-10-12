package frgp.utn.edu.controllers.data.model;

import java.util.Date;
import java.util.List;

public class Proyecto extends Publicacion{
    private String descripcion;
    private int cupo;
    private List<Usuario> voluntarios;
    private TipoProyecto tipo;
    private EstadoProyecto estado;

    public Proyecto(int id, String titulo,  double latitud, double longitud, Date fecha, Usuario owner, String descripcion, int cupo, List<Usuario> voluntarios, TipoProyecto tipo, EstadoProyecto estado) {
        super(id, titulo, latitud,longitud, fecha, owner);
        this.descripcion = descripcion;
        this.cupo = cupo;
        this.voluntarios = voluntarios;
        this.tipo = tipo;
        this.estado = estado;
    }

    public Proyecto() {
    }

    @Override
    public String toString() {
        return "Proyecto{" + super.toString() +
                "descripcion='" + descripcion + '\'' +
                ", cupo=" + cupo +
                ", voluntarios=" + voluntarios +
                ", tipo=" + tipo +
                ", estado=" + estado +
                '}';
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public List<Usuario> getVoluntarios() {
        return voluntarios;
    }

    public void setVoluntarios(List<Usuario> voluntarios) {
        this.voluntarios = voluntarios;
    }

    public TipoProyecto getTipo() {
        return tipo;
    }

    public void setTipo(TipoProyecto tipo) {
        this.tipo = tipo;
    }

    public EstadoProyecto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProyecto estado) {
        this.estado = estado;
    }
}
