package frgp.utn.edu.ar.DAOImpl;

import android.content.Context;

import frgp.utn.edu.ar.DAO.UsuarioDAO;
import frgp.utn.edu.ar.entidades.Usuario;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public boolean agregarUsuario(Context context, Usuario nuevo) {
        return false;
    }

    @Override
    public boolean modificarUsuario(Context context, Usuario modifcar) {
        return false;
    }

    @Override
    public boolean suspenderUsuario(Context context, int ID) {
        return false;
    }
}
