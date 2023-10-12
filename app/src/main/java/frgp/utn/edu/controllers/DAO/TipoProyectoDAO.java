package frgp.utn.edu.controllers.DAO;

import android.content.Context;

import java.util.List;

import frgp.utn.edu.controllers.data.model.TipoProyecto;

public interface TipoProyectoDAO {
    TipoProyecto buscarTipoProyectoPorId(Context context, int ID);
    List<TipoProyecto> listarTiposProyecto(Context context);
}
