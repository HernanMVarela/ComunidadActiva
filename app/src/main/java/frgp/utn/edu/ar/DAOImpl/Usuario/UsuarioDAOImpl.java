package frgp.utn.edu.ar.DAOImpl.Usuario;

import android.content.Context;
import android.util.Log;

import java.util.Objects;

import frgp.utn.edu.ar.DAO.UsuarioDAO;
import frgp.utn.edu.ar.entidades.Usuario;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public boolean agregarUsuario(Context context, Usuario nuevo) {
        DMANuevoUsuario DMANU = new DMANuevoUsuario(nuevo, context);
        DMANU.execute();
        try {
            return DMANU.get();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }

    @Override
    public boolean modificarUsuario(Context context, Usuario modificar) {
        DMAUpdateUsuario DMAUU = new DMAUpdateUsuario(modificar, context);
        DMAUU.execute();
        try {
            return DMAUU.get();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }

    @Override
    public Usuario buscarUsuarioPorId(Context context, int ID) {
        DMABuscarUsuarioPorId DMABU = new DMABuscarUsuarioPorId(ID, context);
        DMABU.execute();
        try {
            return DMABU.get();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

}
