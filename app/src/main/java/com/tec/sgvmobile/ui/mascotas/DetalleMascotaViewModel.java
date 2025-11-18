package com.tec.sgvmobile.ui.mascotas;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.tec.sgvmobile.R;
import com.tec.sgvmobile.models.Mascota;
import com.tec.sgvmobile.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleMascotaViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> mEspecie = new MutableLiveData<>();
    private MutableLiveData<Integer> mSexo = new MutableLiveData<>();
    private MutableLiveData<Boolean> mEstado = new MutableLiveData<>();
    private MutableLiveData<String> mTexto = new MutableLiveData<>();
    private MutableLiveData<Mascota> mMascota = new MutableLiveData<>();
    private MutableLiveData<Intent> intentCamara = new MutableLiveData<>(); //aca viaja el intent de abrir la camara
    private MutableLiveData<Boolean> solicitarPermisoCamara = new MutableLiveData<>(); //aca viaja el estado de la solicitud del permiso
    private MutableLiveData<Boolean> dadoDeBaja = new MutableLiveData<>();
    private Uri nuevaImagenUri;

    public DetalleMascotaViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Integer> getMEspecie() {
        return mEspecie;
    }

    public LiveData<Integer> getMSexo() {
        return mSexo;
    }

    public LiveData<Boolean> getMEstado() {
        return mEstado;
    }

    public LiveData<String> getMTexto() {
        return mTexto;
    }

    public LiveData<Boolean> getDadoDeBaja() {
        return dadoDeBaja;
    }

    public LiveData<Mascota> getMascota() {
        return mMascota;
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
            cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, nuevaImagenUri);// el extraoutput le dice loco guardalo ACA nuevaImagenUri, no me lo pases en el .data, sirve para mejor resoluion de imagen sino la devuelve en 200x200
            intentCamara.postValue(cameraIntent);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), "Error al preparar la cámara", Toast.LENGTH_SHORT).show();
        }
    }

    public void recibirFoto(ActivityResult result) {
        setNuevaImagenUri(nuevaImagenUri);
    }

    public void setNuevaImagenUri(Uri uri) {
        nuevaImagenUri = uri;
        Mascota m = mMascota.getValue();
        if (m != null && uri != null) {
            m.setImagen(uri.toString());
            mMascota.setValue(m);
        }
    }

    public void cambioBoton(String textoBoton,
                            String nombre,
                            String especie,
                            String raza,
                            String edad,
                            String peso,
                            String sexo,
                            String estado) {

        if ("EDITAR".equals(textoBoton)) {
            mEstado.setValue(true);
            mTexto.setValue("GUARDAR");
            return;
        }
        if (nombre == null || nombre.isEmpty() ||
                raza == null || raza.isEmpty() ||
                edad == null || edad.isEmpty() ||
                peso == null || peso.isEmpty()) {

            Toast.makeText(getApplication(), "Todos los campos son obligatorios.", Toast.LENGTH_LONG).show();
            return;
        }

        int edadInt;
        int pesoInt;
        int estadoInt;
        try {
            edadInt = Integer.parseInt(edad);
            pesoInt = Integer.parseInt(peso);
            estadoInt = Integer.parseInt(estado);
        } catch (NumberFormatException ex) {
            Toast.makeText(getApplication(), "Edad, peso y estado deben ser numéricos.", Toast.LENGTH_LONG).show();
            return;
        }

        Mascota actual = mMascota.getValue();
        if (actual == null) {
            Toast.makeText(getApplication(), "Error: mascota no cargada.", Toast.LENGTH_LONG).show();
            return;
        }

        actual.setNombre(nombre);
        actual.setEspecie(especie);
        actual.setRaza(raza);
        actual.setEdad(edadInt);
        actual.setPeso(pesoInt);
        actual.setSexo(sexo);
        actual.setEstado(estadoInt);

        mEstado.setValue(false);
        mTexto.setValue("EDITAR");
        actualizarMascota(actual);
    }

    private void actualizarMascota(final Mascota mascota) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.VeterinariaService api = ApiClient.getVeteService();

        try {
            RequestBody nombre = RequestBody.create(MediaType.parse("text/plain"), (mascota.getNombre()));
            RequestBody especie = RequestBody.create(MediaType.parse("text/plain"), (mascota.getEspecie()));
            RequestBody raza = RequestBody.create(MediaType.parse("text/plain"), (mascota.getRaza()));
            RequestBody edad = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mascota.getEdad()));
            RequestBody peso = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mascota.getPeso()));
            RequestBody sexo = RequestBody.create(MediaType.parse("text/plain"), (mascota.getSexo()));
            RequestBody estado = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mascota.getEstado()));
            String imagenActual = mascota.getImagen() != null ? mascota.getImagen() : "";
            RequestBody imagen = RequestBody.create(
                    MediaType.parse("text/plain"),
                    imagenActual
            );

            MultipartBody.Part imagenArchivo = null;
            if (nuevaImagenUri != null) {
                InputStream inputStream = getApplication().getContentResolver().openInputStream(nuevaImagenUri);
                byte[] bytes = getBytes(inputStream);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), bytes);
                imagenArchivo = MultipartBody.Part.createFormData("imagenArchivo", "mascota_actualizada.jpg", requestFile);
            }

            Call<Void> call = api.actualizarMascota(
                    "Bearer " + token,
                    mascota.getId(),
                    nombre, especie, raza, edad, peso, sexo, imagen, estado, imagenArchivo
            );

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        mMascota.postValue(mascota);
                        Toast.makeText(getApplication(), "Mascota actualizada correctamente.", Toast.LENGTH_LONG).show();
                    } else {
                        if (response.code() == 409){
                            Toast.makeText(getApplication(), "Esa mascota ya existe!!", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplication(), "Error al actualizar: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getApplication(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplication(), "Error al procesar la imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void inicializarMascota(Mascota m) {
        if (m != null) {
            mMascota.setValue(m);
            List<String> especies = new ArrayList<>();
            especies.add("Canino");
            especies.add("Felino");
            especies.add("Roedor");
            especies.add("Ave");
            especies.add("Conejo");
            especies.add("Reptil");
            int posEspecie = especies.indexOf(m.getEspecie());
            mEspecie.postValue(posEspecie);

            List<String> sexos = new ArrayList<>();
            sexos.add("Macho");
            sexos.add("Hembra");
            int posSexos = sexos.indexOf(m.getSexo());
            mSexo.postValue(posSexos);

        }
        mEstado.setValue(false);
        mTexto.setValue("EDITAR");
    }

    public void baja(int idMascota) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.VeterinariaService api = ApiClient.getVeteService();
        Call<Void> call = api.darBaja("Bearer " + token, idMascota);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplication(), "Mascota dada de baja correctamente.", Toast.LENGTH_LONG).show();
                    dadoDeBaja.postValue(true);
                } else {
                    Toast.makeText(getApplication(), "Error al dar de baja la mascota.", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Toast.makeText(getApplication(), "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
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

    public void mostrarImagen(ImageView imageView, String imagen) {
        if (imagen == null || imagen.isEmpty()) {
            imageView.setImageResource(R.drawable.mascotas);
            return;
        }

        if (imagen.startsWith("content://")) {// cuando todavia esta local... por eso carga el uri
            Glide.with(getApplication())
                    .load(Uri.parse(imagen))
                    .override(800, 800)
                    .circleCrop()
                    .into(imageView);
        } else {
            Glide.with(getApplication())
                    .load(ApiClient.BASE_URL + imagen)//cuando ya existe en el server
                    .override(800, 800)
                    .circleCrop()
                    .into(imageView);
        }
    }
}
