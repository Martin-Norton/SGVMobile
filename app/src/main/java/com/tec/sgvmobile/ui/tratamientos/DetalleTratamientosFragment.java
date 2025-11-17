package com.tec.sgvmobile.ui.tratamientos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tec.sgvmobile.R;
import com.tec.sgvmobile.databinding.FragmentDetalleTratamientosBinding;
import com.tec.sgvmobile.databinding.FragmentDetalleTurnosBinding;
import com.tec.sgvmobile.models.Mascota;
import com.tec.sgvmobile.models.Tratamiento;
import com.tec.sgvmobile.models.Turno;
import com.tec.sgvmobile.ui.turnos.DetalleTurnosViewModel;
import com.tec.sgvmobile.ui.turnos.TurnoAdapter;

import java.util.List;

public class DetalleTratamientosFragment extends Fragment {

    private FragmentDetalleTratamientosBinding binding;
    private DetalleTratamientosViewModel vm;
    private Mascota mascota;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetalleTratamientosBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(DetalleTratamientosViewModel.class);
        FloatingActionButton fab = requireActivity().findViewById(R.id.btAgregar);
        if (fab != null) {
            fab.hide();
        }
        mascota = (Mascota) getArguments().getSerializable("mascotaBundle");
        vm.cargarTratamientos(mascota.getId());
        binding.tvTituloTratamiento.setText("Tratamientos de " + mascota.getNombre());

        vm.getMtratamientos().observe(getViewLifecycleOwner(), new Observer<List<Tratamiento>>() {
            @Override
            public void onChanged(List<Tratamiento> tratamientos) {
                TratamientoAdapter adapter = new TratamientoAdapter(getContext(), tratamientos, mascota, getLayoutInflater());
                binding.listaTratamientos.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.listaTratamientos.setAdapter(adapter);
            }
        });
        return binding.getRoot();
    }
}