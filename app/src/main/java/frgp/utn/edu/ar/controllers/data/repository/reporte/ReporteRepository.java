package frgp.utn.edu.ar.controllers.data.repository.reporte;

import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMAActualizarEstadoReporte;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMABuscarReportePorId;

public class ReporteRepository {
    public boolean actualizarEstadoReporte(Reporte reporte) {
        try {
            DMAActualizarEstadoReporte DMAActualizarReporte = new DMAActualizarEstadoReporte(reporte);
            DMAActualizarReporte.execute();
            return DMAActualizarReporte.get();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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
}
