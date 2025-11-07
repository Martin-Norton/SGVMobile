package com.tec.sgvmobile.ui.inmuebles;

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

public class MascotasViewModel extends AndroidViewModel {
    private MutableLiveData<List<Mascota>> listaMascotas= new MutableLiveData<>();
    public MascotasViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData <List<Mascota>> getlistaMascotas(){
        return listaMascotas;
    }

    public void obtenerListaMascotas(){
        String token= ApiClient.leerToken(getApplication());
        ApiClient.InmoService api = ApiClient.getInmoService();
        Call<List<Mascota>> call = api.getMascotas("Bearer " + token);
        call.enqueue(new Callback<List<Mascota>>() {
            @Override
            public void onResponse(Call<List<Mascota>> call, Response<List<Mascota>> response) {
                if (response.isSuccessful()){
                    listaMascotas.postValue(response.body());
                }else{
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