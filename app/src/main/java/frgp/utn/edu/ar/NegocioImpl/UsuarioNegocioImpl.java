package frgp.utn.edu.ar.negocioImpl;

import frgp.utn.edu.ar.DAO.UsuarioDAO;
import frgp.utn.edu.ar.DAOImpl.UsuarioDAOImpl;
import frgp.utn.edu.ar.entidades.Usuario;
import frgp.utn.edu.ar.negocio.UsuarioNegocio;

public class UsuarioNegocioImpl implements UsuarioNegocio {
    private UsuarioDAO UserDAO = new UsuarioDAOImpl();

    @Override
    public boolean guardarUsuario(Usuario nuevo) {
        if(UserDAO.existeUsuarioPorUsername(nuevo.getUsername())){
            return false;
        }
        return UserDAO.guardarUsuario(nuevo);
    }
    @Override
    public boolean modificarUsuario(Usuario modificar) {
        if(UserDAO.existeUsuarioPorUsername(modificar.getUsername())){
            return UserDAO.modificarUsuario(modificar);
        }
        return false;
    }
    @Override
    public boolean eliminarUsuarioPorUsername(String username) {
        if(UserDAO.existeUsuarioPorUsername(username)){
            return UserDAO.eliminarUsuarioPorUsername(username);
        }
        return false;
    }
    @Override
    public boolean eliminarUsuarioPorID(int id) {
        if(UserDAO.existeUsuarioPorID(id)){
            return UserDAO.eliminarUsuarioPorID(id);
        }
        return false;
    }
    @Override
    public Usuario buscarUsuarioPorUsername(String username) {
        return UserDAO.buscarUsuarioPorUsername(username);
    }
    @Override
    public Usuario buscarUsuarioPorID(int id) {
        return UserDAO.buscarUsuarioPorID(id);
    }
    @Override
    public boolean existeUsuario(String username) {
        return UserDAO.existeUsuarioPorUsername(username);
    }
}
