package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMAListarDenunciaProyecto;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMAListarDenunciasReporte;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMASpinnerTiposDenuncia;
import frgp.utn.edu.ar.controllers.ui.activities.HomeActivity;
import frgp.utn.edu.ar.controllers.ui.viewmodels.ListarDenunciaViewModel;

public class ListarDenunciaFragment extends Fragment {

    private ListarDenunciaViewModel mViewModel;
    private ListView listaDenuncias;
    private Denuncia seleccionado = null;
    private View viewSeleccionado = null;
    List<Denuncia> denunciaList = new ArrayList<>();
    private Spinner spinTipoDenuncia;

    public static ListarDenunciaFragment newInstance() {
        return new ListarDenunciaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listar_denuncia, container, false);

        // ESCONDE EL BOTON DEL SOBRE
        if(getActivity() instanceof HomeActivity){
            ((HomeActivity) getActivity()).botonmensaje.hide();
        }

        spinTipoDenuncia = view.findViewById(R.id.spnTipoDenuncia);

        DMASpinnerTiposDenuncia dataActivityTiposDenuncia = new DMASpinnerTiposDenuncia(spinTipoDenuncia, getContext());
        dataActivityTiposDenuncia.execute();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnVerDenuncia = view.findViewById(R.id.btnVerDenuncia);

        listaDenuncias = view.findViewById(R.id.listDenuncias);

        spinTipoDenuncia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(spinTipoDenuncia.getSelectedItem().toString().equals("1 - REPORTE")){
                    Toast.makeText(getContext(), "ENTRE A REPORTE", Toast.LENGTH_LONG).show();
                    DMAListarDenunciasReporte DMAListaDenuncias = new DMAListarDenunciasReporte(listaDenuncias,view.getContext());
                    DMAListaDenuncias.execute();
                }
                if(spinTipoDenuncia.getSelectedItem().toString().equals("2 - PROYECTO")){

                    DMAListarDenunciaProyecto DMAListaDenuncias = new DMAListarDenunciaProyecto(listaDenuncias,view.getContext());
                    DMAListaDenuncias.execute();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listaDenuncias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Restaura el fondo del elemento previamente seleccionado
                if (viewSeleccionado != null) {
                    viewSeleccionado.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_500));
                }

                // Almacena el informe seleccionado en una variable
                seleccionado = (Denuncia) adapterView.getItemAtPosition(i);
                // Cambia el fondo del elemento seleccionado
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));
                // Almacena la vista del elemento seleccionado actualmente
                viewSeleccionado = view;
            }
        });

        btnVerDenuncia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                navegarDetalle();
            }
        });


    }

    public void navegarDetalle(){
        if(seleccionado != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("selected_denuncia", seleccionado);
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_ver_denuncia, bundle);
        }else {
            Toast.makeText(this.getContext(), "Debes seleccionar una Denuncia", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListarDenunciaViewModel.class);
        // TODO: Use the ViewModel
    }

}