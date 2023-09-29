package frgp.utn.edu.ar.Negocio;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.ar.entidades.EstadoUsuario;
import frgp.utn.edu.ar.entidades.TipoUsuario;
import frgp.utn.edu.ar.entidades.Usuario;

public interface UsuarioNegocio {
    boolean agregarUsuario(Context context, Usuario nuevo);
    boolean modificarUsuario(Context context, Usuario modifcar);
    Usuario buscarUsuarioPorId(Context context, int ID);
    Usuario login(Context context, String username, String password);
    List<Usuario> listarUsuarios(Context context);
    List<Usuario> listarUsuariosPorTipo(Context context, int tipoUsuario);
    List<Usuario> listarUsuariosPorEstado(Context context, int estado);
    List<TipoUsuario> listarTiposUsuario(Context context);
    List<EstadoUsuario> listarEstadosUsuario(Context context);
}
