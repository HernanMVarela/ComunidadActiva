package frgp.utn.edu.ar.DAOImpl.Connector;

public class DataDB {
    //Información de la BD
    public static String host="sql10.freesqldatabase.com";
    public static String port="3306";
    public static String nameBD="sql10646408";
    public static String user="sql10646408";
    public static String pass="dfkdmhuVy8";

    //Información para la conexion
    public static String urlMySQL = "jdbc:mysql://" + host + ":" + port + "/"+nameBD;
    public static String driver = "com.mysql.jdbc.Driver";



}
