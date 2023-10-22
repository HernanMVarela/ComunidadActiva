package frgp.utn.edu.ar.controllers.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.repository.CustomMenuItem ;

import java.util.List;

public class GenMenuGridAdapter extends BaseAdapter {
    private List<CustomMenuItem > menuItems;
    private Context context;

    public GenMenuGridAdapter(Context context, List<CustomMenuItem > menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.grid_layout_menu_general, null);
            TextView textView = gridView.findViewById(R.id.gvGenMenu_titulo);
            ImageView imageView = gridView.findViewById(R.id.gvGenMenu_imagen);

            CustomMenuItem  menuItem = menuItems.get(position);
            textView.setText(menuItem.getTitle());
            imageView.setImageResource(menuItem.getIconId());
        } else {
            gridView = convertView;
        }

        return gridView;
    }
}
