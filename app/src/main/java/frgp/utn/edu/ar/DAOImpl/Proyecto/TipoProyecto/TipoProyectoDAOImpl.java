package frgp.utn.edu.ar.DAOImpl.Proyecto.TipoProyecto;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Objects;

import frgp.utn.edu.ar.DAO.TipoProyectoDAO;
import frgp.utn.edu.ar.entidades.TipoProyecto;

public class TipoProyectoDAOImpl implements TipoProyectoDAO {

    @Override
    public TipoProyecto buscarTipoProyectoPorId(Context context, int ID) {
        DMABuscarTipoProyectoPorId DMABTPPI = new DMABuscarTipoProyectoPorId(ID, context);
        DMABTPPI.execute(String.valueOf(ID));
        try {
            return DMABTPPI.get();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    @Override
    public List<TipoProyecto> listarTiposProyecto(Context context) {
        return null;
    }
}
