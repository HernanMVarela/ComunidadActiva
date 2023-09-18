package frgp.utn.edu.ar.DAOImpl;

import frgp.utn.edu.ar.DAO.UsuarioDAO;
import frgp.utn.edu.ar.entidades.Usuario;

public class UsuarioDAOImpl implements UsuarioDAO {
    @Override
    public boolean guardarUsuario(Usuario nuevo) {
        return false;
    }
    @Override
    public boolean modificarUsuario(Usuario modificar) {
        return false;
    }
    @Override
    public boolean eliminarUsuarioPorUsername(String username) {
        return false;
    }
    @Override
    public boolean eliminarUsuarioPorID(int id) {
        return false;
    }
    @Override
    public Usuario buscarUsuarioPorUsername(String username) {
        return null;
    }
    @Override
    public Usuario buscarUsuarioPorID(int ID) {
        return null;
    }
    @Override
    public boolean existeUsuarioPorUsername(String username) {
        return false;
    }
    @Override
    public boolean existeUsuarioPorID(int id) {
        return false;
    }
}
