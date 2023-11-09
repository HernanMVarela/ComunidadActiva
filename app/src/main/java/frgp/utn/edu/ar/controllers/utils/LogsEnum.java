package frgp.utn.edu.ar.controllers.utils;

public enum LogsEnum {

    ///ACCIONES COMUNES
    LOGIN,
    LOGOUT,
    REGISTRO_USUARIO,
    BLOQUEO_USUARIO,
    DESBLOQUEO_USUARIO,
    RECUPERO_PASS,
    CAMBIO_PASS,

    ///ACCIONES DE ADMINSTRACION DE USUARIOS
    ACTIVACION_USUARIO,
    MODIFICACION_USUARIO,
    ELIMINACION_USUARIO,

    ///ACCIONES REPORTE
    CREACION_REPORTE,
    SOLICITUD_CIERRE_REPORTE,
    CIERRE_REPORTE,
    REAPERTURA_REPORTE,
    VALORAR_REPORTE,
    DENUNCIA_REPORTE,

    ///ACCIONES PROYECTO
    CREACION_PROYECTO,


    ///ACCIONES INFORME
    CREACION_INFORME,

    ///ACCIONES MODERACION
    SUSPENSION_USUARIO,
    ELIMINAR_PUBLICACION,
    CERRAR_DENUNCIA_Y_NOTIFICAR,


}
