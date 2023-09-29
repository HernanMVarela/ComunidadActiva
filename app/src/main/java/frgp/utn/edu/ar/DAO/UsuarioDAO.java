package frgp.utn.edu.ar.DAO;

import android.content.Context;

import frgp.utn.edu.ar.entidades.Usuario;

public interface UsuarioDAO {
    boolean agregarUsuario(Context context, Usuario nuevo);
    boolean modificarUsuario(Context context, Usuario modifcar);
    Usuario buscarUsuarioPorId(Context context, int ID);
}
