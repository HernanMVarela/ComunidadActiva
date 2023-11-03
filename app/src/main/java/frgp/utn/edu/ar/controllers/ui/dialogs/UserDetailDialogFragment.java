package frgp.utn.edu.ar.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Usuario;

public class UserDetailDialogFragment extends DialogFragment {
    private TextView username, nombre, puntaje, nacimiento, creacion;

    // Método estático para crear una instancia del fragmento con argumentos
    public static UserDetailDialogFragment newInstance(Usuario user) {
        UserDetailDialogFragment fragment = new UserDetailDialogFragment();

        Bundle args = new Bundle();
        args.putString("username", user.getUsername());
        args.putString("nombre", user.getNombre() + " " + user.getApellido());
        args.putString("puntaje", "Puntaje: " + user.getPuntuacion());
        args.putString("nacimiento", "Nacimiento: " + user.getFecha_nac());
        args.putString("creacion", "Miembro desde el " + user.getFecha_alta());
        fragment.setArguments(args);
        return fragment;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_datos_usuario_reporte, null);

        username = dialogView.findViewById(R.id.tvUsernameDetalleReporte);
        nombre = dialogView.findViewById(R.id.tvNombreDetalleReporte);
        puntaje = dialogView.findViewById(R.id.tvPuntajeDetalleReporte);
        nacimiento = dialogView.findViewById(R.id.tvFechaNacDetalleReporte);
        creacion = dialogView.findViewById(R.id.tvFechaCreacionDetalleReporte);

        // Obtén los datos del usuario de los argumentos
        Bundle args = getArguments();
        if (args != null) {
            String username = args.getString("username");
            String nombre = args.getString("nombre");
            String puntaje = args.getString("puntaje");
            String nacimiento = args.getString("nacimiento");
            String creacion = args.getString("creacion");

            // Llena las vistas con los datos del usuario
            this.username.setText(username);
            this.nombre.setText(nombre);
            this.puntaje.setText(puntaje);
            this.nacimiento.setText(nacimiento);
            this.creacion.setText(creacion);
        }

        builder.setView(dialogView);
        return builder.create();
    }
}
