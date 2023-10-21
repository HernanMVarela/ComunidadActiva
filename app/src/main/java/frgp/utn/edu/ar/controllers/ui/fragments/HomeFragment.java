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

import frgp.utn.edu.ar.controllers.databinding.FragmentHomeBinding;
import frgp.utn.edu.ar.controllers.ui.viewmodels.HomeViewModel;
import frgp.utn.edu.ar.controllers.ui.adapters.GenMenuGridAdapter;
import frgp.utn.edu.ar.controllers.R;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        GenMenuGridAdapter adapter = new GenMenuGridAdapter(getContext());
        GridView gridView = root.findViewById(R.id.gvMenuGeneral);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                switch (i) {
                    case 0:
                        navController.navigate(R.id.action_nav_home_to_nav_nuevo_reporte);
                        break;
                    case 1:
                        navController.navigate(R.id.action_nav_home_to_nav_nuevo_proyecto);
                        break;
                    case 2:
                        navController.navigate(R.id.action_nav_home_to_nav_buscar_reporte);
                        break;
                    case 3:
                        navController.navigate(R.id.action_nav_home_to_nav_buscar_proyecto);
                        break;
                    case 4:
                        navController.navigate(R.id.action_nav_home_to_nav_crear_informe_admin);
                        break;
                    case 5:
                        navController.navigate(R.id.action_nav_home_to_nav_listar_usuarios);
                        break;
                    case 6:
                        navController.navigate(R.id.action_nav_home_to_nav_crear_informe_moderador);
                        break;
                    case 7:
                        navController.navigate(R.id.action_nav_home_to_nav_historial_moderacion);
                        break;
                    case 8:
                        navController.navigate(R.id.action_nav_home_to_nav_listar_denuncias);
                        break;
                    default:
                        break;
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}