package com.tec.sgvmobile.ui.turnos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tec.sgvmobile.R;
import com.tec.sgvmobile.databinding.FragmentDetalleTurnosBinding;
import com.tec.sgvmobile.models.Mascota;
import com.tec.sgvmobile.models.Turno;

import java.util.List;

public class DetalleTurnosFragment extends Fragment {

    private FragmentDetalleTurnosBinding binding;
    private DetalleTurnosViewModel vm;
    private Mascota mascota;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetalleTurnosBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(DetalleTurnosViewModel.class);
        FloatingActionButton fab = requireActivity().findViewById(R.id.btAgregar);
        if (fab != null) {
            fab.hide();
        }
        mascota = (Mascota) getArguments().getSerializable("mascotaBundle");
        vm.cargarTurnosFuturos(mascota.getId());
        binding.tvTituloMascota.setText("Turnos de " + mascota.getNombre());

        vm.getTurnosFuturos().observe(getViewLifecycleOwner(), new Observer<List<Turno>>() {
            @Override
            public void onChanged(List<Turno> turnos) {
                TurnoAdapter adapter = new TurnoAdapter(getContext(), turnos, mascota, getLayoutInflater());
                binding.listaTurnos.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.listaTurnos.setAdapter(adapter);
            }
        });
        return binding.getRoot();
    }
}
