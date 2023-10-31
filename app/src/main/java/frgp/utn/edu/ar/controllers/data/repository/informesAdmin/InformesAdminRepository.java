package frgp.utn.edu.ar.controllers.data.repository.informesAdmin;

import org.json.JSONArray;


import java.util.Date;

import frgp.utn.edu.ar.controllers.data.remote.informesAdmin.DMAListarReportesPorCategoria;
import frgp.utn.edu.ar.controllers.data.remote.informesAdmin.DMAListarUsuariosActivosPorRol;
import frgp.utn.edu.ar.controllers.data.remote.informesAdmin.DMAListarUsuariosNuevosRegistrados;
import frgp.utn.edu.ar.controllers.data.remote.informesAdmin.DMAListarUsuariosPorEstado;

public class InformesAdminRepository {

    public JSONArray listarUsuariosActivosPorRol(Date fechaInicio, Date fechaFin) {
        DMAListarUsuariosActivosPorRol DMAUAPR = new DMAListarUsuariosActivosPorRol(fechaInicio, fechaFin);
        DMAUAPR.execute();
        try {
            return DMAUAPR.get();
        } catch (Exception e) {
            return null;
        }
    }

    public JSONArray listarUsuariosNuevosRegistrados(Date fechaInicio, Date fechaFin) {
        DMAListarUsuariosNuevosRegistrados DMAUNR = new DMAListarUsuariosNuevosRegistrados(fechaInicio, fechaFin);
        DMAUNR.execute();
        try {
            return DMAUNR.get();
        } catch (Exception e) {
            return null;
        }
    }

    public JSONArray listarUsuariosPorEstado(Date fechaInicio, Date fechaFin) {
        DMAListarUsuariosPorEstado DMAUPE = new DMAListarUsuariosPorEstado(fechaInicio, fechaFin);
        DMAUPE.execute();
        try {
            return DMAUPE.get();
        } catch (Exception e) {
            return null;
        }
    }

    public JSONArray listarReportesPorCategoria(Date fechaInicio, Date fechaFin) {
        DMAListarReportesPorCategoria DMAUPE = new DMAListarReportesPorCategoria(fechaInicio, fechaFin);
        DMAUPE.execute();
        try {
            return DMAUPE.get();
        } catch (Exception e) {
            return null;
        }
    }
}
