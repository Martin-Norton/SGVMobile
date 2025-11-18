
package com.tec.sgvmobile.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.tec.sgvmobile.R;
import com.tec.sgvmobile.models.Publicidad;

import java.util.List;

public class InicioFragment extends Fragment {

    private InicioViewModel vm;
    private ViewPager2 vp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_inicio2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vm = new ViewModelProvider(this).get(InicioViewModel.class);
        vp = view.findViewById(R.id.listaPublicidad);

        vm.getPublicidades().observe(getViewLifecycleOwner(), new Observer<List<Publicidad>>() {
            @Override
            public void onChanged(List<Publicidad> publicidades) {
                if (publicidades != null && !publicidades.isEmpty()) {
                    PublicidadAdapter adapter = new PublicidadAdapter(publicidades, getContext());
                    vp.setAdapter(adapter);
                }
            }
        });

        vm.cargarPublicidades();
    }
}
