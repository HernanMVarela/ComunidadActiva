package frgp.utn.edu.ar.controllers.data.remote.denuncia;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import frgp.utn.edu.ar.controllers.data.model.DenunciaNuevo;
import frgp.utn.edu.ar.controllers.data.model.EstadoDenuncia;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.ui.adapters.ListarDenunciaAdapter;


public class DMAListarDenunciasReporte extends AsyncTask<String, Void, String> {
    private Context context;
    private ListView listado;
    private static String result2;
    private static List<DenunciaNuevo> listaDenuncias;

    public DMAListarDenunciasReporte(ListView listview, Context ct){
        listado = listview;
        context = ct;
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();

            String query = "SELECT " +
                    "D.ID_REPORTE AS IDReporte, " +
                    "D.ID_USER AS IDUser, " +
                    "D.ID_ESTADO AS IDEstado, " +
                    "D.TITULO AS Titulo, " +
                    "D.DESCRIPCION AS Descripcion, " +
                    "D.FECHA_CREACION AS FechaCreacion, " +
                    "ED.ESTADO AS EstadoDenuncia, " +
                    "U.USERNAME AS UsernameUsuario, " +
                    "U.NOMBRE AS NombreUsuario, " +
                    "U.APELLIDO AS ApellidoUsuario, " +
                    "U.TELEFONO AS TelefonoUsuario, " +
                    "U.CORREO AS CorreoUsuario, " +
                    "U.FECHA_NAC AS FechaNacimientoUsuario, " +
                    "U.CREACION AS FechaCreacionUsuario " +
                    "FROM DENUNCIAS_REPORTES AS D " +
                    "INNER JOIN REPORTES AS R ON D.ID_REPORTE = R.ID " +
                    "INNER JOIN USUARIOS AS U ON D.ID_USER = U.ID " +
                    "INNER JOIN ESTADOS_DENUNCIA AS ED ON D.ID_ESTADO = ED.ID " +
                    "ORDER BY FECHA_CREACION;";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            result2 = " ";
            listaDenuncias = new ArrayList<DenunciaNuevo>();

            while (rs.next()) {
                DenunciaNuevo denuncia = new DenunciaNuevo();
                denuncia.setDescripcion(rs.getString("Descripcion"));
                denuncia.setTitulo(rs.getString("Titulo"));

                denuncia.setId(rs.getInt("IDReporte"));
                denuncia.setFecha_creacion(rs.getDate("FechaCreacion"));

                Usuario user = new Usuario();
                user.setId(rs.getInt("IDUser"));
                user.setUsername(rs.getString("UsernameUsuario"));
                user.setNombre(rs.getString("NombreUsuario"));
                user.setApellido(rs.getString("ApellidoUsuario"));
                user.setTelefono(rs.getString("TelefonoUsuario"));
                user.setCorreo(rs.getString("CorreoUsuario"));
                user.setFecha_nac(rs.getDate("FechaNacimientoUsuario"));
                user.setFecha_alta(rs.getDate("FechaCreacionUsuario"));

                EstadoDenuncia estadoDenuncia = new EstadoDenuncia(rs.getInt("IDEstado"), rs.getString("EstadoReporte"));

                denuncia.setEstado(estadoDenuncia);
                denuncia.setDenunciante(user);

                listaDenuncias.add(denuncia);
            }
            response = "Conexion exitosa";

        }catch (Exception e){
            e.printStackTrace();
            result2 = "Conexion no exitosa";
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        ListarDenunciaAdapter adapter = new ListarDenunciaAdapter(context, listaDenuncias);
        assert listaDenuncias != null;

        listado.setAdapter(adapter);
    }


}
