package com.tec.sgvmobile.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
        usuarioMutable = new MutableLiveData<>();
        errorMutable = new MutableLiveData<>();
    }

    public LiveData<Usuario> getusuario() {
        return usuarioMutable;
    }

    public LiveData<String> getError() {
        return errorMutable;
    }

    public void validarUsuario(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            errorMutable.setValue("Todos los campos son obligatorios");
            return;
        }
        ApiClient.VeterinariaService service = ApiClient.getVeteService();
        Call<String> call = service.loginForm(email, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("loginn","respuesta de la api " + response.message());
                if (response.isSuccessful()) {
                    String token = response.body();
                    Log.d("loginn","Obtuvimos el token");
                    ApiClient.guardarToken(getApplication(),token);
                    obtenerDatosUsuario(token);
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(intent);
                } else {
                    Log.d("token", response.message());
                    Log.d("token", response.code() + "");
                    errorMutable.postValue("Usuario y/o Clave incorrectos");
                    Log.d("token", response.errorBody() + "");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.d("token", throwable.getMessage());

            }
        });
    }
    private void obtenerDatosUsuario(String token) {
        ApiClient.VeterinariaService service = ApiClient.getVeteService();
        Call<Usuario> callUsuario = service.getUsuario("Bearer " + token);

        callUsuario.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario dueno = response.body();

                    SharedPreferences sp = getApplication()
                            .getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("nombre", dueno.getNombre() + " " + dueno.getApellido());
                    editor.putString("email", dueno.getEmail());
                    editor.apply();

                    Log.d("login", "Datos del usuario guardados");

                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getApplication().startActivity(intent);
                } else {
                    Log.d("login", "Error al obtener datos del usuario: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("login", "Fallo al obtener usuario", t);
            }
        });
    }

}
