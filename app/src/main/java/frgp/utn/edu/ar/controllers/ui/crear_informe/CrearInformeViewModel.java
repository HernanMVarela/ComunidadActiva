package frgp.utn.edu.ar.controllers.ui.crear_informe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CrearInformeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CrearInformeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}