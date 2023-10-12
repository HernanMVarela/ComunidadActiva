package frgp.utn.edu.controllers.DAO;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.controllers.data.model.Usuario;

public interface UsuarioDAO {
    boolean agregarUsuario(Context context, Usuario nuevo);
    boolean modificarUsuario(Context context, Usuario modifcar);
    Usuario buscarUsuarioPorId(Context context, int ID);
    Usuario login(Context context, String username, String password);
    List<Usuario> listarUsuarios(Context context);
    List<Usuario> listarUsuariosPorTipo(Context context, int tipoUsuario);
    List<Usuario> listarUsuariosPorEstado(Context context, int estado);
}
