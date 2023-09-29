package frgp.utn.edu.ar.NegocioImpl;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.ar.DAO.EstadoProyectoDAO;
import frgp.utn.edu.ar.DAO.EstadoUsuarioDAO;
import frgp.utn.edu.ar.DAO.ProyectoDAO;
import frgp.utn.edu.ar.DAO.TipoProyectoDAO;
import frgp.utn.edu.ar.DAO.TiposUsuarioDAO;
import frgp.utn.edu.ar.DAO.UsuarioDAO;
import frgp.utn.edu.ar.DAOImpl.Proyecto.EstadoProyecto.EstadoProyectoDAOImpl;
import frgp.utn.edu.ar.DAOImpl.Proyecto.ProyectoDAOImpl;
import frgp.utn.edu.ar.DAOImpl.Proyecto.TipoProyecto.TipoProyectoDAOImpl;
import frgp.utn.edu.ar.DAOImpl.Usuario.EstadoUsuario.EstadoUsuarioDAOImpl;
import frgp.utn.edu.ar.DAOImpl.Usuario.TipoUsuario.TipoUsuarioDAOImpl;
import frgp.utn.edu.ar.DAOImpl.Usuario.UsuarioDAOImpl;
import frgp.utn.edu.ar.Negocio.ProyectoNegocio;
import frgp.utn.edu.ar.entidades.EstadoProyecto;
import frgp.utn.edu.ar.entidades.EstadoUsuario;
import frgp.utn.edu.ar.entidades.Proyecto;
import frgp.utn.edu.ar.entidades.TipoProyecto;
import frgp.utn.edu.ar.entidades.TipoUsuario;
import frgp.utn.edu.ar.entidades.Usuario;

public class ProyectoNegocioImpl implements ProyectoNegocio {

    private ProyectoDAO proyectoDAO = new ProyectoDAOImpl();
    private EstadoProyectoDAO estadoProyectoDAO = new EstadoProyectoDAOImpl();
    private TipoProyectoDAO tipoProyectoDAO = new TipoProyectoDAOImpl();


    @Override
    public boolean agregarProyecto(Context context, Proyecto proyecto) {
        return proyectoDAO.agregarProyecto(context, proyecto);
    }

    @Override
    public boolean modificarProyecto(Context context, Proyecto proyecto) {
        return proyectoDAO.modificarProyecto(context, proyecto);
    }

    @Override
    public Proyecto buscarProyectoPorId(Context context, int ID) {
        return proyectoDAO.buscarProyectoPorId(context, ID);
    }

    @Override
    public List<Proyecto> listarProyectos(Context context) {
        return proyectoDAO.listarProyectos(context);
    }

    @Override
    public List<Proyecto> listarProyectosPorOwner(Context context, int idCreador) {
        return proyectoDAO.listarProyectosPorCreador(context, idCreador);
    }

    @Override
    public List<Proyecto> listarProyectosPorEstado(Context context, int idEstado) {
        return proyectoDAO.listarProyectosPorEstado(context, idEstado);
    }

    @Override
    public List<Proyecto> listarProyectosPorTipo(Context context, int idTipo) {
        return proyectoDAO.listarProyectosPorTipo(context, idTipo);
    }

    @Override
    public List<EstadoProyecto> listarEstadosProyecto(Context context) {
        return estadoProyectoDAO.listarEstadosProyectos(context);
    }

    @Override
    public List<TipoProyecto> listarTiposProyecto(Context context) {
        return tipoProyectoDAO.listarTiposProyecto(context);
    }
}
