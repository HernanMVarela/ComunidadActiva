package frgp.utn.edu.ar.negocioImpl;

import android.content.Context;

import frgp.utn.edu.ar.DAO.UsuarioDAO;
import frgp.utn.edu.ar.DAOImpl.UsuarioDAOImpl;
import frgp.utn.edu.ar.entidades.Usuario;
import frgp.utn.edu.ar.Negocio.UsuarioNegocio;

public class UsuarioNegocioImpl implements UsuarioNegocio {
    private UsuarioDAO UserDAO = new UsuarioDAOImpl();


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
