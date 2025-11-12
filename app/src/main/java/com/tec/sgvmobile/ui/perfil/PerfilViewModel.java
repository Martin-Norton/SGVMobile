package com.tec.sgvmobile.ui.perfil;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.sgvmobile.models.Usuario;
import com.tec.sgvmobile.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> mEstado = new MutableLiveData<>();
    private MutableLiveData<String> mTexto = new MutableLiveData<>();
    private MutableLiveData<Usuario> usuario = new MutableLiveData<>();

    public PerfilViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getMEstado() { return mEstado; }
    public LiveData<String> getMTexto() { return mTexto; }
    public LiveData<Usuario> getUsuario() { return usuario; }

    public void cambioBoton(String textoBoton, String nombre, String apellido, String dni, String telefono, String email) {
        if (textoBoton.equals("EDITAR")) {
            mEstado.setValue(true);
            mTexto.setValue("GUARDAR");
        } else {
            if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                Toast.makeText(getApplication(), "Todos los campos son obligatorios.", Toast.LENGTH_LONG).show();
                return;
            }
            if (!dni.matches("\\d+")) {
                Toast.makeText(getApplication(), "El DNI debe contener solo números.", Toast.LENGTH_LONG).show();
                return;
            }
            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                Toast.makeText(getApplication(), "Ingrese un email válido.", Toast.LENGTH_LONG).show();
                return;
            }
            if (telefono.length() < 7) {
                Toast.makeText(getApplication(), "Ingrese un teléfono válido.", Toast.LENGTH_LONG).show();
                return;
            }

            mEstado.setValue(false);
            mTexto.setValue("EDITAR");

            String token = ApiClient.leerToken(getApplication());
            ApiClient.InmoService api = ApiClient.getInmoService();

            Usuario usuarioActual = usuario.getValue();
            if (usuarioActual == null) {
                Toast.makeText(getApplication(), "Error: usuario no cargado.", Toast.LENGTH_LONG).show();
                return;
            }
            Usuario usuarioNuevo = new Usuario();
            usuarioNuevo.setId(usuarioActual.getId());
            usuarioNuevo.setNombre(nombre);
            usuarioNuevo.setApellido(apellido);
            usuarioNuevo.setDni(usuarioActual.getDni());
            usuarioNuevo.setTelefono(telefono);
            usuarioNuevo.setEmail(usuarioActual.getEmail());
            usuarioNuevo.setRol(usuarioActual.getRol());
            usuarioNuevo.setEstado(usuarioActual.getEstado());
            Log.d("telefono", "telefono del user nuevo: " + usuarioNuevo.getTelefono());
            Call<Usuario> call = api.actualizarUsuario("Bearer " + token, usuarioNuevo);
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.isSuccessful()) {
                        usuario.setValue(usuarioNuevo);
                        Toast.makeText(getApplication(), "Perfil actualizado correctamente.", Toast.LENGTH_LONG).show();
                        Log.d("PerfilVM", "Perfil actualizado con éxito.");
                    } else {
                        Toast.makeText(getApplication(), "Error al actualizar: " + response.code(), Toast.LENGTH_LONG).show();
                        Log.e("PerfilVM", "Error al actualizar: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(getApplication(), "Error en la conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("PerfilVM", "Error conexión: " + t.getMessage());
                }
            });
        }
    }
    public void obtenerPerfil() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoService api = ApiClient.getInmoService();

        Call<Usuario> call = api.getUsuario("Bearer " + token);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    usuario.setValue(response.body());
                    Log.d("PerfilVM", "Usuario obtenido correctamente.");
                } else {
                    Toast.makeText(getApplication(), "Error al obtener usuario: " + response.code(), Toast.LENGTH_LONG).show();
                    Log.e("PerfilVM", "Error al obtener usuario: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(getApplication(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("PerfilVM", "Error de conexión: " + t.getMessage());
            }
        });
    }
}