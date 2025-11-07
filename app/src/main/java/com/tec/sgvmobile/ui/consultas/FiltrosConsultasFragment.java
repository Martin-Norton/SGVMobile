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

import com.tec.sgvmobile.R;
import com.tec.sgvmobile.databinding.FragmentFiltrosConsultasBinding;
import com.tec.sgvmobile.models.Consulta;

import java.util.List;

public class FiltrosConsultasFragment extends Fragment {

    private FragmentFiltrosConsultasBinding binding;
    private FiltrosConsultasViewModel vm;
    private int idMascota;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFiltrosConsultasBinding.inflate(inflater, container, false);

        vm = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(FiltrosConsultasViewModel.class);

        idMascota = getArguments().getInt("IdMascotaBundle");

        binding.btFiltroMotivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etMotivo.setVisibility(View.VISIBLE);
                binding.etDesde.setVisibility(View.GONE);
                binding.etHasta.setVisibility(View.GONE);
                binding.btBuscarConsultas.setEnabled(true);
                binding.btBuscarConsultas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vm.buscarPorMotivo(idMascota, binding.etMotivo.getText().toString().trim());
                    }
                });
            }
        });

        binding.btFiltroMesActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etMotivo.setVisibility(View.GONE);
                binding.etDesde.setVisibility(View.GONE);
                binding.etHasta.setVisibility(View.GONE);
                binding.btBuscarConsultas.setEnabled(true);

                binding.btBuscarConsultas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vm.buscarEsteMes(idMascota);
                    }
                });
            }
        });

        binding.btFiltroEntreFechas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etMotivo.setVisibility(View.GONE);
                binding.etDesde.setVisibility(View.VISIBLE);
                binding.etHasta.setVisibility(View.VISIBLE);
                binding.btBuscarConsultas.setEnabled(true);

                binding.btBuscarConsultas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vm.buscarEntreFechas(
                                idMascota,
                                binding.etDesde.getText().toString().trim(),
                                binding.etHasta.getText().toString().trim()
                        );
                    }
                });
            }
        });
        vm.getListaConsultas().observe(getViewLifecycleOwner(), new Observer<List<Consulta>>() {
            @Override
            public void onChanged(List<Consulta> consultas) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("consultasBundle", (java.io.Serializable) consultas);
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.resultadosBusquedaFragment, bundle);
            }
        });
        binding.btVolverFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });
        return binding.getRoot();
    }
}
