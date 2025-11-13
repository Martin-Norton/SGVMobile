package com.tec.sgvmobile.ui.mascotas;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tec.sgvmobile.R;
import com.tec.sgvmobile.databinding.FragmentCrearMascotaBinding;

public class CrearMascotaFragment extends Fragment {

    private CrearMascotaViewModel mv;
    private FragmentCrearMascotaBinding binding;
    private ActivityResultLauncher<Intent> arlCamara;
    private static final int codigoPermisoCamaraCrear = 102;
    private static final int codigoPermisoGaleriaCrear = 1002;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = requireActivity().findViewById(R.id.btAgregar);
        fab.hide();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Agregar mascota");
        binding = FragmentCrearMascotaBinding.inflate(inflater, container, false);
        mv = new ViewModelProvider(this).get(CrearMascotaViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<String> adapterSexo = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Macho", "Hembra"}
        );
        adapterSexo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spSexo.setAdapter(adapterSexo);
        ArrayAdapter<String> adapterEspecie = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Canino", "Felino", "Roedor", "Ave", "Conejo", "Reptil"}
        );
        adapterSexo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spEspecie.setAdapter(adapterEspecie);

        arlCamara = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        mv.recibirFoto(result);
                    }
                });

        binding.btCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mv.verificarPermisoCamara(CrearMascotaFragment.this);
            }
        });

        mv.getSolicitarPermisoCamara().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean solicitar) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.CAMERA}, codigoPermisoCamaraCrear);

            }
        });
        mv.getUriMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                Glide.with(requireContext())
                        .load(uri)
                        .circleCrop()
                        .into(binding.ivFoto);
            }
        });

        mv.getIntentCamara().observe(getViewLifecycleOwner(), new Observer<Intent>() {
            @Override
            public void onChanged(Intent intent) {
                arlCamara.launch(intent);

            }
        });

        binding.btBuscarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        binding.btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mv.guardarMascota(
                        binding.etNombreMascota.getText().toString(),
                        binding.spEspecie.getSelectedItem().toString(),
                        binding.etRaza.getText().toString(),
                        binding.etEdad.getText().toString(),
                        binding.etPeso.getText().toString(),
                        binding.spSexo.getSelectedItem().toString()
                );
            }
        });
        
        mv.getMascotaCreada().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean creada) {
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_crearMascotaFragment_to_nav_mascotas);
            }
        });
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, codigoPermisoGaleriaCrear);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        mv.setNuevaImagenUri(uri);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

