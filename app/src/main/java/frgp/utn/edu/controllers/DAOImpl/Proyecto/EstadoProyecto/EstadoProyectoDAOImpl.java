package frgp.utn.edu.controllers.DAOImpl.Proyecto.EstadoProyecto;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Objects;

import frgp.utn.edu.controllers.DAO.EstadoProyectoDAO;
import frgp.utn.edu.controllers.data.model.EstadoProyecto;

public class EstadoProyectoDAOImpl implements EstadoProyectoDAO {

    @Override
    public EstadoProyecto buscarEstadoProyectoPorId(Context context, int ID) {
        DMABuscarEstadoProyectoPorId DMABPPI = new DMABuscarEstadoProyectoPorId(ID, context);
        DMABPPI.execute();
        try {
            return DMABPPI.get();
        } catch (Exception e) {
            Log.println(Log.ERROR, "Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    @Override
    public List<EstadoProyecto> listarEstadosProyectos(Context context) {
        return null;
    }
}
