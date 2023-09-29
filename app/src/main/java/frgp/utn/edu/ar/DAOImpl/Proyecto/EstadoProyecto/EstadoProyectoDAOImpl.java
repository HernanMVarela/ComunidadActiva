package frgp.utn.edu.ar.DAOImpl.Proyecto.EstadoProyecto;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Objects;

import frgp.utn.edu.ar.DAO.EstadoProyectoDAO;
import frgp.utn.edu.ar.DAOImpl.Usuario.EstadoUsuario.DMABuscarEstadoUsuarioPorId;
import frgp.utn.edu.ar.DAOImpl.Usuario.EstadoUsuario.DMAListarEstadosUsuarios;
import frgp.utn.edu.ar.entidades.EstadoProyecto;
import frgp.utn.edu.ar.entidades.EstadoUsuario;

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
