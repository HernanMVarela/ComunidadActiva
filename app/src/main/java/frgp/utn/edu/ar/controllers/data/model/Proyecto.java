package frgp.utn.edu.ar.controllers.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Proyecto extends Publicacion implements Serializable {
    private String descripcion;
    private int cupo;
    private List<Voluntario> voluntarios;
    private TipoProyecto tipo;
    private String requerimientos;
    private String contacto;
    private EstadoProyecto estado;

    public Proyecto(int id, String titulo, String descripcion, double latitud, double longitud, Date fecha, Usuario owner, int cupo, List<Voluntario> voluntarios, TipoProyecto tipo, String requerimientos, String contacto, EstadoProyecto estado) {
        super(id, titulo, descripcion, latitud,longitud, fecha, owner);
        this.descripcion = descripcion;
        this.cupo = cupo;
        this.voluntarios = voluntarios;
        this.tipo = tipo;
        this.requerimientos = requerimientos;
        this.contacto = contacto;
        this.estado = estado;
    }

    public Proyecto() {
    }

    @Override
    public String toString() {
        return "Proyecto{" + super.toString() +
                " tipo=" + tipo + '\'' +
                " requerimientos=" + requerimientos + '\'' +
                " descripcion='" + descripcion + '\'' +
                ", cupo=" + cupo +
                ", voluntarios=" + voluntarios +
                ", contacto=" + contacto +
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

    public List<Voluntario> getVoluntarios() {
        return voluntarios;
    }

    public void setVoluntarios(List<Voluntario> voluntarios) {
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

    public String getRequerimientos() {
        return requerimientos;
    }

    public void setRequerimientos(String requerimientos) {
        this.requerimientos = requerimientos;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

}
