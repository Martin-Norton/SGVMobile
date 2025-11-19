package com.tec.sgvmobile.ui.tratamientos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tec.sgvmobile.R;
import com.tec.sgvmobile.databinding.FragmentTratamientosBinding;
import com.tec.sgvmobile.models.Mascota;
import com.tec.sgvmobile.ui.turnos.MascotasConTurnosAdapter;

import java.util.List;

public class TratamientosFragment extends Fragment {

    private FragmentTratamientosBinding binding;
    private TratamientosViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(TratamientosViewModel.class);
        binding = FragmentTratamientosBinding.inflate(inflater, container, false);

        FloatingActionButton fab = requireActivity().findViewById(R.id.btAgregar);
        fab.hide();

        View root = binding.getRoot();

        vm.getListaMascotasConTratamientos().observe(getViewLifecycleOwner(), new Observer<List<Mascota>>() {
            @Override
            public void onChanged(List<Mascota> mascotasTratamientos) {
                MascotasConTratamientosAdapter a = new MascotasConTratamientosAdapter(getContext(), mascotasTratamientos, getLayoutInflater());
                GridLayoutManager glm = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                binding.listaMascotasTratamientos.setLayoutManager(glm);
                binding.listaMascotasTratamientos.setAdapter(a);
            }
        });
        vm.obtenerListaMascotasConTratamientos();
        return root;
    }

}