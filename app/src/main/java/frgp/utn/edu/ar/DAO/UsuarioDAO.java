package frgp.utn.edu.ar.DAO;

import frgp.utn.edu.ar.entidades.Usuario;

public interface UsuarioDAO {
    public boolean guardarUsuario(Usuario nuevo);
    public boolean modificarUsuario(Usuario modificar);
    public boolean eliminarUsuarioPorUsername(String username);
    public boolean eliminarUsuarioPorID(int id);
    public Usuario buscarUsuarioPorUsername(String username);
    public Usuario buscarUsuarioPorID(int ID);
    public boolean existeUsuarioPorUsername(String username);
    boolean existeUsuarioPorID(int id);
}
