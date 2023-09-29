package frgp.utn.edu.ar.DAO;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.ar.entidades.EstadoProyecto;

public interface EstadoProyectoDAO {
    EstadoProyecto buscarEstadoProyectoPorId(Context context, int ID);
    List<EstadoProyecto> listarEstadosProyectos(Context context);
}
