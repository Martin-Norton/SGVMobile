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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tec.sgvmobile.R;
import com.tec.sgvmobile.databinding.FragmentCrearMascotaBinding;

public class CrearMascotaFragment extends Fragment {

    private CrearMascotaViewModel mv;
    private FragmentCrearMascotaBinding binding;
    private Intent intent;
    private ActivityResultLauncher<Intent> arl;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = requireActivity().findViewById(R.id.btAgregar);
        fab.hide();

        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Agregar Mascota");
        binding = FragmentCrearMascotaBinding.inflate(inflater, container, false);
        mv = new ViewModelProvider(this).get(CrearMascotaViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        abrirGaleria();
        binding.btBuscarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arl.launch(intent);
            }
        });
        binding.btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarMascota();
            }
        });
        mv.getUriMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                Glide.with(requireContext())
                        .load(uri)
                        .centerCrop()
                        .into(binding.ivFoto);
            }
        });
        mv.getMascotaCreada().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_crearMascotaFragment_to_nav_mascotas);
            }
        });
    }
    public void cargarMascota(){
        String nombre = binding.etNombreMascota.getText().toString();
        String especie = binding.etEspecie.getText().toString();
        String raza = binding.etRaza.getText().toString();
        String edad = binding.etEdad.getText().toString();
        String peso = binding.etPeso.getText().toString();
        String sexo =binding.etSexo.getText().toString();
        mv.guardarMascota(nombre, especie, raza, edad, peso, sexo);
    }
    private void abrirGaleria() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                mv.recibirFoto(result);
            }
        });
    }

}