package frgp.utn.edu.ar.controllers.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.databinding.FragmentHomeBinding;
import frgp.utn.edu.ar.controllers.ui.viewmodels.HomeViewModel;
import frgp.utn.edu.ar.controllers.ui.adapters.GenMenuGridAdapter;
import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;
import frgp.utn.edu.ar.controllers.data.repository.CustomMenuItem ;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    SharedPreferencesService sharedPreferences = new SharedPreferencesService();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Usuario usuario = sharedPreferences.getUsuarioData(getContext());
        String userType = usuario.getTipo().getTipo();

        List<CustomMenuItem > menuItems = generateMenuItems(userType);
        GenMenuGridAdapter adapter = new GenMenuGridAdapter(getContext(), menuItems);
        GridView gridView = root.findViewById(R.id.gvMenuGeneral);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int destinationId = menuItems.get(i).getDestinationId();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(destinationId);
            }
        });

        return root;
    }

    private List<CustomMenuItem > generateMenuItems(String userType) {
        List<CustomMenuItem > menuItems = new ArrayList<>();

        switch (userType) {
            case "VECINO":
                menuItems.add(new CustomMenuItem (R.drawable.genmenu_reportes_24, "Crear reporte", R.id.action_nav_home_to_nav_nuevo_reporte));
                menuItems.add(new CustomMenuItem (R.drawable.genmenu_proyectos_24, "Crear proyecto", R.id.action_nav_home_to_nav_nuevo_proyecto));
                menuItems.add(new CustomMenuItem (R.drawable.genmenu_search_reporte_24, "Buscar reporte", R.id.action_nav_home_to_nav_buscar_reporte));
                menuItems.add(new CustomMenuItem (R.drawable.genmenu_search_proyecto_24, "Buscar proyecto", R.id.action_nav_home_to_nav_buscar_proyecto));

                break;
            case "MODERADOR":
                menuItems.add(new CustomMenuItem (R.drawable.genmenu_search_reporte_24, "Buscar reporte", R.id.action_nav_home_to_nav_buscar_reporte));
                menuItems.add(new CustomMenuItem (R.drawable.genmenu_search_proyecto_24, "Buscar proyecto", R.id.action_nav_home_to_nav_buscar_proyecto));
                menuItems.add(new CustomMenuItem (R.drawable.genmenu_crear_informe_moderador_24, "Crear informe", R.id.action_nav_home_to_nav_crear_informe_moderador));
                menuItems.add(new CustomMenuItem (R.drawable.genmenu_historial_moderacion_24, "Historial Moderacion", R.id.action_nav_home_to_nav_historial_moderacion));
                menuItems.add(new CustomMenuItem (R.drawable.genmenu_listar_denuncias_24, "Listar Denuncias", R.id.action_nav_home_to_nav_listar_denuncias));

                break;
            case "ADMINISTRADOR":

                menuItems.add(new CustomMenuItem (R.drawable.genmenu_reportes_24, "Crear reporte", R.id.action_nav_home_to_nav_nuevo_reporte));
                menuItems.add(new CustomMenuItem (R.drawable.genmenu_proyectos_24, "Crear proyecto", R.id.action_nav_home_to_nav_nuevo_proyecto));
                menuItems.add(new CustomMenuItem (R.drawable.genmenu_search_reporte_24, "Buscar reporte", R.id.action_nav_home_to_nav_buscar_reporte));
                menuItems.add(new CustomMenuItem (R.drawable.genmenu_search_proyecto_24, "Buscar proyecto", R.id.action_nav_home_to_nav_buscar_proyecto));
                menuItems.add(new CustomMenuItem (R.drawable.genmenu_crear_informe_admin_24, "Crear informe", R.id.action_nav_home_to_nav_crear_informe_admin));
                menuItems.add(new CustomMenuItem (R.drawable.genmenu_listar_usuarios_24, "Listar usuarios", R.id.action_nav_home_to_nav_listar_usuarios));
                menuItems.add(new CustomMenuItem (R.drawable.genmenu_listar_denuncias_24, "Listar denuncias", R.id.action_nav_home_to_nav_listar_denuncias));
                // Agrega otros elementos específicos para el tipo de usuario "ADMINISTRADOR"
                break;
            default:
                // Define un menú predeterminado para otros tipos de usuario
                break;
        }

        return menuItems;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
