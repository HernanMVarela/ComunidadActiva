package frgp.utn.edu.ar.controllers.data.repository.usuario;

import android.content.Context;
import android.util.Log;

import java.util.Objects;

import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMABuscarUsuarioPorMail;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMABuscarUsuarioPorUsername;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMANuevoUsuario;

public class UsuarioRepository {

    public boolean cargarUsuario(Usuario nuevo, Context context){
        DMANuevoUsuario cargarUsuarioEnDB = new DMANuevoUsuario(nuevo, context);
        cargarUsuarioEnDB.execute();
        try {
            return cargarUsuarioEnDB.get();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }

    public Usuario checkUserName(String userName, Context context){
        DMABuscarUsuarioPorUsername buscarUsuario = new DMABuscarUsuarioPorUsername(userName, context);
        buscarUsuario.execute();
        try {
            return buscarUsuario.get();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    public Usuario checkMail(String correo, Context context){
        DMABuscarUsuarioPorMail buscarUsuario = new DMABuscarUsuarioPorMail(correo, context);
        buscarUsuario.execute();
        try {
            return buscarUsuario.get();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }
}
