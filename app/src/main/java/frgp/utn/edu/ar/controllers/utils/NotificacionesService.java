package frgp.utn.edu.ar.controllers.utils;

import java.util.Calendar;

import frgp.utn.edu.ar.controllers.data.model.Notificacion;
import frgp.utn.edu.ar.controllers.data.repository.notificacion.NotificacionRepository;

public class NotificacionesService {
    NotificacionRepository notificacionesRepository = new NotificacionRepository();
    public void notificacion(int idUser, String descripcion) {
        Notificacion notificacion = new Notificacion(0, idUser, descripcion, Calendar.getInstance().getTime(), false);
        notificacionesRepository.agregarNotificacion(notificacion);
    }
}
