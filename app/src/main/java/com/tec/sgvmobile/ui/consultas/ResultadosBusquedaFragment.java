package com.tec.sgvmobile.ui.consultas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tec.sgvmobile.R;
import com.tec.sgvmobile.databinding.FragmentResultadosBusquedaBinding;
import com.tec.sgvmobile.models.Consulta;

import java.util.List;

public class ResultadosBusquedaFragment extends Fragment {

    private FragmentResultadosBusquedaBinding binding;
    private ResultadosBusquedaViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ResultadosBusquedaViewModel.class);
        binding = FragmentResultadosBusquedaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vm.getListaConsultas().observe(getViewLifecycleOwner(), new Observer<List<Consulta>>() {
            @Override
            public void onChanged(List<Consulta> consultas) {
                ConsultasAdapter adapter = new ConsultasAdapter(getContext(), consultas, getLayoutInflater());
                LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                binding.listaConsultas.setLayoutManager(lm);
                binding.listaConsultas.setAdapter(adapter);
            }
        });
        List<Consulta> consultas = (List<Consulta>) getArguments().getSerializable("consultasBundle");
        vm.setConsultas(consultas);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
