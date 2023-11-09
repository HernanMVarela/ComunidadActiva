package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.ui.dialogs.AyudaFAQDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.CerrarReporteDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.AyudaViewModel;

public class AyudaFragment extends Fragment {

    private AyudaViewModel mViewModel;
    private String titulo, subtitulo, paso_1, paso_2, paso_3, paso_4;

    public static AyudaFragment newInstance() {
        return new AyudaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ayuda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button pregunta1 = view.findViewById(R.id.acc_preg_uno);
        comportamiento_boton_preg_uno(pregunta1);
        Button pregunta2 = view.findViewById(R.id.acc_preg_dos);
        comportamiento_boton_preg_dos(pregunta2);
        Button pregunta3 = view.findViewById(R.id.acc_preg_tres);
        comportamiento_boton_preg_tres(pregunta3);
        Button pregunta4 = view.findViewById(R.id.acc_preg_cuatro);
        comportamiento_boton_preg_cuatro(pregunta4);
        Button pregunta5 = view.findViewById(R.id.acc_preg_cinco);
        comportamiento_boton_preg_cinco(pregunta5);
        Button pregunta6 = view.findViewById(R.id.acc_preg_seis);
        comportamiento_boton_preg_seis(pregunta6);
        Button pregunta7 = view.findViewById(R.id.acc_preg_siete);
        comportamiento_boton_preg_siete(pregunta7);
        Button pregunta8 = view.findViewById(R.id.acc_preg_ocho);
        comportamiento_boton_preg_ocho(pregunta8);
        Button pregunta9 = view.findViewById(R.id.acc_preg_nueve);
        comportamiento_boton_preg_nueve(pregunta9);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AyudaViewModel.class);
        // TODO: Use the ViewModel
    }

    private Bundle cargar_bundle(){
        Bundle bundle = new Bundle();
        bundle.putString("ayuda_titulo", titulo);
        bundle.putString("ayuda_subtitulo", subtitulo);
        bundle.putString("ayuda_paso1", paso_1);
        bundle.putString("ayuda_paso2", paso_2);
        bundle.putString("ayuda_paso3", paso_3);
        bundle.putString("ayuda_paso4", paso_4);
        return bundle;
    }
    private void comportamiento_boton_preg_uno(Button pregunta){
        pregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo= "¿Cómo hacer un reporte?";
                subtitulo = "Para hacer un reporte, sigue estos pasos:";
                paso_1 = "- Inicia sesión en tu cuenta.";
                paso_2 = "- Toca el botón \"Crear reporte\"";
                paso_3 = "- Completa los detalles del reporte y envíalo.";
                paso_4 = "";
                AyudaFAQDialogFragment dialogFragment = new AyudaFAQDialogFragment();
                dialogFragment.setArguments(cargar_bundle()); // Establece el Bundle como argumento
                dialogFragment.show(getFragmentManager(), "layout_cerrar_reporte");
            }
        });
    }

    private void comportamiento_boton_preg_dos(Button pregunta){
        pregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo= "¿Cómo valorar un reporte?";
                subtitulo = "Para valorar un reporte, sigue estos pasos:";
                paso_1 = "- Abre el reporte que deseas valorar.";
                paso_2 = "- Toca el botón \"Valorar\".";
                paso_3 = "- Asigna una calificación y presiona \"Aceptar\".";
                paso_4 = "";
                AyudaFAQDialogFragment dialogFragment = new AyudaFAQDialogFragment();
                dialogFragment.setArguments(cargar_bundle()); // Establece el Bundle como argumento
                dialogFragment.show(getFragmentManager(), "layout_cerrar_reporte");
            }
        });
    }

    private void comportamiento_boton_preg_tres(Button pregunta){
        pregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo= "¿Cómo atender un reporte?";
                subtitulo = "Para atender un reporte, sigue estos pasos:";
                paso_1 = "- Abre el reporte que deseas atender.";
                paso_2 = "- Toca el botón \"Solicitar Cierre\"";
                paso_3 = "- Completa los datos del cierre";
                paso_4 = "- De aceptar la solicitud, el creador cerrará el reporte";
                AyudaFAQDialogFragment dialogFragment = new AyudaFAQDialogFragment();
                dialogFragment.setArguments(cargar_bundle()); // Establece el Bundle como argumento
                dialogFragment.show(getFragmentManager(), "layout_cerrar_reporte");
            }
        });
    }

    private void comportamiento_boton_preg_cuatro(Button pregunta){
        pregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo= "¿Cómo cerrar un reporte?";
                subtitulo = "Puedes cerrar un reporte de la siguiente manera:";
                paso_1 = "- Abre el reporte que deseas cerrar.";
                paso_2 = "- Toca el botón \"Cerrar\".";
                paso_3 = "- Valida la información de cierre en la nueva ventana";
                paso_4 = "- Confirma el cierre o reabrelo si no cumple las condiciones";
                AyudaFAQDialogFragment dialogFragment = new AyudaFAQDialogFragment();
                dialogFragment.setArguments(cargar_bundle()); // Establece el Bundle como argumento
                dialogFragment.show(getFragmentManager(), "layout_cerrar_reporte");
            }
        });
    }

    private void comportamiento_boton_preg_cinco(Button pregunta){
        pregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo= "¿Cómo crear un proyecto?";
                subtitulo = "Para crear un proyecto, sigue estos pasos:";
                paso_1 = "- Inicia sesión en tu cuenta.";
                paso_2 = "- Toca el botón \"Crear Proyecto\".";
                paso_3 = "- Rellena la información del proyecto y guárdalo.";
                paso_4 = "";
                AyudaFAQDialogFragment dialogFragment = new AyudaFAQDialogFragment();
                dialogFragment.setArguments(cargar_bundle()); // Establece el Bundle como argumento
                dialogFragment.show(getFragmentManager(), "layout_cerrar_reporte");
            }
        });
    }

    private void comportamiento_boton_preg_seis(Button pregunta){
        pregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo= "¿Cómo unirse a un proyecto?";
                subtitulo = "Para unirte a un proyecto, sigue estos pasos:";
                paso_1 = "- Explora la lista de proyectos disponibles.";
                paso_2 = "- Selecciona el proyecto al que deseas unirte.";
                paso_3 = "- Toca el botón \"Unirse\"";
                paso_4 = "";
                AyudaFAQDialogFragment dialogFragment = new AyudaFAQDialogFragment();
                dialogFragment.setArguments(cargar_bundle()); // Establece el Bundle como argumento
                dialogFragment.show(getFragmentManager(), "layout_cerrar_reporte");
            }
        });
    }

    private void comportamiento_boton_preg_siete(Button pregunta){
        pregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo= "¿Cómo finalizar un proyecto?";
                subtitulo = "Si deseas finalizar un proyecto, sigue estos pasos:";
                paso_1 = "- Abre el proyecto que deseas finalizar.";
                paso_2 = "- Toca el botón \"Finalizar\".";
                paso_3 = "- Confirma la finalización.";
                paso_4 = "";
                AyudaFAQDialogFragment dialogFragment = new AyudaFAQDialogFragment();
                dialogFragment.setArguments(cargar_bundle()); // Establece el Bundle como argumento
                dialogFragment.show(getFragmentManager(), "layout_cerrar_reporte");
            }
        });
    }

    private void comportamiento_boton_preg_ocho(Button pregunta){
        pregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo= "¿Cómo funciona el sistema de puntos?";
                subtitulo = "Recibiras puntos por distintas acciones realizadas";
                paso_1 = "- Crear un reporte: 5 puntos.";
                paso_2 = "- Crear un proyecto: 15 puntos.";
                paso_3 = "- Valorar o cerrar un reporte: 1 punto";
                paso_4 = "- Atender o participar de una publicación: 2 puntos";
                AyudaFAQDialogFragment dialogFragment = new AyudaFAQDialogFragment();
                dialogFragment.setArguments(cargar_bundle()); // Establece el Bundle como argumento
                dialogFragment.show(getFragmentManager(), "layout_cerrar_reporte");
            }
        });
    }

    private void comportamiento_boton_preg_nueve(Button pregunta){
        pregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo= "¿Cómo denunciar una publicación?";
                subtitulo = "Si necesitas denunciar una publicación, sigue estos pasos:";
                paso_1 = "- Abre la publicación que deseas denunciar.";
                paso_2 = "- Toca el botón de \"Denunciar\".";
                paso_3 = "- Proporciona los detalles de la denuncia y envíala.";
                paso_4 = "";
                AyudaFAQDialogFragment dialogFragment = new AyudaFAQDialogFragment();
                dialogFragment.setArguments(cargar_bundle()); // Establece el Bundle como argumento
                dialogFragment.show(getFragmentManager(), "layout_cerrar_reporte");
            }
        });
    }
}