package com.tec.sgvmobile.ui.logout;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
                .setPositiveButton("Sí", (dialog, which) -> {
                    requireActivity().finishAffinity();
                    System.exit(0);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();

                })
                .show();
    }
}
