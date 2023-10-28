package frgp.utn.edu.ar.controllers.data.repository.notificacion;

import android.util.Log;

import java.util.List;
import java.util.Objects;

import frgp.utn.edu.ar.controllers.data.model.Notificacion;
import frgp.utn.edu.ar.controllers.data.remote.notificacion.DMAListarNotificacionesNoLeidasPorUsuario;
import frgp.utn.edu.ar.controllers.data.remote.notificacion.DMAModificarNotificacion;
import frgp.utn.edu.ar.controllers.data.remote.notificacion.DMANuevaNotificacion;

public class NotificacionRepository {
    public boolean agregarNotificacion(Notificacion notificacion) {
        DMANuevaNotificacion DMANN = new DMANuevaNotificacion(notificacion);
        DMANN.execute();
        try {
            return DMANN.get();
        } catch (Exception e) {
            return false;
        }
    }

    public List<Notificacion> listarNotificacionesNoLeidasPorId(int userId) {
        DMAListarNotificacionesNoLeidasPorUsuario DMALNLPU = new DMAListarNotificacionesNoLeidasPorUsuario(userId);
        DMALNLPU.execute();
        try {
            return DMALNLPU.get();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean modificarNotificacion(Notificacion modificado) {
        DMAModificarNotificacion DMANN = new DMAModificarNotificacion(modificado);
        DMANN.execute();
        try {
            return DMANN.get();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }
}
