package frgp.utn.edu.ar.DAO;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.ar.entidades.Proyecto;
import frgp.utn.edu.ar.entidades.Usuario;

public interface ProyectoDAO {
    boolean agregarProyecto(Context context, Proyecto proyecto);
    boolean modificarProyecto(Context context, Proyecto proyecto);
    Proyecto buscarProyectoPorId(Context context, int ID);
    List<Proyecto> listarProyectos(Context context);
    List<Proyecto> listarProyectosPorTipo(Context context, int tipoProyecto);
    List<Proyecto> listarProyectosPorEstado(Context context, int estado);
    List<Proyecto> listarProyectosPorCreador(Context context, int idCreador);
}
