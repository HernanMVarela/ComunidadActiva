package frgp.utn.edu.ar.controllers.data.repository.informesModerador;

import org.json.JSONArray;

import java.util.Date;

import frgp.utn.edu.ar.controllers.data.remote.informesModerador.DMAListarDenunciasCerradas;
import frgp.utn.edu.ar.controllers.data.remote.informesModerador.DMAListarDenunciasAtendidas;
import frgp.utn.edu.ar.controllers.data.remote.informesModerador.DMAListarDenunciasPendientes;
import frgp.utn.edu.ar.controllers.data.remote.informesModerador.DMAListarPublicacionesEliminadas;
import frgp.utn.edu.ar.controllers.data.remote.informesModerador.DMAListarUsuariosSuspendidos;

public class InformesModeradorRepository {

    public JSONArray listarDenunciasCerradas(Date fechaInicio, Date fechaFin){
        DMAListarDenunciasCerradas DMAListaDenunciasCerradas = new DMAListarDenunciasCerradas(fechaInicio, fechaFin);
        DMAListaDenunciasCerradas.execute();
        try {
            return DMAListaDenunciasCerradas.get();
        } catch (Exception e) {
            return null;
        }
    }

    public JSONArray listarDenunciasAtendidas(Date fechaInicio, Date fechaFin){
        DMAListarDenunciasAtendidas DMAListaDenunciasAtendidadas = new DMAListarDenunciasAtendidas(fechaInicio, fechaFin);
        DMAListaDenunciasAtendidadas.execute();
        try {
            return DMAListaDenunciasAtendidadas.get();
        } catch (Exception e) {
            return null;
        }
    }

    public JSONArray listarDenunciasPendientes(Date fechaInicio, Date fechaFin){
        DMAListarDenunciasPendientes DMAListaDenunciasPendientes = new DMAListarDenunciasPendientes(fechaInicio, fechaFin);
        DMAListaDenunciasPendientes.execute();
        try {
            return DMAListaDenunciasPendientes.get();
        } catch (Exception e) {
            return null;
        }
    }

    public JSONArray listaPublicacionesEliminadas(Date fechaInicio, Date fechaFin){
        DMAListarPublicacionesEliminadas DMAListaPublicacionesEliminadas = new DMAListarPublicacionesEliminadas(fechaInicio, fechaFin);
        DMAListaPublicacionesEliminadas.execute();
        try {
            return DMAListaPublicacionesEliminadas.get();
        } catch (Exception e) {
            return null;
        }
    }

    public JSONArray listaUsuariosSuspendidos(Date fechaInicio, Date fechaFin){
        DMAListarUsuariosSuspendidos DMAListaUsuariosSuspendidos = new DMAListarUsuariosSuspendidos(fechaInicio, fechaFin);
        DMAListaUsuariosSuspendidos.execute();
        try {
            return DMAListaUsuariosSuspendidos.get();
        } catch (Exception e) {
            return null;
        }
    }

}
