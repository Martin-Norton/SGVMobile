package com.tec.sgvmobile.ui.mascotas;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.sgvmobile.models.Mascota;
import com.tec.sgvmobile.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearMascotaViewModel extends AndroidViewModel {

    private MutableLiveData<Uri> uriMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mascotaCreada = new MutableLiveData<>();
    private MutableLiveData<Intent> intentCamara = new MutableLiveData<>();
    private MutableLiveData<Boolean> solicitarPermisoCamara = new MutableLiveData<>();
    private Uri nuevaImagenUri;
    private static final int REQUEST_CAMERA_PERMISSION = 101;//codigo con el que identifico ESTE pedido request de camara

    public CrearMascotaViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Uri> getUriMutableLiveData() {
        return uriMutableLiveData;
    }

    public LiveData<Boolean> getMascotaCreada() {
        return mascotaCreada;
    }

    public LiveData<Intent> getIntentCamara() {
        return intentCamara;
    }

    public LiveData<Boolean> getSolicitarPermisoCamara() {
        return solicitarPermisoCamara;
    }

    public void verificarPermisoCamara(@NonNull Fragment fragment) {
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            solicitarPermisoCamara.postValue(true);
        } else {
            prepararCamara();
        }
    }

    public void prepararCamara() {
        try {
            File fotoFile = File.createTempFile(
                    "foto_mascota_", ".jpg",
                    getApplication().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );

            nuevaImagenUri = FileProvider.getUriForFile(
                    getApplication(),
                    getApplication().getPackageName() + ".provider",
                    fotoFile
            );

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, nuevaImagenUri);
            intentCamara.postValue(cameraIntent);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), "Error al preparar la cámara", Toast.LENGTH_SHORT).show();
        }
    }
    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == android.app.Activity.RESULT_OK && nuevaImagenUri != null) {
            setNuevaImagenUri(nuevaImagenUri);
            uriMutableLiveData.setValue(nuevaImagenUri);
        }
    }
    public void setNuevaImagenUri(Uri uri) {
        nuevaImagenUri = uri;
        uriMutableLiveData.setValue(nuevaImagenUri);
    }
    public void guardarMascota(String nombreStr, String especieStr, String razaStr, String edadStr, String pesoStr, String sexoStr) {
        if (nombreStr == null || nombreStr.trim().isEmpty() ||
                especieStr == null || especieStr.trim().isEmpty() ||
                razaStr == null || razaStr.trim().isEmpty() ||
                edadStr == null || edadStr.trim().isEmpty() ||
                pesoStr == null || pesoStr.trim().isEmpty() ||
                sexoStr == null || sexoStr.trim().isEmpty()) {
            Toast.makeText(getApplication(), "Todos los campos son obligatorios.", Toast.LENGTH_LONG).show();
            return;
        }

        int edad, peso;
        try {
            edad = Integer.parseInt(edadStr);
            peso = Integer.parseInt(pesoStr);
            if (edad <= 0 || peso <= 0) {
                Toast.makeText(getApplication(), "Edad y peso deben ser mayores que 0.", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getApplication(), "Edad y peso deben ser numéricos.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!(sexoStr.equalsIgnoreCase("M") || sexoStr.equalsIgnoreCase("H"))) {
            Toast.makeText(getApplication(), "El sexo debe ser 'M' o 'H'.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Mascota mascota = new Mascota();
            mascota.setNombre(nombreStr.trim());
            mascota.setEspecie(especieStr.trim());
            mascota.setRaza(razaStr.trim());
            mascota.setEdad(edad);
            mascota.setPeso(peso);
            mascota.setSexo(sexoStr.trim().toUpperCase());
            mascota.setEstado(1);

            ApiClient.InmoService api = ApiClient.getInmoService();
            String token = ApiClient.leerToken(getApplication());

            RequestBody nombre = RequestBody.create(MediaType.parse("text/plain"), mascota.getNombre());
            RequestBody especie = RequestBody.create(MediaType.parse("text/plain"), mascota.getEspecie());
            RequestBody raza = RequestBody.create(MediaType.parse("text/plain"), mascota.getRaza());
            RequestBody edadBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mascota.getEdad()));
            RequestBody pesoBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mascota.getPeso()));
            RequestBody sexo = RequestBody.create(MediaType.parse("text/plain"), mascota.getSexo());

            MultipartBody.Part imagenPart;
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
