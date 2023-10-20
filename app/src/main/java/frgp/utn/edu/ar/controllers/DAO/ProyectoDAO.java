package frgp.utn.edu.ar.controllers.DAO;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.Proyecto;

public interface ProyectoDAO {
    boolean agregarProyecto(Context context, Proyecto proyecto);
    boolean modificarProyecto(Context context, Proyecto proyecto);
    List<Proyecto> listarProyectosPorTipo(Context context, int tipoProyecto);
    List<Proyecto> listarProyectosPorEstado(Context context, int estado);
    List<Proyecto> listarProyectosPorCreador(Context context, int idCreador);
}
