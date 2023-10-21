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

import frgp.utn.edu.ar.controllers.databinding.FragmentHomeVecinoBinding;
import frgp.utn.edu.ar.controllers.ui.viewmodels.HomeVecinoViewModel;
import frgp.utn.edu.ar.controllers.ui.adapters.GenMenuGridAdapter;
import frgp.utn.edu.ar.controllers.R;

public class HomeVecinoFragment extends Fragment {

    private FragmentHomeVecinoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeVecinoViewModel homeVecinoViewModel =
                new ViewModelProvider(this).get(HomeVecinoViewModel.class);

        binding = FragmentHomeVecinoBinding.inflate(inflater, container, false);
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
                        navController.navigate(R.id.action_nav_home_to_nav_panel_mod);
                        break;
                    case 5:
                        navController.navigate(R.id.action_nav_home_to_nav_panel_admin);
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