package frgp.utn.edu.ar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import frgp.utn.edu.ar.controllers.R;

public class GenMenuGridAdapter extends BaseAdapter {

    private final int[] imagenIDs ={R.drawable.genmenu_reportes_24,
            R.drawable.genmenu_proyectos_24,
            R.drawable.genmenu_search_reporte_24,
            R.drawable.genmenu_search_proyecto_24};
    private final String[] titulos = {"Reportes","Proyectos","Buscar Reporte","Buscar Proyecto"};
    private Context context;

    public GenMenuGridAdapter(Context context){
        this.context=context;
    }
    @Override
    public int getCount() {
        return imagenIDs.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if(view == null){
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.grid_layout_menu_general,null);
            TextView textView = gridView.findViewById(R.id.gvGenMenu_titulo);
            textView.setText(titulos[i]);
            ImageView imageView = gridView.findViewById(R.id.gvGenMenu_imagen);
            imageView.setImageResource(imagenIDs[i]);
        } else {
            gridView = view;
        }
        return gridView;
    }
}
