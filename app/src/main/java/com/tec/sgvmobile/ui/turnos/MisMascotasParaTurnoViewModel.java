package com.tec.sgvmobile.ui.turnos;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.sgvmobile.models.Mascota;
import com.tec.sgvmobile.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisMascotasParaTurnoViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Mascota>> mascotas = new MutableLiveData<>();

    public MisMascotasParaTurnoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Mascota>> getMascotas() {
        return mascotas;
    }

    public void cargarMascotas() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoService api = ApiClient.getInmoService();

        Call<List<Mascota>> call = api.getMascotas("Bearer " + token);
        call.enqueue(new Callback<List<Mascota>>() {
            @Override
            public void onResponse(Call<List<Mascota>> call, Response<List<Mascota>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mascotas.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Error al obtener mascotas.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mascota>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
