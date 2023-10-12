package frgp.utn.edu.ar.controllers.DAO;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;

public interface TiposUsuarioDAO {
    TipoUsuario buscarTipoUsuarioPorId(Context context, int ID);
    List<TipoUsuario> listarTiposUsuario(Context context);
}
