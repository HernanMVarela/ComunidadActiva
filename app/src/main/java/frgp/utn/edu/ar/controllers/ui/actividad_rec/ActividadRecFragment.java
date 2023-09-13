package frgp.utn.edu.ar.controllers.ui.actividad_rec;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import frgp.utn.edu.ar.controllers.databinding.FragmentActividadRecienteBinding;

public class ActividadRecFragment extends Fragment {

    private FragmentActividadRecienteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ActividadRecViewModel actividadRecViewModel =
                new ViewModelProvider(this).get(ActividadRecViewModel.class);

        binding = FragmentActividadRecienteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textActividad;
        actividadRecViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}