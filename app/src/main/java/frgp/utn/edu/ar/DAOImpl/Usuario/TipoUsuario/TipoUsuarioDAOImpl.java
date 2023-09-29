package frgp.utn.edu.ar.DAOImpl.Usuario.TipoUsuario;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Objects;

import frgp.utn.edu.ar.DAO.TiposUsuarioDAO;
import frgp.utn.edu.ar.DAO.UsuarioDAO;
import frgp.utn.edu.ar.DAOImpl.Usuario.DMABuscarUsuarioPorId;
import frgp.utn.edu.ar.DAOImpl.Usuario.DMAListarUsuarios;
import frgp.utn.edu.ar.DAOImpl.Usuario.DMAListarUsuariosPorEstado;
import frgp.utn.edu.ar.DAOImpl.Usuario.DMAListarUsuariosPorTipo;
import frgp.utn.edu.ar.DAOImpl.Usuario.DMALoginUsuario;
import frgp.utn.edu.ar.DAOImpl.Usuario.DMANuevoUsuario;
import frgp.utn.edu.ar.DAOImpl.Usuario.DMAUpdateUsuario;
import frgp.utn.edu.ar.entidades.TipoUsuario;
import frgp.utn.edu.ar.entidades.Usuario;

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
