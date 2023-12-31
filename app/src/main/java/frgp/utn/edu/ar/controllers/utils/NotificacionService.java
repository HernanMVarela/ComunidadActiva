package frgp.utn.edu.ar.controllers.utils;

import java.sql.Timestamp;
import java.util.Calendar;

import frgp.utn.edu.ar.controllers.data.model.Notificacion;
import frgp.utn.edu.ar.controllers.data.repository.notificacion.NotificacionRepository;

public class NotificacionService {
    private NotificacionRepository notificacionesRepository = new NotificacionRepository();
    public void notificacion(int idUser, String descripcion) {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdUser(idUser);
        notificacion.setDescripcion(descripcion);
        notificacion.setFecha(new Timestamp(Calendar.getInstance().getTime().getTime()));
        notificacion.setLectura(false);
        notificacionesRepository.agregarNotificacion(notificacion);
    }
}
