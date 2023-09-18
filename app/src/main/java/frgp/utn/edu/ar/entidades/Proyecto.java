package frgp.utn.edu.ar.entidades;

import java.util.Date;
import java.util.List;

public class Proyecto extends Publicacion{

    private int cupo;
    private List<Usuario> voluntarios;
    private TipoProyecto tipo;
    private EstadoProyecto estado;

    public Proyecto(int ID, String titulo, String descripcion, Ubicacion ubicacion, Date fecha, Usuario owner, int cupo, List<Usuario> voluntarios, TipoProyecto tipo, EstadoProyecto estado) {
        super(ID, titulo, descripcion, ubicacion, fecha, owner);
        this.cupo = cupo;
        this.voluntarios = voluntarios;
        this.tipo = tipo;
        this.estado = estado;
    }
}
