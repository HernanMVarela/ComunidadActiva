package frgp.utn.edu.ar.controllers.ui.adapters;

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
import frgp.utn.edu.ar.controllers.data.model.Notificacion;

public class ListaNotificacionesAdapter extends ArrayAdapter<Notificacion> {
    public ListaNotificacionesAdapter(Context context, List<Notificacion> listaNotificaciones) {
        super(context, R.layout.layout_list_notificaciones, listaNotificaciones);
        setDropDownViewResource(R.layout.layout_list_notificaciones);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Notificacion notificacion = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_notificaciones, parent,false);
        }

        TextView descripcion = convertView.findViewById(R.id.tvDescripcionNotificacion);
        TextView fecha = convertView.findViewById(R.id.tvFechaNotificacion);
        assert notificacion != null;

        descripcion.setText(notificacion.getDescripcion());
        String timeStamp = new Timestamp(notificacion.getFecha().getTime()).toString();
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
