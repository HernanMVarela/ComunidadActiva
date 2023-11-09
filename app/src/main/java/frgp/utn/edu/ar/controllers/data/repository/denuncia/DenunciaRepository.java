package frgp.utn.edu.ar.controllers.data.repository.denuncia;

import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.Denuncia;

import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMAActualizarEstadoDenunciaProyecto;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMAListarDenunciasProyecto;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMAListarDenunciasReporte;

public class DenunciaRepository {

    public List<Denuncia> listarDenunciasReportes() {
        DMAListarDenunciasReporte LDR = new DMAListarDenunciasReporte();
        LDR.execute();
        try {
            return LDR.get();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Denuncia> listarDenunciasProyectos() {
        DMAListarDenunciasProyecto LDP = new DMAListarDenunciasProyecto();
        LDP.execute();
        try {
            return LDP.get();
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean cambiarEstadoDenunciaProyecto(Denuncia modificar) {
        DMAActualizarEstadoDenunciaProyecto AEDP = new DMAActualizarEstadoDenunciaProyecto(modificar);
        AEDP.execute();
        try {
            return AEDP.get();
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean cambiarEstadoDenunciaReporte(Denuncia modificar) {
        DMAActualizarEstadoDenunciaProyecto AEDP = new DMAActualizarEstadoDenunciaProyecto(modificar);
        AEDP.execute();
        try {
            return AEDP.get();
        } catch (Exception e) {
            return false;
        }
    }
}
