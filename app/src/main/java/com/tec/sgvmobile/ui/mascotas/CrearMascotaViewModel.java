package com.tec.sgvmobile.ui.mascotas;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.sgvmobile.models.Mascota;
import com.tec.sgvmobile.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearMascotaViewModel extends AndroidViewModel {
    private MutableLiveData<Uri> uriMutableLiveData;
    private MutableLiveData<Mascota> mMascota;
    private Uri nuevaImagenUri;
    private MutableLiveData<Boolean> mascotaCreada;

    public CrearMascotaViewModel(@NonNull Application application) {
        super(application);
        uriMutableLiveData = new MutableLiveData<>();
        mMascota = new MutableLiveData<>();
        mascotaCreada = new MutableLiveData<>();
    }

    public LiveData<Uri> getUriMutableLiveData() {
        return uriMutableLiveData;
    }

    public LiveData<Mascota> getmMascota() {
        return mMascota;
    }

    public LiveData<Boolean> getMascotaCreada() { return mascotaCreada; }

    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Uri uri = result.getData().getData();
            uriMutableLiveData.setValue(uri);
            nuevaImagenUri = uri;
        }
    }

    public void guardarMascota(String nombreStr, String especieStr, String razaStr, String edadStr, String pesoStr, String sexoStr) {
        try {
            int edad = Integer.parseInt(edadStr);
            int peso = Integer.parseInt(pesoStr);

            Mascota mascota = new Mascota();
            mascota.setNombre(nombreStr);
            mascota.setEspecie(especieStr);
            mascota.setRaza(razaStr);
            mascota.setEdad(edad);
            mascota.setPeso(peso);
            mascota.setSexo(sexoStr);
            mascota.setEstado(1);

            ApiClient.InmoService api = ApiClient.getInmoService();
            String token = ApiClient.leerToken(getApplication());

            RequestBody nombre = RequestBody.create(MediaType.parse("text/plain"), mascota.getNombre());
            RequestBody especie = RequestBody.create(MediaType.parse("text/plain"), mascota.getEspecie());
            RequestBody raza = RequestBody.create(MediaType.parse("text/plain"), mascota.getRaza());
            RequestBody edadBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mascota.getEdad()));
            RequestBody pesoBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mascota.getPeso()));
            RequestBody sexo = RequestBody.create(MediaType.parse("text/plain"), mascota.getSexo());

            MultipartBody.Part imagenPart = null;
            if (nuevaImagenUri != null) {
                InputStream inputStream = getApplication().getContentResolver().openInputStream(nuevaImagenUri);
                byte[] bytes = getBytes(inputStream);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), bytes);
                imagenPart = MultipartBody.Part.createFormData("imagen", "foto_mascota.jpg", requestFile);
            } else {
                RequestBody empty = RequestBody.create(MediaType.parse("text/plain"), "");
                imagenPart = MultipartBody.Part.createFormData("imagen", "", empty);
            }

            Call<Void> call = api.cargarMascota(
                    "Bearer " + token,
                    nombre, especie, raza, edadBody, pesoBody, sexo, imagenPart
            );

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplication(), "Mascota creada correctamente", Toast.LENGTH_LONG).show();
                        mascotaCreada.postValue(true);
                    } else {
                        Toast.makeText(getApplication(), "Error al crear mascota: " + response.code(), Toast.LENGTH_LONG).show();
                        Log.d("MascotaCrearVM", "Error: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("MascotaCrearVM", "Fallo en conexión: " + t.getMessage());
                    Toast.makeText(getApplication(), "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (NumberFormatException e) {
            Toast.makeText(getApplication(), "Edad o peso inválidos", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), "Error al procesar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
}
