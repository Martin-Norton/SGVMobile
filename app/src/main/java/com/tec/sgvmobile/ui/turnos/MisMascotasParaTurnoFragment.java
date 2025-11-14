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
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tec.sgvmobile.R;
import com.tec.sgvmobile.databinding.FragmentMisMascotasParaTurnoBinding;
import com.tec.sgvmobile.models.Mascota;

import java.util.List;

public class MisMascotasParaTurnoFragment extends Fragment {

    private FragmentMisMascotasParaTurnoBinding binding;
    private MisMascotasParaTurnoViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(MisMascotasParaTurnoViewModel.class);
        binding = FragmentMisMascotasParaTurnoBinding.inflate(inflater, container, false);
        FloatingActionButton fab = requireActivity().findViewById(R.id.btAgregar);
        if (fab != null) {
            fab.hide();
        }
        View root = binding.getRoot();

        vm.getMascotas().observe(getViewLifecycleOwner(), new Observer<List<Mascota>>() {
            @Override
            public void onChanged(List<Mascota> mascotas) {
                TodasLasMascotasAdapter tlma = new TodasLasMascotasAdapter(getContext(), mascotas, getLayoutInflater());
                GridLayoutManager glm = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                binding.listaMascotast.setLayoutManager(glm);
                binding.listaMascotast.setAdapter(tlma);
            }
        });

        vm.cargarMascotas();
        return root;
    }
}
