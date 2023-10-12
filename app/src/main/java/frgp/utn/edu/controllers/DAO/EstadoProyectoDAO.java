package frgp.utn.edu.controllers.DAO;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.controllers.data.model.EstadoProyecto;

public interface EstadoProyectoDAO {
    EstadoProyecto buscarEstadoProyectoPorId(Context context, int ID);
    List<EstadoProyecto> listarEstadosProyectos(Context context);
}
