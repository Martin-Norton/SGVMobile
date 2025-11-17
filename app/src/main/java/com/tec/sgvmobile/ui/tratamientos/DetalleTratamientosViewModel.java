package com.tec.sgvmobile.ui.tratamientos;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.sgvmobile.models.Tratamiento;
import com.tec.sgvmobile.models.Turno;
import com.tec.sgvmobile.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleTratamientosViewModel extends AndroidViewModel{
    private MutableLiveData<List<Tratamiento>> mTratamientos = new MutableLiveData<>();

    public DetalleTratamientosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Tratamiento>> getMtratamientos() {
        return mTratamientos;
    }
    public void cargarTratamientos(int idMascota) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.VeterinariaService api = ApiClient.getVeteService();
        Call<List<Tratamiento>> call = api.tratamientosPorMascota("Bearer " + token, idMascota);

        call.enqueue(new Callback<List<Tratamiento>>() {
            @Override
            public void onResponse(Call<List<Tratamiento>> call, Response<List<Tratamiento>> response) {
                if (response.isSuccessful()) {
                    mTratamientos.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Error al obtener tratamientos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tratamiento>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}