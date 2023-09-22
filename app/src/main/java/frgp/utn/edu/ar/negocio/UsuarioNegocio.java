package frgp.utn.edu.ar.Negocio;

import android.content.Context;

import frgp.utn.edu.ar.entidades.Usuario;

public interface UsuarioNegocio {
    boolean agregarUsuario(Context context, Usuario nuevo);
    boolean modificarUsuario(Context context, Usuario modifcar);
    boolean suspenderUsuario(Context context, int ID);

}
