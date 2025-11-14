package com.tec.sgvmobile.ui.turnos;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tec.sgvmobile.R;
import com.tec.sgvmobile.databinding.FragmentTurnosBinding;
import com.tec.sgvmobile.models.Mascota;

import java.util.List;

public class TurnosFragment extends Fragment {
    private FragmentTurnosBinding binding;
    private TurnosViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(TurnosViewModel.class);
        binding = FragmentTurnosBinding.inflate(inflater, container, false);

        FloatingActionButton fab = requireActivity().findViewById(R.id.btAgregar);
        if (fab != null) {
            fab.hide();
        }

        View root = binding.getRoot();

        vm.getListaMascotasConTurnos().observe(getViewLifecycleOwner(), new Observer<List<Mascota>>() {
            @Override
            public void onChanged(List<Mascota> mascotasTurnos) {
                MascotasConTurnosAdapter mcta = new MascotasConTurnosAdapter(getContext(), mascotasTurnos, getLayoutInflater());
                GridLayoutManager glm = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                binding.listaMascotasTurnos.setLayoutManager(glm);
                binding.listaMascotasTurnos.setAdapter(mcta);
            }
        });

        binding.btNuevoTurno2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v)
                        .navigate(R.id.misMascotasParaTurnoFragment);
            }
        });

        vm.obtenerListaMascotasConTurnos();
        return root;
    }
}