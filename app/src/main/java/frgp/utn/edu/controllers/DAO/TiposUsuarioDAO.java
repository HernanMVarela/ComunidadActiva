package frgp.utn.edu.controllers.DAO;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.controllers.data.model.TipoUsuario;

public interface TiposUsuarioDAO {
    TipoUsuario buscarTipoUsuarioPorId(Context context, int ID);
    List<TipoUsuario> listarTiposUsuario(Context context);
}
