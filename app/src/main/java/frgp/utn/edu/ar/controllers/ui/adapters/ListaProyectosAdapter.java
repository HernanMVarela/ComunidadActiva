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
import frgp.utn.edu.ar.controllers.data.model.Proyecto;

public class ListaProyectosAdapter extends ArrayAdapter<Proyecto> {

    private Context context;
    private List<Proyecto> proyectos;

    public ListaProyectosAdapter(Context context, List<Proyecto> listaProyecto) {
        super(context, R.layout.layout_lista_proyecto_unidad, listaProyecto);
        setDropDownViewResource(R.layout.layout_lista_proyecto_unidad);
        this.context=context;
        this.proyectos=listaProyecto;
    }
    public int getCount() {
        return proyectos.size();
    }

    @Override
    public Proyecto getItem(int position) {
        return proyectos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Proyecto proyecto = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_lista_proyecto_unidad, parent,false);
        }
        TextView titulo = convertView.findViewById(R.id.txtTituloProyecto);
        TextView datos = convertView.findViewById(R.id.txDatosProyecto);
        TextView telefono = convertView.findViewById(R.id.txTelefonoContacto);

        String timeStamp = new Timestamp(proyecto.getFecha().getTime()).toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(timeStamp);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        sdf = new SimpleDateFormat("d/MM/yyyy");

        titulo.setText(proyecto.getTitulo());
        datos.setText(String.format("Creado por %s el %s", proyecto.getOwner().getUsername(), sdf.format(date)));
        telefono.setText("Telefono: " + proyecto.getContacto());
        return convertView;
    }

}
