package frgp.utn.edu.ar.DAO;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.ar.entidades.TipoUsuario;
import frgp.utn.edu.ar.entidades.Usuario;

public interface TiposUsuarioDAO {
    TipoUsuario buscarTipoUsuarioPorId(Context context, int ID);
    List<TipoUsuario> listarTiposUsuario(Context context);
}
