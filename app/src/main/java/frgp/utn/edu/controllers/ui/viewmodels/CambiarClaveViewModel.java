package frgp.utn.edu.controllers.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CambiarClaveViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CambiarClaveViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}