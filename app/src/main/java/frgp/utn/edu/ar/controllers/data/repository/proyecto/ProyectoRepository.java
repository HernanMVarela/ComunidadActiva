package frgp.utn.edu.ar.controllers.data.repository.proyecto;

import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAActualizarEstadoProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMABuscarProyectoPorId;

public class ProyectoRepository {

    public boolean actualizarEstadoProyecto(Proyecto proyecto) {
        try {
            DMAActualizarEstadoProyecto DMAActualizarProyecto = new DMAActualizarEstadoProyecto(proyecto);
            DMAActualizarProyecto.execute();
            return DMAActualizarProyecto.get();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Proyecto buscarProyectoPorId(int id) {
        DMABuscarProyectoPorId BPPI = new DMABuscarProyectoPorId(id);
        BPPI.execute();
        try {
            return BPPI.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
