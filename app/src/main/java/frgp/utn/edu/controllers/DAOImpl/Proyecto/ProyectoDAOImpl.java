package frgp.utn.edu.controllers.DAOImpl.Proyecto;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Objects;

import frgp.utn.edu.controllers.DAO.ProyectoDAO;
import frgp.utn.edu.controllers.data.model.Proyecto;

public class ProyectoDAOImpl implements ProyectoDAO {

    @Override
    public boolean agregarProyecto(Context context, Proyecto proyecto) {
        DMANuevoProyecto DMANP = new DMANuevoProyecto(proyecto, context);
        DMANP.execute();
        try {
            return DMANP.get();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }

    @Override
    public boolean modificarProyecto(Context context, Proyecto proyecto) {
        DMAUpdateProyecto DMAUP = new DMAUpdateProyecto(proyecto, context);
        DMAUP.execute();
        try {
            return DMAUP.get();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }

    @Override
    public Proyecto buscarProyectoPorId(Context context, int ID) {
        DMABuscarProyectoPorId DMABP = new DMABuscarProyectoPorId(ID, context);
        DMABP.execute();
        try {
            return DMABP.get();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    @Override
    public List<Proyecto> listarProyectos(Context context) {
        DMAListarProyectos DMALP = new DMAListarProyectos(context);
        DMALP.execute();
        try {
            return DMALP.get();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    @Override
    public List<Proyecto> listarProyectosPorTipo(Context context, int tipoProyecto) {
        DMAListarProyectosPorTipo DMALPT = new DMAListarProyectosPorTipo(context, tipoProyecto);
        DMALPT.execute();
        try {
            return DMALPT.get();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    @Override
    public List<Proyecto> listarProyectosPorEstado(Context context, int estado) {
        DMAListarProyectosPorEstado DMALPE = new DMAListarProyectosPorEstado(context, estado);
        DMALPE.execute();
        try {
            return DMALPE.get();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    @Override
    public List<Proyecto> listarProyectosPorCreador(Context context, int idCreador) {
        DMAListarProyectosPorOwner DMALPC = new DMAListarProyectosPorOwner(context, idCreador);
        DMALPC.execute();
        try {
            return DMALPC.get();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }
}
