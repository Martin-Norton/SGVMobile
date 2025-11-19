package com.tec.sgvmobile.ui.turnos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tec.sgvmobile.R;
import com.tec.sgvmobile.databinding.FragmentCrearTurnoBinding;
import com.tec.sgvmobile.models.Mascota;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CrearTurnoFragment extends Fragment {

    private FragmentCrearTurnoBinding binding;
    private CrearTurnoViewModel vm;
    private Mascota mascota;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCrearTurnoBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(CrearTurnoViewModel.class);
        FloatingActionButton fab = requireActivity().findViewById(R.id.btAgregar);
        fab.hide();


        mascota = (Mascota) getArguments().getSerializable("mascotaBundle");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        binding.datePicker.setMinDate(calendar.getTimeInMillis());

        binding.btElegirDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dia = binding.datePicker.getDayOfMonth();
                int mes = binding.datePicker.getMonth();
                int anio = binding.datePicker.getYear();
                Calendar fechaSeleccionada = Calendar.getInstance();
                fechaSeleccionada.set(anio, mes, dia);
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String fechaVista = formato.format(fechaSeleccionada.getTime());
                vm.obtenerHorariosDisponibles(fechaVista);
            }
        });

        vm.getHorarios().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> horarios) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, horarios);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spHorarios.setAdapter(adapter);
            }
        });

        binding.btCrearTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String horarioSeleccionado = (String) binding.spHorarios.getSelectedItem();
                String motivo = binding.etMotivo.getText().toString();
                int dia = binding.datePicker.getDayOfMonth();
                int mes = binding.datePicker.getMonth();
                int anio = binding.datePicker.getYear();
                Calendar fechaTurno = Calendar.getInstance();
                fechaTurno.set(anio, mes, dia);
                String fechaHoraVista = String.format(Locale.getDefault(),
                        "%04d-%02d-%02dT%s:00", anio, mes + 1, dia, horarioSeleccionado);
                vm.crearTurno(mascota.getId(), motivo, fechaHoraVista);
            }
        });
        vm.getCreado().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_crearTurnoFragment_to_turnosFragment);
            }
        });
        return binding.getRoot();
    }
}
