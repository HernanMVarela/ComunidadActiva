package frgp.utn.edu.ar.controllers.DAOImpl.Logs;

import android.content.Context;

import java.util.Date;
import java.util.List;

import frgp.utn.edu.ar.controllers.DAO.LogsDAO;
import frgp.utn.edu.ar.controllers.data.model.Logs;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;

public class LogsDAOImpl implements LogsDAO {

    @Override
    public boolean agregarLog(Context context, Logs logs) {
        DMANuevoLog DMANL = new DMANuevoLog(logs, context);
        DMANL.execute();
        try {
            return DMANL.get();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Logs> listarLogsPorUser(Context context, int idUser) {
        DMABuscarLogsPorUsuario DMABLPU = new DMABuscarLogsPorUsuario(idUser, context);
        DMABLPU.execute();
        try {
            return DMABLPU.get();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Logs> listarLogsPorUserPorAccion(Context context, LogsEnum accion, int idUser) {
        DMABuscarLogsPorUsuarioPorAccion DMABLPUA = new DMABuscarLogsPorUsuarioPorAccion(idUser, context, accion);
        DMABLPUA.execute();
        try {
            return DMABLPUA.get();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Logs> listarLogsPorUserEntreFechas(Context context, int idUser, Date fechaDesde, Date fechaHasta) {
        DMABuscarLogsPorUsuarioEntreFechas DMABLPUEF = new DMABuscarLogsPorUsuarioEntreFechas(idUser, context, fechaDesde, fechaHasta);
        DMABLPUEF.execute();
        try {
            return DMABLPUEF.get();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Logs> listarLogPorFecha(Context context, Date fecha) {
        DMABuscarLogsPorFecha DMABLPF = new DMABuscarLogsPorFecha(context, fecha);
        DMABLPF.execute();
        try {
            return DMABLPF.get();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Logs> listarLogsEntreFechas(Context context, Date fechaDesde, Date fechaHasta) {
        DMABuscarLogsEntreFechas DMABLEF = new DMABuscarLogsEntreFechas(context, fechaDesde, fechaHasta);
        DMABLEF.execute();
        try {
            return DMABLEF.get();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Logs> listarLogsPorAccion(Context context, LogsEnum accion) {
        return null;
    }
}
