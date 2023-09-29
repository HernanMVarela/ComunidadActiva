package frgp.utn.edu.ar.Negocio;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.ar.entidades.EstadoProyecto;
import frgp.utn.edu.ar.entidades.Proyecto;
import frgp.utn.edu.ar.entidades.TipoProyecto;

public interface ProyectoNegocio {
    boolean agregarProyecto(Context context, Proyecto proyecto);
    boolean modificarProyecto(Context context, Proyecto proyecto);
    Proyecto buscarProyectoPorId(Context context, int ID);
    List<Proyecto> listarProyectos(Context context);
    List<Proyecto> listarProyectosPorOwner(Context context, int idCreador);
    List<Proyecto> listarProyectosPorEstado(Context context, int idEstado);
    List<Proyecto> listarProyectosPorTipo(Context context, int idTipo);
    List<EstadoProyecto> listarEstadosProyecto(Context context);
    List<TipoProyecto> listarTiposProyecto(Context context);
}
