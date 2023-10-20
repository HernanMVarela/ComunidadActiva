package frgp.utn.edu.ar.controllers.utils;

import java.util.Calendar;
import java.util.Date;

import frgp.utn.edu.ar.controllers.data.model.Logs;
import frgp.utn.edu.ar.controllers.data.repository.log.LogRepository;

public class LogService {
    LogRepository logRepository = new LogRepository();
    public void log(int idUser, LogsEnum accion, String descripcion) {
        Logs log = new Logs(0, idUser, accion, descripcion, Calendar.getInstance().getTime());
        logRepository.agregarLog(log);
    }
}
