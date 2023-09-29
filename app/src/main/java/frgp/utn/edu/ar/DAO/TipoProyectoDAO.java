package frgp.utn.edu.ar.DAO;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.ar.entidades.TipoProyecto;

public interface TipoProyectoDAO {
    TipoProyecto buscarTipoProyectoPorId(Context context, int ID);
    List<TipoProyecto> listarTiposProyecto(Context context);
}
