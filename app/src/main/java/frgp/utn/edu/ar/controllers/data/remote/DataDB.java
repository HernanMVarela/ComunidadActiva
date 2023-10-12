package frgp.utn.edu.ar.controllers.data.remote;

public class DataDB {
    //Información de la BD
    public static String host="sql10.freesqldatabase.com";
    public static String port="3306";
    public static String nameBD="sql10652506";
    public static String user="sql10652506";
    public static String pass=null; // MODIFICAR CLAVE LOCALMENTE PARA EVITAR ALERTAS DE SEGURIDAD

    //Información para la conexion
    public static String urlMySQL = "jdbc:mysql://" + host + ":" + port + "/"+nameBD;
    public static String driver = "com.mysql.jdbc.Driver";



}
