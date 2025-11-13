package com.tec.sgvmobile.ui.turnos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.tec.sgvmobile.R;
import com.tec.sgvmobile.models.Mascota;
import com.tec.sgvmobile.models.Turno;
import com.tec.sgvmobile.request.ApiClient;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TurnoAdapter extends RecyclerView.Adapter<TurnoAdapter.ViewHolderTurno> {

    private Context context;
    private List<Turno> listaTurnos;
    private Mascota mascota;
    private LayoutInflater inflater;

    public TurnoAdapter(Context context, List<Turno> listaTurnos, Mascota mascota, LayoutInflater inflater) {
        this.context = context;
        this.listaTurnos = listaTurnos;
        this.mascota = mascota;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolderTurno onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_turno, parent, false);
        return new ViewHolderTurno(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTurno holder, int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Turno turno = listaTurnos.get(position);
        Log.d("TurnoAdapter", "llenando con turno" + turno.getMotivo() );

        holder.tvMotivo.setText("Motivo: " + turno.getMotivo());
        holder.tvFecha.setText("El dia: " + (turno.getFecha() != null ? dateFormat.format(turno.getFecha()) : "-"));
        holder.tvHora.setText("A las: " + turno.getHora() + " hs" != null ? "A las: " + turno.getHora().substring(0,5) + " hs" : "A las: -");
        holder.tvEstado.setText((turno.getEstado() == 1 ? "Activo" : "Cancelado"));

        holder.btCancelarTurno.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Cancelar turno")
                    .setMessage("¿Está seguro que desea cancelar este turno?")
                    .setPositiveButton("Sí", (dialog, which) -> cancelarTurno(turno.getId()))
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return listaTurnos.size();
    }

    private void cancelarTurno(int idTurno) {

        String token = ApiClient.leerToken(inflater.getContext());
        ApiClient.InmoService api = ApiClient.getInmoService();
        Call<Void> call = api.cancelarTurno("Bearer " + token, idTurno);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Turno cancelado correctamente", Toast.LENGTH_LONG).show();
                    NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment_content_main);
                    navController.navigate(R.id.action_detalleTurnosFragment_to_turnosFragment);
                } else {
                    Toast.makeText(context, "Error al cancelar turno", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static class ViewHolderTurno extends RecyclerView.ViewHolder {

        TextView tvMotivo, tvFecha, tvHora, tvEstado;
        Button btCancelarTurno;

        public ViewHolderTurno(@NonNull View itemView) {
            super(itemView);
            tvMotivo = itemView.findViewById(R.id.tvMotivo);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            btCancelarTurno = itemView.findViewById(R.id.btCancelarTurno);
        }
    }
}
