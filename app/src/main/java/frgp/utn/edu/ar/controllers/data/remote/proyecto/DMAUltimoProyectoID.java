package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAUltimoProyectoID extends AsyncTask<String, Void, Integer> {

    @Override
    protected Integer doInBackground(String... urls) {
        int proyectoId = -1;

        try {
            // Realiza la inserción del proyecto
            // ...

            // Consulta para obtener el último ID insertado
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            String query = "SELECT MAX(ID) FROM PROYECTOS";

            ResultSet resultSet = st.executeQuery(query);

            if (resultSet.next()) {
                proyectoId = resultSet.getInt(1);
            }

            resultSet.close();
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            proyectoId = -1;
        }

        return proyectoId;
    }
}
