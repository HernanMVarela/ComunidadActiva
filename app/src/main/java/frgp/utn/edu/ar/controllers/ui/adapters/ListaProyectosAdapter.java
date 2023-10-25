package frgp.utn.edu.ar.controllers.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
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
        TextView titulo = convertView.findViewById(R.id.txtTituloProyectoU);
        TextView datos = convertView.findViewById(R.id.txtDatosCreadorU);
        titulo.setText(proyecto.getTitulo());
        titulo.setText(proyecto.getTitulo());
        datos.setText("Contactar al " + proyecto.getContacto());
        return convertView;
    }

}
