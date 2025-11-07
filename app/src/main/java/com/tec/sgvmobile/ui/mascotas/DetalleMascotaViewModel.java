package com.tec.sgvmobile.ui.mascotas;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
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

public class DetalleMascotaViewModel extends AndroidViewModel {
    private MutableLiveData<Mascota> mMascota;
    private Uri nuevaImagenUri;
    private MutableLiveData<Uri> uriMutableLiveData;
    public DetalleMascotaViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<Mascota> getMmascota(){
        if (mMascota == null){
            mMascota = new MutableLiveData<>();
        }
        return mMascota;
    }
    public LiveData<Uri> getUriMutableLiveData() {
        if (uriMutableLiveData == null) {
            uriMutableLiveData = new MutableLiveData<>();
        }
        return uriMutableLiveData;
    }
    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Uri uri = result.getData().getData();
            uriMutableLiveData.setValue(uri);
            setNuevaImagenUri(uri);
        }
    }
    public void recuperarMascota(Bundle bundle){
        Mascota mascota = (Mascota) bundle.get("mascotaBundle");
        if (mascota!= null){
            mMascota.setValue(mascota);
        }
        if (uriMutableLiveData == null) uriMutableLiveData = new MutableLiveData<>();
        uriMutableLiveData.setValue(Uri.parse(mascota.getImagen()));
    }
    public void setNuevaImagenUri(Uri uri) {
        this.nuevaImagenUri = uri;
    }
    public void actualizarMascota(Mascota mascota) {
        ApiClient.InmoService api = ApiClient.getInmoService();
        String token = ApiClient.leerToken(getApplication());

        try {
            RequestBody nombre = RequestBody.create(MediaType.parse("text/plain"), mascota.getNombre());
            RequestBody especie = RequestBody.create(MediaType.parse("text/plain"), mascota.getEspecie());
            RequestBody raza = RequestBody.create(MediaType.parse("text/plain"), mascota.getRaza());
            RequestBody edad = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mascota.getEdad()));
            RequestBody peso = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mascota.getPeso()));
            RequestBody sexo = RequestBody.create(MediaType.parse("text/plain"), mascota.getSexo());
            RequestBody estado = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mascota.getEstado()));
            RequestBody imagen = RequestBody.create(MediaType.parse("text/plain"),
                    mascota.getImagen() != null ? mascota.getImagen() : "");

            MultipartBody.Part imagenArchivo = null;
            if (nuevaImagenUri != null) {
                InputStream inputStream = getApplication().getContentResolver().openInputStream(nuevaImagenUri);
                byte[] bytes = getBytes(inputStream);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), bytes);
                imagenArchivo = MultipartBody.Part.createFormData("imagen", "mascota_actualizada.jpg", requestFile);
            }
            Call<Void> call = api.actualizarMascota(
                    "Bearer " + token,
                    mascota.getId(),
                    nombre,
                    especie,
                    raza,
                    edad,
                    peso,
                    sexo,
                    estado,
                    imagen,
                    imagenArchivo
            );

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplication(), "Mascota actualizada correctamente", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplication(), "Error " + response.code(), Toast.LENGTH_LONG).show();
                        Log.e("ActualizarMascota", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("ActualizarMascota", "Fallo: " + t.getMessage(), t);
                    Toast.makeText(getApplication(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

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