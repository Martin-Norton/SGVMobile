package com.tec.sgvmobile.ui.login;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.sgvmobile.MainActivity;
import com.tec.sgvmobile.models.Usuario;
import com.tec.sgvmobile.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {
    private MutableLiveData<Usuario> usuarioMutable;
    private MutableLiveData<String> errorMutable;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Usuario> getusuario() {
        if (usuarioMutable == null) {
            usuarioMutable = new MutableLiveData<>();
        }
        return usuarioMutable;
    }

    public LiveData<String> getError() {
        if (errorMutable == null) {
            errorMutable = new MutableLiveData<>();
        }
        return errorMutable;
    }

    public void validarUsuario(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            errorMutable.setValue("Todos los campos son obligatorios");
            return;
        }
        ApiClient.InmoService service = ApiClient.getInmoService();
        Call<String> call = service.loginForm(email, password);
        Log.d("loginn","Llamando a la api");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("loginn","respuesta de la api " + response.message());
                if (response.isSuccessful()) {
                    String token = response.body();
                    Log.d("loginn","Obtuvimos el token");
                    ApiClient.guardarToken(getApplication(),token);
                    Log.d("token", token);
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(intent);
                } else {
                    Log.d("token", response.message());
                    Log.d("token", response.code() + "");
                    Log.d("token", response.errorBody() + "");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.d("token", throwable.getMessage());

            }
        });
    }
}
