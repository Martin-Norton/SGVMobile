package com.tec.sgvmobile.ui.consultas;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.sgvmobile.models.Mascota;
import com.tec.sgvmobile.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultasViewModel extends AndroidViewModel {

    private MutableLiveData<List<Mascota>> listaMascotasConConsultas = new MutableLiveData<>();
    private List<Mascota> mascotasConConsultas;

    public ConsultasViewModel(@NonNull Application application) {
        super(application);
        mascotasConConsultas = new ArrayList<>();
    }

    public LiveData<List<Mascota>> getListaMascotasConConsultas() {
        return listaMascotasConConsultas;
    }

    public void obtenerListaMascotasConConsultas() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoService api = ApiClient.getInmoService();
        Call<List<Mascota>> call = api.obtenerMascotasConConsultas("Bearer " + token);
        call.enqueue(new Callback<List<Mascota>>() {
            @Override
            public void onResponse(Call<List<Mascota>> call, Response<List<Mascota>> response) {
                if (response.isSuccessful()) {
                    mascotasConConsultas.clear();
                    mascotasConConsultas.addAll(response.body());
                    listaMascotasConConsultas.postValue(mascotasConConsultas);

                    Toast.makeText(getApplication(), "Cantidad de Mascotas con Consultas: " +  mascotasConConsultas.size(), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplication(), "Error al obtener Mascotas en onResponse", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mascota>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en la respuesta en onFailure" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}