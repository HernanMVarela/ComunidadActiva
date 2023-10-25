package frgp.utn.edu.ar.controllers.data.repository.log;

import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.Logs;
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
        DMAListarLogsPorUsuario DMAListarLogsPorUsuario = new DMAListarLogsPorUsuario(userId);
        DMAListarLogsPorUsuario.execute();
        try {
            return DMAListarLogsPorUsuario.get();
        } catch (Exception e) {
            return null;
        }
    }
}
