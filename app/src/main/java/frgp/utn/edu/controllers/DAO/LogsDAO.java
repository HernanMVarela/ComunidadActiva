package frgp.utn.edu.controllers.DAO;

import android.content.Context;

import java.util.Date;
import java.util.List;

import frgp.utn.edu.controllers.data.model.Logs;
import frgp.utn.edu.controllers.utils.LogsEnum;

public interface LogsDAO {
    boolean agregarLog(Context context, Logs logs);
    List<Logs> listarLogsPorUser(Context context, int idUser);
    List<Logs> listarLogsPorUserEntreFechas(Context context, int idUser, Date fechaDesde, Date fechaHasta);
    List<Logs> listarLogsPorUserPorAccion(Context context, LogsEnum accion, int idUser);
    List<Logs> listarLogPorFecha(Context context, Date fecha);
    List<Logs> listarLogsEntreFechas(Context context, Date fechaDesde, Date fechaHasta);
    List<Logs> listarLogsPorAccion(Context context, LogsEnum accion);
}
