package com.tec.sgvmobile.ui.consultas;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.sgvmobile.models.Consulta;

import java.util.ArrayList;
import java.util.List;

public class ResultadosBusquedaViewModel extends AndroidViewModel {

    private MutableLiveData<List<Consulta>> listaConsultas = new MutableLiveData<>();

    public ResultadosBusquedaViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Consulta>> getListaConsultas() {
        return listaConsultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        if (consultas == null) {
            consultas = new ArrayList<>();
        }
        listaConsultas.setValue(consultas);
    }
}
