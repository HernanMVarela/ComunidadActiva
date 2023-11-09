package frgp.utn.edu.ar.controllers.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Logs;

public class ListaHistorialDeModeracionAdapter extends ArrayAdapter<Logs> {
    public ListaHistorialDeModeracionAdapter(Context context, List<Logs> listaLogs) {
        super(context, R.layout.layout_list_item_actividad_reciente, listaLogs);
        setDropDownViewResource(R.layout.layout_list_item_actividad_reciente);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Logs log = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_item_actividad_reciente, parent,false);
        }

        TextView titulo = convertView.findViewById(R.id.tvTituloActividad);
        TextView descripcion = convertView.findViewById(R.id.tvDescripcionActividadReciente);
        TextView fecha = convertView.findViewById(R.id.tvFechaActividadReciente);
        assert log != null;

        ///FOrmat LOGENUM to take out _character
        String[] accion = log.getAccion().toString().split("_");
        String accionFinal = "";
        for (String s : accion) {
            accionFinal += s + " ";
        }
        titulo.setText(accionFinal);
        descripcion.setText(log.getDescripcion());

        String timeStamp = new Timestamp(log.getFecha().getTime()).toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(timeStamp);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        sdf = new SimpleDateFormat("d/MM/yyyy h:m a");
        fecha.setText(sdf.format(date));

        return convertView;
    }
}
