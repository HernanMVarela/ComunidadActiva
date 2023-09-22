package frgp.utn.edu.ar.negocio;

import frgp.utn.edu.ar.entidades.Usuario;

public interface UsuarioNegocio {
    public boolean guardarUsuario(Usuario nuevo);
    public boolean modificarUsuario(Usuario modificar);
    public boolean eliminarUsuarioPorUsername(String username);
    public boolean eliminarUsuarioPorID(int id);
    public Usuario buscarUsuarioPorUsername(String username);
    public Usuario buscarUsuarioPorID(int ID);
    public boolean existeUsuario(String username);
}
