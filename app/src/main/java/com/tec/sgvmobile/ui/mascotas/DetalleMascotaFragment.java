package com.tec.sgvmobile.ui.mascotas;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.tec.sgvmobile.databinding.FragmentDetalleMascotaBinding;
import com.tec.sgvmobile.models.Mascota;
import com.tec.sgvmobile.request.ApiClient;

public class DetalleMascotaFragment extends Fragment {

    private DetalleMascotaViewModel mv;
    private FragmentDetalleMascotaBinding binding;
    private Mascota mascota;
    private Uri nuevaImagenUri;
    private Intent intent;
    private ActivityResultLauncher<Intent> arlImagen;


    public static DetalleMascotaFragment newInstance() {
        return new DetalleMascotaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mascota = new Mascota();
        mv = new ViewModelProvider(this).get(DetalleMascotaViewModel.class);
        binding = FragmentDetalleMascotaBinding.inflate(inflater, container, false);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Detalles de la Mascota");
        View view = binding.getRoot();
        mv.getMmascota().observe(getViewLifecycleOwner(), new Observer<Mascota>() {
            @Override
            public void onChanged(Mascota mascota) {
                binding.etCodigo.setText(String.valueOf(mascota.getId()));
                binding.etNombreMascota.setText(mascota.getNombre());
                binding.etEspecie.setText(mascota.getEspecie());
                binding.etRaza.setText(String.valueOf(mascota.getRaza()));
                binding.etEdad.setText(String.valueOf(mascota.getEdad()));
                binding.etSexo.setText(mascota.getSexo());
                binding.etPeso.setText(String.valueOf(mascota.getPeso()));
                String imagen = mascota.getImagen();
                if (imagen != null) {
                    if (imagen.startsWith("content://")) {
                        Glide.with(getContext())
                                .load(Uri.parse(imagen))
                                .into(binding.ivFoto);
                    } else {
                        Glide.with(getContext())
                                .load(ApiClient.BASE_URL + imagen)
                                .into(binding.ivFoto);
                    }
                }

            }
        });
        abrirGaleria();
        binding.btCambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arlImagen.launch(intent);
            }
        });
        mv.getUriMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.ivFoto.setImageURI(uri);
            }
        });

        binding.btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mascota.setNombre(binding.etNombreMascota.getText().toString());
                mascota.setEspecie(binding.etEspecie.getText().toString());
                mascota.setRaza(binding.etRaza.getText().toString());
                mascota.setEdad(Integer.parseInt(binding.etEdad.getText().toString()));
                mascota.setSexo(binding.etSexo.getText().toString());
                mascota.setPeso(Integer.parseInt(binding.etPeso.getText().toString()));
                Uri uri = mv.getUriMutableLiveData().getValue();
                mascota.setImagen(uri.toString());
                mascota.setEstado(1);
                mv.actualizarMascota(mascota);
            }
        });

        mv.recuperarMascota(getArguments());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mv = new ViewModelProvider(this).get(DetalleMascotaViewModel.class);
        // TODO: Use the ViewModel
    }
    private void abrirGaleria() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        arlImagen = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                mv.recibirFoto(result);
            }
        });
    }

}