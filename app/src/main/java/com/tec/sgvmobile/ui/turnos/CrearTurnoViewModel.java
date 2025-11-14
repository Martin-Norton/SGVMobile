package com.tec.sgvmobile.ui.turnos;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.sgvmobile.dtos.CrearTurnoDto;
import com.tec.sgvmobile.models.Turno;
import com.tec.sgvmobile.request.ApiClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearTurnoViewModel extends AndroidViewModel {

    private MutableLiveData<List<String>> horarios = new MutableLiveData<>();
    private MutableLiveData<Boolean> creado = new MutableLiveData<>();

    public CrearTurnoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<String>> getHorarios() {
        return horarios;
    }
    public LiveData<Boolean> getCreado(){return creado;}

    public void obtenerHorariosDisponibles(String fecha) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.VeterinariaService api = ApiClient.getVeteService();

        Call<List<String>> call = api.getHorariosDisponibles("Bearer " + token, fecha);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    horarios.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "No hay horarios disponibles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void crearTurno(int idMascota, String motivo, String fechaHoraVista) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.VeterinariaService api = ApiClient.getVeteService();

        try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date fechaHora = formato.parse(fechaHoraVista);
            if (motivo.trim().isEmpty() || motivo.trim().length() < 2){
                motivo = "Sin motivo especificado";
            }
            CrearTurnoDto body = new CrearTurnoDto(idMascota, motivo, fechaHora);
            Call<Turno> call = api.crearTurno("Bearer " + token, idMascota, body);
            call.enqueue(new Callback<Turno>() {
                @Override
                public void onResponse(Call<Turno> call, Response<Turno> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplication(), "Turno creado con éxito", Toast.LENGTH_SHORT).show();
                        creado.postValue(true);
                    } else {
                        Toast.makeText(getApplication(), "Ya hay un turno para esta mascota en este día! Seleccione otro día." , Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Turno> call, Throwable t) {
                    Toast.makeText(getApplication(), "error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), "Error al procesar la fecha", Toast.LENGTH_SHORT).show();
        }
    }
}
