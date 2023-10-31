package frgp.utn.edu.ar.controllers.data.repository.informesAdmin;

import org.json.JSONArray;


import java.util.Date;

import frgp.utn.edu.ar.controllers.data.remote.informesAdmin.DMAListarUsuariosActivosPorRol;

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
}
