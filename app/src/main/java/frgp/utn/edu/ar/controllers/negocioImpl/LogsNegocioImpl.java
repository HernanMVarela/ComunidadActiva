package frgp.utn.edu.ar.controllers.negocioImpl;

import android.content.Context;

import java.util.Date;
import java.util.List;

import frgp.utn.edu.ar.controllers.DAO.LogsDAO;
import frgp.utn.edu.ar.controllers.DAOImpl.Logs.LogsDAOImpl;
import frgp.utn.edu.ar.controllers.negocio.LogsNegocio;
import frgp.utn.edu.ar.controllers.data.model.Logs;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;

public class LogsNegocioImpl implements LogsNegocio {
    private LogsDAO logsDAO = new LogsDAOImpl();

    @Override
    public List<Logs> buscarLogsPorUser(Context context, int idUser) {
        return  logsDAO.listarLogsPorUser(context, idUser);
    }

    @Override
    public List<Logs> buscarLogsPorUserEntreFechas(Context context, int idUser, Date fechaDesde, Date fechaHasta) {
        return logsDAO.listarLogsPorUserEntreFechas(context, idUser, fechaDesde, fechaHasta);
    }

    @Override
    public List<Logs> buscarLogsPorUserPorAccion(Context context, LogsEnum accion, int idUser) {
        return logsDAO.listarLogsPorUserPorAccion(context, accion, idUser);
    }

    @Override
    public List<Logs> buscarLogPorFecha(Context context, Date fecha) {
        return logsDAO.listarLogPorFecha(context, fecha);
    }

    @Override
    public List<Logs> buscarLogsEntreFechas(Context context, Date fechaDesde, Date fechaHasta) {
        return logsDAO.listarLogsEntreFechas(context, fechaDesde, fechaHasta);
    }

    @Override
    public List<Logs> buscarLogsPorAccion(Context context, LogsEnum accion) {
        return logsDAO.listarLogsPorAccion(context, accion);
    }
}
