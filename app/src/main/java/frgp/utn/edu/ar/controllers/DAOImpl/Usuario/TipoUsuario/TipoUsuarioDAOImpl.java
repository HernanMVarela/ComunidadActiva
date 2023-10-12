package frgp.utn.edu.ar.controllers.DAOImpl.Usuario.TipoUsuario;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Objects;

import frgp.utn.edu.ar.controllers.DAO.TiposUsuarioDAO;
import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;

public class TipoUsuarioDAOImpl implements TiposUsuarioDAO {

    @Override
    public TipoUsuario buscarTipoUsuarioPorId(Context context, int ID) {
        DMABuscarTipoUsuarioPorId DMABTUPI = new DMABuscarTipoUsuarioPorId(ID, context);
        DMABTUPI.execute();
        try {
            return DMABTUPI.get();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    @Override
    public List<TipoUsuario> listarTiposUsuario(Context context) {
        return null;
    }
}
