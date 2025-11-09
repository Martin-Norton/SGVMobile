package com.tec.sgvmobile.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tec.sgvmobile.dtos.CrearTurnoDto;
import com.tec.sgvmobile.models.Consulta;
import com.tec.sgvmobile.models.Mascota;
import com.tec.sgvmobile.models.Turno;
import com.tec.sgvmobile.models.Usuario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ApiClient {
    public final static String BASE_URL = "http://192.168.0.103:5000/";
    //public final static String BASE_URL = "http://192.168.0.4:5000/"; MI DEPTO

    public static InmoService getInmoService() {
        Gson gson = new GsonBuilder().setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(InmoService.class);
    }

    public static void guardarToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static String leerToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", null);
    }

    public interface InmoService {
        @FormUrlEncoded
        @POST("api/auth/login")
        Call<String> loginForm(@Field("Usuario") String usuario, @Field("Clave") String clave);

        //Zona Perfil
        @GET("api/usuarios/me")
        Call<Usuario> getUsuario(@Header("Authorization") String token);

        @PUT("api/usuarios/me")
        Call<Usuario> actualizarUsuario(@Header("Authorization") String token, @Body Usuario usuario);
//Fin Zona Perfil

        //Zona Mascotas
        @GET("api/mascotas/mias")
        //traer las mascotas del dueno loggeado
        Call<List<Mascota>> getMascotas(@Header("Authorization") String token);

        @Multipart
        @PUT("api/mascotas/actualizar/{id}")
        Call<Void> actualizarMascota(
                @Header("Authorization") String token,
                @Path("id") int id,
                @Part("nombre") RequestBody nombre,
                @Part("especie") RequestBody especie,
                @Part("raza") RequestBody raza,
                @Part("edad") RequestBody edad,
                @Part("peso") RequestBody peso,
                @Part("sexo") RequestBody sexo,
                @Part("imagen") RequestBody imagen,
                @Part("estado") RequestBody estado,
                @Part MultipartBody.Part imagenArchivo
        );
        @Multipart
        @POST("api/mascotas/crear")
        Call<Void> cargarMascota(
                @Header("Authorization") String token,
                @Part("nombre") RequestBody nombre,
                @Part("especie") RequestBody especie,
                @Part("raza") RequestBody raza,
                @Part("edad") RequestBody edad,
                @Part("peso") RequestBody peso,
                @Part("sexo") RequestBody sexo,
                @Part MultipartBody.Part imagen
        );

        //Fin Zona Mascotas
//Zona Turnos
        @GET("api/turnos/mascotas-con-turnos")
        Call<List<Mascota>> getConTurnos(@Header("Authorization") String token);

        @GET("api/turnos/{id}/futuros")
        Call<List<Turno>> getTurnosFuturos(@Header("Authorization") String token, @Path("id") int idMascota);

        @GET("api/turnos/horarios-disponibles")
        Call<List<String>> getHorariosDisponibles(
                @Header("Authorization") String token,
                @Query("fecha") String fechaISO
        );

        @POST("api/turnos/crear/{idMascota}")
            //crear/sacar un nuevo turno
        Call<Turno> crearTurno(
                @Header("Authorization") String token,
                @Path("idMascota") int idMascota,
                @Body CrearTurnoDto body
        );

//Fin Zona Turnos
//Zona Consultas
        @GET("api/consultas/mascotas-con-consultas")
        //Todas las mascotas de usuario loggeado que tienen consultas
        Call<List<Mascota>> obtenerMascotasConConsultas(
                @Header("Authorization") String token
        );

        @GET("api/consultas/por-mascota/{id}")
        // Consultas de una mascota en particular
        Call<List<Consulta>> obtenerConsultasPorMascota(
                @Header("Authorization") String token,
                @Path("id") int idMascota
        );

        @GET("api/consultas/por-mascota/{id}/buscar")
        Call<List<Consulta>> obtenerConsultasPorMotivo(
                @Header("Authorization") String token,
                @Path("id") int idMascota,
                @Query("q") String palabra
        );

        @GET("api/consultas/por-mascota/{id}/este-mes")
        Call<List<Consulta>> obtenerConsultasEsteMes(
                @Header("Authorization") String token,
                @Path("id") int idMascota
        );

        @GET("api/consultas/por-mascota/{id}/entre-fechas")
        Call<List<Consulta>> obtenerConsultasEntreFechas(
                @Header("Authorization") String token,
                @Path("id") int idMascota,
                @Query("desde") String desde,
                @Query("hasta") String hasta
        );
//Fin Zona Consultas
    }
}
