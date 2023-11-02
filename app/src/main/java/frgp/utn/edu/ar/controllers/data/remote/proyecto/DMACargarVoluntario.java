package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Voluntario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMACargarVoluntario extends AsyncTask<String, Void, Voluntario> {

    private int idUserN, idProyectoN;
    private int filasafectadas;

    public DMACargarVoluntario(int idUser, int idProyecto)
    {
        idUserN=idUser;
        idProyectoN=idProyecto;
    }

    @Override
    protected Voluntario doInBackground(String... urls) {

        try {
            filasafectadas = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            String query = "SELECT U.ID, U.USERNAME, U.PUNTUACION, U.NOMBRE, U.APELLIDO, U.TELEFONO, U.CORREO, U.FECHA_NAC, " +
                    "U.CREACION, E.ID AS ID_ESTADO, E.ESTADO, T.ID AS ID_TIPO, T.TIPO, UP.FECHA_UNION, UP.FECHA_SALIDA " +
                    "FROM USUARIOS U INNER JOIN USERS_PROYECTO UP ON U.ID = UP.ID_USER " +
                    "INNER JOIN ESTADOS_USUARIO E ON U.ID_ESTADO = E.ID " +
                    "INNER JOIN TIPOS_USUARIO T ON U.ID_TIPO = T.ID WHERE U.ID = ? AND UP.ID_PROYECTO = ?";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, idUserN);
            preparedStatement.setInt(2, idProyectoN);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Voluntario voluntario = new Voluntario();
                voluntario.setId(resultSet.getInt("ID"));
                voluntario.setUsername(resultSet.getString("USERNAME"));
                voluntario.setPuntuacion(resultSet.getInt("PUNTUACION"));
                voluntario.setNombre(resultSet.getString("NOMBRE"));
                voluntario.setApellido(resultSet.getString("APELLIDO"));
                voluntario.setTelefono(resultSet.getString("TELEFONO"));
                voluntario.setCorreo(resultSet.getString("CORREO"));
                voluntario.setFecha_nac(resultSet.getDate("FECHA_NAC"));
                voluntario.setFecha_alta(resultSet.getDate("CREACION"));
                voluntario.setEstado(new EstadoUsuario(resultSet.getInt("ID_ESTADO"), resultSet.getString("ESTADO")));
                voluntario.setTipo(new TipoUsuario(resultSet.getInt("ID_TIPO"),resultSet.getString("TIPO")));
                voluntario.setFecha_union(resultSet.getDate("FECHA_UNION"));
                voluntario.setFecha_salida(resultSet.getDate("FECHA_SALIDA"));
                voluntario.setActivo(voluntario.getFecha_salida()==null);
                preparedStatement.close();
                con.close();
                return voluntario;
            }else{
                preparedStatement.close();
                con.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}