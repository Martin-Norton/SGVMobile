package com.tec.sgvmobile.ui.turnos;

import android.app.Application;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.tec.sgvmobile.models.Turno;
import com.tec.sgvmobile.request.ApiClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleTurnosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Turno>> turnosFuturos = new MutableLiveData<>();

    public DetalleTurnosViewModel(@NonNull Application app) {
        super(app);
    }

    public LiveData<List<Turno>> getTurnosFuturos() {
        return turnosFuturos;
    }

    public void cargarTurnosFuturos(int idMascota) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoService api = ApiClient.getInmoService();
        Call<List<Turno>> call = api.getTurnosFuturos("Bearer " + token, idMascota);

        call.enqueue(new Callback<List<Turno>>() {
            @Override
            public void onResponse(Call<List<Turno>> call, Response<List<Turno>> response) {
                if (response.isSuccessful()) {
                    turnosFuturos.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Error al obtener turnos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Turno>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
