package frgp.utn.edu.ar.NegocioImpl;

import android.content.Context;

import frgp.utn.edu.ar.DAO.UsuarioDAO;
import frgp.utn.edu.ar.DAOImpl.UsuarioDAOImpl;
import frgp.utn.edu.ar.Negocio.UsuarioNegocio;
import frgp.utn.edu.ar.entidades.Usuario;

public class UsuarioNegocioImpl implements UsuarioNegocio {
    UsuarioDAO UserDAO = new UsuarioDAOImpl();
    @Override
    public boolean agregarUsuario(Context context, Usuario nuevo) {
        return UserDAO.agregarUsuario(context,nuevo);
    }

    @Override
    public boolean modificarUsuario(Context context, Usuario modifcar) {
        return UserDAO.modificarUsuario(context,modifcar);
    }

    @Override
    public boolean suspenderUsuario(Context context, int ID) {
        return UserDAO.suspenderUsuario(context,ID);
    }
}
