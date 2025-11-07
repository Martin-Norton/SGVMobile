package com.tec.sgvmobile.ui.turnos;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tec.sgvmobile.R;
import com.tec.sgvmobile.models.Mascota;
import com.tec.sgvmobile.models.Turno;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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

        holder.tvNombreMascota.setText(mascota.getNombre());
        holder.tvMotivo.setText(turno.getMotivo());
        holder.tvFecha.setText((turno.getFecha() != null ? dateFormat.format(turno.getFecha()) : "-"));
        holder.tvHora.setText(turno.getHora() != null ? turno.getHora().substring(0,5) : "-");
        holder.tvEstado.setText((turno.getEstado() == 1 ? "Activo" : "Cancelado"));
    }

    @Override
    public int getItemCount() {
        return listaTurnos.size();
    }

    public static class ViewHolderTurno extends RecyclerView.ViewHolder {

        TextView tvNombreMascota, tvMotivo, tvFecha, tvHora, tvEstado;

        public ViewHolderTurno(@NonNull View itemView) {
            super(itemView);
            tvNombreMascota = itemView.findViewById(R.id.tvNombreMascota);
            tvMotivo = itemView.findViewById(R.id.tvMotivo);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}
