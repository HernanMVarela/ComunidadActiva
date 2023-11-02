package frgp.utn.edu.ar.controllers.utils;

import java.sql.Timestamp;
import java.util.Calendar;

import frgp.utn.edu.ar.controllers.data.model.Logs;
import frgp.utn.edu.ar.controllers.data.repository.log.LogRepository;

public class LogService {
    LogRepository logRepository = new LogRepository();
    public void log(int idUser, LogsEnum accion, String descripcion) {
        ///LOG WITH CURRENT DATE INCLUDING HOUR AND SECONDS
        Logs log = new Logs();
        log.setIdUser(idUser);
        log.setAccion(accion);
        log.setDescripcion(descripcion);
        log.setFecha(new Timestamp(Calendar.getInstance().getTime().getTime()));
        logRepository.agregarLog(log);
    }
}
