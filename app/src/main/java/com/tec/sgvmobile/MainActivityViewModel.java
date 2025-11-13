package com.tec.sgvmobile;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainActivityViewModel extends AndroidViewModel {
    private MutableLiveData<String> nombre = new MutableLiveData<>();
    private MutableLiveData<String> email = new MutableLiveData<>();
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        cargarDatosUsuario();
    }
    public LiveData<String> getNombre() {
        return nombre;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public void cargarDatosUsuario() {
        SharedPreferences sp = getApplication()
                .getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);

        nombre.setValue(sp.getString("nombre", "Usuario"));
        email.setValue(sp.getString("email", "emailUsuario@dominio.com"));
    }
}
