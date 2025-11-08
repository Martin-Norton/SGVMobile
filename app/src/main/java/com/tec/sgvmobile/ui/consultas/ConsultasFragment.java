package com.tec.sgvmobile.ui.consultas;
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
import com.tec.sgvmobile.databinding.FragmentConsultasBinding;
import com.tec.sgvmobile.databinding.FragmentTurnosBinding;
import com.tec.sgvmobile.models.Mascota;
import com.tec.sgvmobile.ui.consultas.MascotasConConsultasAdapter;

import java.util.List;

public class ConsultasFragment extends Fragment {
    private FragmentConsultasBinding binding;
    private ConsultasViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ConsultasViewModel.class);
        binding = FragmentConsultasBinding.inflate(inflater, container, false);
        FloatingActionButton fab = requireActivity().findViewById(R.id.btAgregar);
        fab.hide();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Mis mascotas con consultas");
        View root = binding.getRoot();

        vm.getListaMascotasConConsultas().observe(getViewLifecycleOwner(), new Observer<List<Mascota>>() {
            @Override
            public void onChanged(List<Mascota> mascotasConsultas) {
                MascotasConConsultasAdapter ca = new MascotasConConsultasAdapter(getContext(), mascotasConsultas, getLayoutInflater());
                GridLayoutManager glm = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                binding.reciclerMascotasConConsultas.setLayoutManager(glm);
                binding.reciclerMascotasConConsultas.setAdapter(ca);
            }
        });
        vm.obtenerListaMascotasConConsultas();
        return root;
    }
}