package frgp.utn.edu.controllers.DAO;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.controllers.data.model.EstadoUsuario;

public interface EstadoUsuarioDAO {
    EstadoUsuario buscarEstadoUsuarioPorId(Context context, int ID);
    List<EstadoUsuario> listarEstadosUsuario(Context context);
}
