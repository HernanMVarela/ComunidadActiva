package frgp.utn.edu.ar.controllers.data.repository.usuario;

import android.content.Context;
import android.util.Log;

import java.util.Objects;

import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMABuscarUsuarioPorMail;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMABuscarUsuarioPorUsernameYPass;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMABuscarUsuarioPorUsername;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMAModificarUsuario;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMANuevoUsuario;

public class UsuarioRepository {

    public Usuario loginUsuario(String username, String pass) {
        DMABuscarUsuarioPorUsernameYPass buscarUsuario = new DMABuscarUsuarioPorUsernameYPass(username, pass);
        buscarUsuario.execute();
        try {
            return buscarUsuario.get();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }
    public boolean cargarUsuario(Usuario nuevo, Context context) {
        DMANuevoUsuario cargarUsuarioEnDB = new DMANuevoUsuario(nuevo, context);
        cargarUsuarioEnDB.execute();
        try {
            return cargarUsuarioEnDB.get();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }

    public boolean modificarUsuario(Usuario modificado) {
        DMAModificarUsuario modificarUsuario = new DMAModificarUsuario(modificado);
        modificarUsuario.execute();
        try {
            return modificarUsuario.get();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }

    public Usuario getUserByUserName(String userName) {
        DMABuscarUsuarioPorUsername buscarUsuario = new DMABuscarUsuarioPorUsername(userName);
        buscarUsuario.execute();
        try {
            return buscarUsuario.get();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    public Usuario getUserByMail(String correo, Context context) {
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
