package frgp.utn.edu.ar.controllers.data.repository.log;

import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.Logs;
import frgp.utn.edu.ar.controllers.data.remote.log.DMAListarLogsModeracionPorUsuario;
import frgp.utn.edu.ar.controllers.data.remote.log.DMAListarLogsPorUsuario;
import frgp.utn.edu.ar.controllers.data.remote.log.DMANuevoLog;

public class LogRepository {
    public boolean agregarLog(Logs logs) {
        DMANuevoLog DMANL = new DMANuevoLog(logs);
        DMANL.execute();
        try {
            return DMANL.get();
        } catch (Exception e) {
            return false;
        }
    }
    public List<Logs> listarLogsPorId(int userId) {
        DMAListarLogsPorUsuario DMALLPU = new DMAListarLogsPorUsuario(userId);
        DMALLPU.execute();
        try {
            return DMALLPU.get();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Logs> listarLogsModeracionPorId(int userId) {
        DMAListarLogsModeracionPorUsuario DMALLMPU = new DMAListarLogsModeracionPorUsuario(userId);
        DMALLMPU.execute();
        try {
            return DMALLMPU.get();
        } catch (Exception e) {
            return null;
        }
    }


}
