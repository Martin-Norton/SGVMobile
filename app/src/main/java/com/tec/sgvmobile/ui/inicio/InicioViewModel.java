package com.tec.sgvmobile.ui.inicio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.sgvmobile.models.Publicidad;
import com.tec.sgvmobile.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioViewModel extends AndroidViewModel {

    private MutableLiveData<List<Publicidad>> publicidades = new MutableLiveData<>();

    public InicioViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Publicidad>> getPublicidades() {
        return publicidades;
    }

    public void cargarPublicidades() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.VeterinariaService api = ApiClient.getVeteService();

        Call<List<Publicidad>> call = api.getPublicidadesActivas("Bearer " + token);
        call.enqueue(new Callback<List<Publicidad>>() {
            @Override
            public void onResponse(Call<List<Publicidad>> call, Response<List<Publicidad>> response) {
                if (response.isSuccessful()) {
                    publicidades.postValue(response.body());
                } else {
                    publicidades.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Publicidad>> call, Throwable t) {
                publicidades.postValue(new ArrayList<>());
            }
        });
    }
}
