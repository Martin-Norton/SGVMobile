package com.tec.sgvmobile.ui.mascotas;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tec.sgvmobile.R;
import com.tec.sgvmobile.databinding.FragmentDetalleMascotaBinding;
import com.tec.sgvmobile.models.Mascota;

public class DetalleMascotaFragment extends Fragment {

    private static final int REQUEST_PICK_IMAGE = 1001;

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
                binding.btCambiarFoto.setEnabled(estado);
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

        binding.btCambiarFoto.setOnClickListener(new View.OnClickListener() {
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
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
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
