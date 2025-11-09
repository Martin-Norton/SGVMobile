package com.tec.sgvmobile.ui.logout;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.tec.sgvmobile.R;

public class LogoutFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mostrarDialogoSalida();
    }

    private void mostrarDialogoSalida() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirmar salida")
                .setMessage("¿Seguro que deseas salir de la aplicación?")
                .setPositiveButton("Sí, quiero salir", (dialog, which) -> {
                    requireActivity().finishAffinity();
                    System.exit(0);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                            .navigate(R.id.nav_inicio);
                })
                .show();
    }
}
