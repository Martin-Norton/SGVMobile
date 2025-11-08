package com.tec.sgvmobile.ui.inicio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class InicioViewModel extends AndroidViewModel {

    private final MutableLiveData<LatLng> ubicacion = new MutableLiveData<>();
    private final MutableLiveData<String> titulo = new MutableLiveData<>();

    public InicioViewModel(@NonNull Application application) {
        super(application);
        ubicacion.setValue(new LatLng(-32.90179071060903, -68.79113147001739));
        titulo.setValue("Indicador en Veterinaria San Francisco");
    }

    public LiveData<LatLng> getUbicacion() {
        return ubicacion;
    }

    public LiveData<String> getTitulo() {
        return titulo;
    }

    public void mostrarUbicacion(GoogleMap map) {
        LatLng punto = ubicacion.getValue();
        String texto = titulo.getValue();

        if (punto != null && map != null) {
            map.clear();
            map.addMarker(new MarkerOptions().position(punto).title(texto));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(punto, 16f));
        }
    }
}
