package com.tec.sgvmobile.ui.tratamientos;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tec.sgvmobile.models.Mascota;
import com.tec.sgvmobile.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TratamientosViewModel extends AndroidViewModel {
    private MutableLiveData<List<Mascota>> listaMascotasConTratamientos= new MutableLiveData<>();
    private List<Mascota> mascotasConTratamiento;

    public TratamientosViewModel(@NonNull Application application) {
        super(application);
        mascotasConTratamiento = new ArrayList<>();
    }

    public LiveData<List<Mascota>> getListaMascotasConTratamientos() {
        return listaMascotasConTratamientos;
    }

    public void obtenerListaMascotasConTratamientos() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.VeterinariaService api = ApiClient.getVeteService();
        Call<List<Mascota>> call = api.mascotasConTratamientos("Bearer " + token);
        call.enqueue(new Callback<List<Mascota>>() {
            @Override
            public void onResponse(Call<List<Mascota>> call, Response<List<Mascota>> response) {
                if (response.isSuccessful()) {
                    mascotasConTratamiento.clear();
                    mascotasConTratamiento.addAll(response.body());
                    listaMascotasConTratamientos.postValue(mascotasConTratamiento);
                    Toast.makeText(getApplication(), "Sus mascotas con tratamientos: ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "Error al obtener mascotas con tratamientos", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mascota>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}