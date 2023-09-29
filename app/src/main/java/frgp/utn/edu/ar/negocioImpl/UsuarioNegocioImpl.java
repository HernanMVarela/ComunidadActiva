package frgp.utn.edu.ar.NegocioImpl;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.ar.DAO.EstadoUsuarioDAO;
import frgp.utn.edu.ar.DAO.TiposUsuarioDAO;
import frgp.utn.edu.ar.DAO.UsuarioDAO;
import frgp.utn.edu.ar.DAOImpl.Usuario.EstadoUsuario.EstadoUsuarioDAOImpl;
import frgp.utn.edu.ar.DAOImpl.Usuario.TipoUsuario.TipoUsuarioDAOImpl;
import frgp.utn.edu.ar.DAOImpl.Usuario.UsuarioDAOImpl;
import frgp.utn.edu.ar.entidades.EstadoUsuario;
import frgp.utn.edu.ar.entidades.TipoUsuario;
import frgp.utn.edu.ar.entidades.Usuario;
import frgp.utn.edu.ar.Negocio.UsuarioNegocio;

public class UsuarioNegocioImpl implements UsuarioNegocio {
    private UsuarioDAO UserDAO = new UsuarioDAOImpl();
    private TiposUsuarioDAO TiposUserDAO = new TipoUsuarioDAOImpl();
    private EstadoUsuarioDAO EstadoUserDAO = new EstadoUsuarioDAOImpl();

    @Override
    public boolean agregarUsuario(Context context, Usuario nuevo) {
        return UserDAO.agregarUsuario(context, nuevo);
    }

    @Override
    public boolean modificarUsuario(Context context, Usuario modifcar) {
        return UserDAO.modificarUsuario(context, modifcar);
    }

    @Override
    public Usuario buscarUsuarioPorId(Context context, int ID) {
        return UserDAO.buscarUsuarioPorId(context, ID);
    }

    @Override
    public Usuario login(Context context, String username, String password) {
        return UserDAO.login(context, username, password);
    }

    @Override
    public List<Usuario> listarUsuarios(Context context) {
        return UserDAO.listarUsuarios(context);
    }

    @Override
    public List<Usuario> listarUsuariosPorTipo(Context context, int tipoUsuario) {
        return UserDAO.listarUsuariosPorTipo(context, tipoUsuario);
    }

    @Override
    public List<Usuario> listarUsuariosPorEstado(Context context, int estado) {
        return UserDAO.listarUsuariosPorEstado(context, estado);
    }

    @Override
    public List<TipoUsuario> listarTiposUsuario(Context context) {
        return TiposUserDAO.listarTiposUsuario(context);
    }

    @Override
    public List<EstadoUsuario> listarEstadosUsuario(Context context) {
        return EstadoUserDAO.listarEstadosUsuario(context);
    }
}