package com.tec.sgvmobile.ui.consultas;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.sgvmobile.models.Consulta;
import com.tec.sgvmobile.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FiltrosConsultasViewModel extends AndroidViewModel {

    private MutableLiveData<List<Consulta>> listaConsultas = new MutableLiveData<>();
    private List<Consulta> consultas = new ArrayList<>();

    public FiltrosConsultasViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Consulta>> getListaConsultas() {
        return listaConsultas;
    }

    public void buscarPorMotivo(int idMascota, String motivo) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoService api = ApiClient.getInmoService();
        Call<List<Consulta>> call = api.obtenerConsultasPorMotivo("Bearer " + token, idMascota, motivo);
        call.enqueue(new Callback<List<Consulta>>() {
            @Override
            public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    consultas.clear();
                    consultas.addAll(response.body());
                    listaConsultas.postValue(consultas);
                    Toast.makeText(getApplication(), "Consultas encontradas: " + consultas.size(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication(), "No se encontraron consultas con ese motivo.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en la conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void buscarEsteMes(int idMascota) {
        Log.d("motivo", "enviando consulta este mes para mascota con id: " + idMascota);
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoService api = ApiClient.getInmoService();
        Call<List<Consulta>> call = api.obtenerConsultasEsteMes("Bearer " + token, idMascota);
        call.enqueue(new Callback<List<Consulta>>() {
            @Override
            public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    consultas.clear();
                    consultas.addAll(response.body());
                    listaConsultas.postValue(consultas);
                    Toast.makeText(getApplication(), "Consultas de este mes: " + consultas.size(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication(), "No hay consultas este mes.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en la conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void buscarEntreFechas(int idMascota, String desde, String hasta) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoService api = ApiClient.getInmoService();
        Call<List<Consulta>> call = api.obtenerConsultasEntreFechas("Bearer " + token, idMascota, desde, hasta);
        call.enqueue(new Callback<List<Consulta>>() {
            @Override
            public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    consultas.clear();
                    consultas.addAll(response.body());
                    listaConsultas.postValue(consultas);
                    Toast.makeText(getApplication(), "Consultas en el rango: " + consultas.size(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication(), "No se encontraron consultas entre esas fechas.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en la conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
