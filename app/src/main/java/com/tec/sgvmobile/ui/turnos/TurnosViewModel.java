package com.tec.sgvmobile.ui.turnos;

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

public class TurnosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Mascota>> listaMascotasConTurnos = new MutableLiveData<>();
    private List<Mascota> mascotasConTurno;

    public TurnosViewModel(@NonNull Application application) {
        super(application);
        mascotasConTurno = new ArrayList<>();
    }

    public LiveData<List<Mascota>> getListaMascotasConTurnos() {
        return listaMascotasConTurnos;
    }

    public void obtenerListaMascotasConTurnos() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.VeterinariaService api = ApiClient.getVeteService();
        Call<List<Mascota>> call = api.getMascotasConTurnos("Bearer " + token);
        call.enqueue(new Callback<List<Mascota>>() {
            @Override
            public void onResponse(Call<List<Mascota>> call, Response<List<Mascota>> response) {
                if (response.isSuccessful()) {
                    mascotasConTurno.clear();
                    mascotasConTurno.addAll(response.body());
                    listaMascotasConTurnos.postValue(mascotasConTurno);
                    Toast.makeText(getApplication(), "Sus mascotas con turnos: ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "Error al obtener mascotas con turnos", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mascota>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}