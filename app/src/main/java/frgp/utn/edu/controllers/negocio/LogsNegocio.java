package frgp.utn.edu.controllers.negocio;

import android.content.Context;

import java.util.Date;
import java.util.List;

import frgp.utn.edu.controllers.data.model.Logs;
import frgp.utn.edu.controllers.utils.LogsEnum;

public interface LogsNegocio {
    boolean agregarLog(Context context, Logs nuevo);
    List<Logs> buscarLogsPorUser(Context context, int idUser);
    List<Logs> buscarLogsPorUserEntreFechas(Context context, int idUser, Date fechaDesde, Date fechaHasta);
    List<Logs> buscarLogsPorUserPorAccion(Context context, LogsEnum accion, int idUser);
    List<Logs> buscarLogPorFecha(Context context, Date fecha);
    List<Logs> buscarLogsEntreFechas(Context context, Date fechaDesde, Date fechaHasta);
    List<Logs> buscarLogsPorAccion(Context context, LogsEnum accion);
}
