package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.model.Voluntario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMABuscarUsuarioEnProyecto extends AsyncTask<String, Void, Boolean> {
    private static String resultado;
    private int idUserN, idProyectoN;
    public DMABuscarUsuarioEnProyecto(int isUserB, int idProyectoB) {
        idProyectoN = idProyectoB;
        idUserN=isUserB;
    }
    @Override
    protected Boolean doInBackground(String... urls) {
        try {
            Voluntario user = null;
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String query = "SELECT ID_USER, ID_PROYECTO, FECHA_SALIDA FROM USERS_PROYECTO WHERE ID_USER = ? AND ID_PROYECTO = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1,idUserN);
            preparedStatement.setInt(2,idProyectoN);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                user = new Voluntario();
                if(rs.getDate("FECHA_SALIDA")!=null){
                    user.setActivo(false);
                }else{
                    user.setActivo(true);
                }
            }
            rs.close();
            con.close();
            return user.isActivo();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
