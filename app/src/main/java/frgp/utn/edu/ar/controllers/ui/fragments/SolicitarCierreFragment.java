package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.CierreReporte;
import frgp.utn.edu.ar.controllers.data.model.EstadoReporte;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMAActualizarEstadoReporte;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMAGuardarCierreReporte;
import frgp.utn.edu.ar.controllers.ui.viewmodels.SolicitarCierreViewModel;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.NotificacionService;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class SolicitarCierreFragment extends Fragment {
    private static final int CAMERA_PIC_REQUEST = 1337;
    private SolicitarCierreViewModel mViewModel;
    private Reporte selectedReport = null;
    private Usuario loggedInUser = null;
    private TextView titulo;
    private EditText motivo;
    private Bitmap imagenCapturada = null;
    private ImageView imagenCierre = null;
    private LogService logService = new LogService();
    private NotificacionService notificacionService = new NotificacionService();

    public static SolicitarCierreFragment newInstance() {
        return new SolicitarCierreFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            selectedReport = (Reporte) args.getSerializable("selected_report");
            loggedInUser = (Usuario) args.getSerializable("logged_in_user");
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_solicitar_cierre, container, false);

        titulo = view.findViewById(R.id.cerrar_rep_titulo);
        imagenCierre = view.findViewById(R.id.cerrar_rep_imagen);
        motivo = view.findViewById(R.id.cerrar_rep_etdescripcion);

        Bundle bundle = this.getArguments();
        /// OBTIENE EL REPORTE SELECCIONADO EN LA PANTALLA ANTERIOR
        if (bundle != null) {
            selectedReport = (Reporte) bundle.getSerializable("selected_report");
            if(selectedReport != null){
                titulo.setText(selectedReport.getTitulo());
            }
        }else{
            FragmentManager fragmentManager = getChildFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                // Si hay fragmentos en la pila, retrocede al fragment anterior
                fragmentManager.popBackStack();
            }else{
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_nav_home_to_nav_buscar_reporte);
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnCamara = view.findViewById(R.id.btn_cerrar_rep_camara);
        Button btnCerrar = view.findViewById(R.id.btn_cerrar_rep_confirmar);
        comportamiento_boton_camara(btnCamara);
        comportamiento_boton_cierre(btnCerrar);
    }

    private void comportamiento_boton_cierre(Button btnCerrar){
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /// SE VALIDAN LOS CAMPOS
                if(validarCampos()){
                    /// SE CREA NUEVO CIERREREPORTE Y SE CARGAN SUS DATOS
                    CierreReporte cerrarRep = new CierreReporte();
                    cerrarRep.setUser(loggedInUser);
                    cerrarRep.setReporte(selectedReport);
                    cerrarRep.setImagen(imagenCapturada);
                    cerrarRep.setMotivo(motivo.getText().toString());
                    cerrarRep.setFechaCierreFromDate(new Date(System.currentTimeMillis()));
                    cerrarRep.setEstado(new EstadoReporte(2,"PENDIENTE"));

                    try {
                        /// SE INTENTA GUARDAR EL NUEVO CIERRE EN LA DB
                        DMAGuardarCierreReporte dmaCierreRep = new DMAGuardarCierreReporte(cerrarRep);
                        dmaCierreRep.execute();
                        if(dmaCierreRep.get()){
                            /// SI SE GUARDA CORRECTAMENTE - SE INTENTA ACTUALIZAR EL ESTADO DEL REPORTE
                            cerrarRep.getReporte().setEstado(new EstadoReporte(2,"PENDIENTE"));
                            DMAActualizarEstadoReporte dmaActualizar = new DMAActualizarEstadoReporte(cerrarRep.getReporte());
                            dmaActualizar.execute();
                            if(dmaActualizar.get()){
                                /// SI SE ACTUALIZA EL ESTADO, SE GUARDA EL LOG Y SE GENERA LA NOTIFICACION
                                Toast.makeText(getContext(), "Reporte cerrado!", Toast.LENGTH_SHORT).show();
                                logService.log(loggedInUser.getId(), LogsEnum.SOLICITUD_CIERRE_REPORTE, String.format("Solicitaste el cierre del reporte %s", selectedReport.getTitulo()));
                                notificacionService.notificacion(selectedReport.getOwner().getId(), String.format("EL %s %s solicito el cierre del reporte %s", loggedInUser.getTipo().getTipo(), loggedInUser.getUsername(), selectedReport.getTitulo()));
                                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                                navController.popBackStack();
                            }else{
                                Toast.makeText(getContext(), "No se pudo actualizar el estado!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getContext(), "No se pudo cerrar el reporte!", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void comportamiento_boton_camara(Button btnCamara){
        btnCamara.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Verificar si tiene permiso de la cámara
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // SI TIENE PERMISOS, REDIRIGE A LA VISTA DE LA CÁMARA
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                } else {
                    // Si no tiene el permiso, se pide al usuario
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PIC_REQUEST);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SolicitarCierreViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // VALIDA QUE LA ACCION SE HAYA COMPLETADO CORRECTAMENTE
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            // La imagen se capturó exitosamente
            Bundle extras = data.getExtras();
            imagenCapturada = (Bitmap) extras.get("data");
            // Modificación de tamaño - prueba de imagen
            assert imagenCapturada != null;
            Bitmap imagenRedimensionada = Bitmap.createScaledBitmap(imagenCapturada, imagenCapturada.getWidth()*2, imagenCapturada.getHeight()*2, true);
            // Configura la imagen capturada en el ImageView - prueba de imagen
            imagenCierre.setImageBitmap(imagenRedimensionada);
        }
    }

    private boolean validarCampos(){
        if(motivo.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Debe indicar el motivo.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(motivo.getText().toString().length()>250){
            Toast.makeText(this.getContext(), "La descripción es muy larga!", Toast.LENGTH_LONG).show();
            return false;
        }
        if(imagenCapturada == null){
            Log.i("IMAGEN","LLEGA AL VALIDAR UNA VEZ");
            Bitmap imagenPredeterminada = BitmapFactory.decodeResource(getResources(), R.mipmap.image_placeholder_foreground);
            int nuevoAncho = 50;
            int nuevoAlto = 50;
            imagenCapturada = Bitmap.createScaledBitmap(imagenPredeterminada, nuevoAncho, nuevoAlto, true);
            imagenCierre.setImageBitmap(imagenCapturada);
            Toast.makeText(getContext(), "Está por cerrar un reporte sin evidencia, para continuar vuelva a presionar Cerrar Repote", Toast.LENGTH_LONG).show();
            return false;
        }
        if(selectedReport == null){
            Toast.makeText(getContext(), "Error al obtener el reporte", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}