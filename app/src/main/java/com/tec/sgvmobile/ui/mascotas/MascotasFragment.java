package com.tec.sgvmobile.ui.mascotas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tec.sgvmobile.R;
import com.tec.sgvmobile.databinding.FragmentMascotasBinding;
import com.tec.sgvmobile.models.Mascota;
import com.tec.sgvmobile.ui.inmuebles.MascotasViewModel;
import com.tec.sgvmobile.ui.mascotas.MascotaAdapter;

import java.util.List;

public class MascotasFragment extends Fragment {

    private FragmentMascotasBinding binding;
    private MascotasViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(MascotasViewModel.class);
        binding = FragmentMascotasBinding.inflate(inflater, container, false);
        FloatingActionButton fab = requireActivity().findViewById(R.id.btAgregar);
        fab.show();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Mis Mascotas");
        View root = binding.getRoot();
        vm.getlistaMascotas().observe(getViewLifecycleOwner(), new Observer<List<Mascota>>() {
            @Override
            public void onChanged(List<Mascota> mascotas) {
                MascotaAdapter ia = new MascotaAdapter(getContext(), mascotas, getLayoutInflater());
                GridLayoutManager glm=new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
                binding.listaMascotas.setLayoutManager(glm);
                binding.listaMascotas.setAdapter(ia);
            }
        });
        vm.obtenerListaMascotas();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}