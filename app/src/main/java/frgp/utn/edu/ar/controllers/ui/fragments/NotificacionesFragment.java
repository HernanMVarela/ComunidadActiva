package frgp.utn.edu.ar.controllers.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.util.List;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Notificacion;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.repository.notificacion.NotificacionRepository;
import frgp.utn.edu.ar.controllers.databinding.FragmentNotificacionesBinding;
import frgp.utn.edu.ar.controllers.ui.adapters.ListaActividadRecienteAdapter;
import frgp.utn.edu.ar.controllers.ui.adapters.ListaNotificacionesAdapter;
import frgp.utn.edu.ar.controllers.ui.viewmodels.NotificacionesViewModel;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class NotificacionesFragment extends Fragment {

    private FragmentNotificacionesBinding binding;
    private SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private NotificacionRepository notificacionesRepository = new NotificacionRepository();
    private List<Notificacion> listaNotificaciones;
    private Usuario usuario;
    private ListView listaNotificacionesNoLeidas;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificacionesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        usuario = sharedPreferences.getUsuarioData(this.getContext());
        listaNotificaciones = notificacionesRepository.listarNotificacionesNoLeidasPorId(usuario.getId());
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(listaNotificaciones.size() == 0) {
            Toast.makeText(this.getContext(), "No posee notificaciones pendientes", Toast.LENGTH_SHORT).show();
        }

        ListaNotificacionesAdapter adapter = new ListaNotificacionesAdapter(this.getContext(), listaNotificaciones);
        listaNotificacionesNoLeidas = view.findViewById(R.id.listNotificaciones);
        listaNotificacionesNoLeidas.setAdapter(adapter);

        listaNotificacionesNoLeidas.setOnItemClickListener((parent, view1, position, id) -> {
            Notificacion notificacion = listaNotificaciones.get(position);
            notificacion.setLectura(true);
            notificacionesRepository.modificarNotificacion(notificacion);
            listaNotificaciones.remove(position);
            adapter.notifyDataSetChanged();

            if(listaNotificaciones.size() == 0) {
                Toast.makeText(this.getContext(), "No posee notificaciones pendientes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}