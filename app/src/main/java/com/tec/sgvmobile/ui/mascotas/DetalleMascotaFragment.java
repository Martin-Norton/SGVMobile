package com.tec.sgvmobile.ui.mascotas;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tec.sgvmobile.R;
import com.tec.sgvmobile.databinding.FragmentDetalleMascotaBinding;
import com.tec.sgvmobile.models.Mascota;

public class DetalleMascotaFragment extends Fragment {

    private ActivityResultLauncher<Intent> arlCamara;
    private static final int codigoPermisoCamaraDetalle = 101;
    private static final int codigoPermisoGaleriaDetalle = 1001;

    private FragmentDetalleMascotaBinding binding;
    private DetalleMascotaViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleMascotaBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(DetalleMascotaViewModel.class);
        FloatingActionButton fab = requireActivity().findViewById(R.id.btAgregar);
        fab.hide();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Detalles de la Mascota");

        arlCamara = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        vm.recibirFoto(result);
                    }
                });

        binding.btCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.verificarPermisoCamara(DetalleMascotaFragment.this);
            }
        });
        vm.getSolicitarPermisoCamara().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean solicitar) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.CAMERA}, codigoPermisoCamaraDetalle);

            }
        });
        vm.getIntentCamara().observe(getViewLifecycleOwner(), new Observer<Intent>() {
            @Override
            public void onChanged(Intent intent) {
                arlCamara.launch(intent);
            }
        });

        vm.getMascota().observe(getViewLifecycleOwner(), new Observer<Mascota>() {
            @Override
            public void onChanged(Mascota mascota) {
                if (mascota == null) return;
                binding.etNombreMascota.setText(mascota.getNombre());
                binding.etEspecie.setText(mascota.getEspecie());
                binding.etRaza.setText(mascota.getRaza());
                binding.etEdad.setText(String.valueOf(mascota.getEdad()));
                binding.etPeso.setText(String.valueOf(mascota.getPeso()));
                binding.etSexo.setText(mascota.getSexo());
                vm.mostrarImagen(binding.ivFoto, mascota.getImagen());
            }
        });

        vm.getMEstado().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean estado) {
                binding.etNombreMascota.setEnabled(estado);
                binding.etEspecie.setEnabled(estado);
                binding.etRaza.setEnabled(estado);
                binding.etEdad.setEnabled(estado);
                binding.etPeso.setEnabled(estado);
                binding.etSexo.setEnabled(estado);
                binding.btBuscarFoto.setEnabled(estado);
                binding.btCamara.setEnabled(estado);
            }
        });

        vm.getMTexto().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String texto) {
                binding.btEditar.setText(texto);
            }
        });

        binding.btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.cambioBoton(
                        binding.btEditar.getText().toString(),
                        binding.etNombreMascota.getText().toString(),
                        binding.etEspecie.getText().toString(),
                        binding.etRaza.getText().toString(),
                        binding.etEdad.getText().toString(),
                        binding.etPeso.getText().toString(),
                        binding.etSexo.getText().toString(),
                        "1"
                );
            }
        });

        binding.btBuscarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        Bundle bundle = getArguments();
        Mascota mascota = (Mascota) bundle.get("mascotaBundle");
        vm.inicializarMascota(mascota);

        return binding.getRoot();
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, codigoPermisoGaleriaDetalle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        vm.setNuevaImagenUri(uri);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
