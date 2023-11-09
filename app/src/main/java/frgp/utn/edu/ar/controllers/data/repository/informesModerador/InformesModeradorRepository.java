package frgp.utn.edu.ar.controllers.data.repository.informesModerador;

import org.json.JSONArray;

import java.util.Date;

import frgp.utn.edu.ar.controllers.data.remote.informesModerador.DMAListarDenucniasCerradas;

public class InformesModeradorRepository {

    public JSONArray listarDenunciasCerradas(Date fechaInicio, Date fechaFin){
        DMAListarDenucniasCerradas DMAListaDenunciasCerradas = new DMAListarDenucniasCerradas(fechaInicio, fechaFin);
        DMAListaDenunciasCerradas.execute();
        try {
            return DMAListaDenunciasCerradas.get();
        } catch (Exception e) {
            return null;
        }
    }
}
