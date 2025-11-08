package com.tec.sgvmobile.ui.mascotas;

import android.app.Application;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.tec.sgvmobile.R;
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

    private final MutableLiveData<Boolean> mEstado = new MutableLiveData<>(false);
    private final MutableLiveData<String> mTexto = new MutableLiveData<>("EDITAR");
    private final MutableLiveData<Mascota> mascotaLive = new MutableLiveData<>();
    private Uri nuevaImagenUri;

    public DetalleMascotaViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getMEstado() {
        return mEstado;
    }

    public LiveData<String> getMTexto() {
        return mTexto;
    }

    public LiveData<Mascota> getMascota() {
        return mascotaLive;
    }
    public void setMascota(Mascota m) {
        mascotaLive.setValue(m);
    }
    public void setNuevaImagenUri(Uri uri) {
        nuevaImagenUri = uri;
        Mascota m = mascotaLive.getValue();
        if (m != null && uri != null) {
            m.setImagen(uri.toString());
            mascotaLive.setValue(m);
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
                especie == null || especie.isEmpty() ||
                raza == null || raza.isEmpty() ||
                edad == null || edad.isEmpty() ||
                peso == null || peso.isEmpty() ||
                sexo == null || sexo.isEmpty()) {

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

        Mascota actual = mascotaLive.getValue();
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
        ApiClient.InmoService api = ApiClient.getInmoService();

        try {
            RequestBody nombre = RequestBody.create(MediaType.parse("text/plain"), (mascota.getNombre()));
            RequestBody especie = RequestBody.create(MediaType.parse("text/plain"),(mascota.getEspecie()));
            RequestBody raza = RequestBody.create(MediaType.parse("text/plain"), (mascota.getRaza()));
            RequestBody edad = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mascota.getEdad()));
            RequestBody peso = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mascota.getPeso()));
            RequestBody sexo = RequestBody.create(MediaType.parse("text/plain"), (mascota.getSexo()));
            RequestBody estado = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mascota.getEstado()));
            RequestBody imagen = RequestBody.create(MediaType.parse("text/plain"), (mascota.getImagen()));

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
                        mascotaLive.postValue(mascota);
                        Toast.makeText(getApplication(), "Mascota actualizada correctamente.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplication(), "Error al actualizar: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getApplication(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplication(), "Error al procesar la imagen", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void inicializarMascota(Mascota m) {
        if (m != null) {
            mascotaLive.setValue(m);
        }
        mEstado.setValue(false);
        mTexto.setValue("EDITAR");
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

        if (imagen.startsWith("content://")) {
            Glide.with(getApplication())
                    .load(Uri.parse(imagen))
                    .override(800, 800)
                    .centerCrop()
                    .into(imageView);
        } else {
            Glide.with(getApplication())
                    .load(ApiClient.BASE_URL + imagen)
                    .override(800, 800)
                    .centerCrop()
                    .into(imageView);
        }
    }
}
