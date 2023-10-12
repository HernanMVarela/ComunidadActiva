package frgp.utn.edu.ar.controllers.DAOImpl.Usuario.EstadoUsuario;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Objects;

import frgp.utn.edu.ar.controllers.DAO.EstadoUsuarioDAO;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;

public class EstadoUsuarioDAOImpl implements EstadoUsuarioDAO {

    @Override
    public EstadoUsuario buscarEstadoUsuarioPorId(Context context, int ID) {
        DMABuscarEstadoUsuarioPorId DMABEUPI = new DMABuscarEstadoUsuarioPorId(ID, context);
        DMABEUPI.execute();
        try {
            return DMABEUPI.get();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    @Override
    public List<EstadoUsuario> listarEstadosUsuario(Context context) {
        DMAListarEstadosUsuarios DMALEU = new DMAListarEstadosUsuarios(context);
        DMALEU.execute();
        try {
            return DMALEU.get();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }
}
