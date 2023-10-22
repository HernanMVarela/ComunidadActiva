package frgp.utn.edu.ar.controllers.ui.viewmodels;

import androidx.lifecycle.ViewModel;

import frgp.utn.edu.ar.controllers.data.model.Usuario;

public class UsuarioViewModel extends ViewModel {
    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
