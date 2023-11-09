package frgp.utn.edu.ar.controllers.data.repository.publicacion;

import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMABuscarPoyectoPorId;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMABuscarReportePorId;

public class PublicacionRepository {

    public Reporte buscarReportePorId(int id) {
        DMABuscarReportePorId BRPI = new DMABuscarReportePorId(id);
        BRPI.execute();
        try {
            return BRPI.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Proyecto buscarProyectoPorId(int id) {
        DMABuscarPoyectoPorId BPPI = new DMABuscarPoyectoPorId(id);
        BPPI.execute();
        try {
            return BPPI.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
