package frgp.utn.edu.ar.DAO;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.ar.entidades.EstadoUsuario;

public interface EstadoUsuarioDAO {
    EstadoUsuario buscarEstadoUsuarioPorId(Context context, int ID);
    List<EstadoUsuario> listarEstadosUsuario(Context context);
}
