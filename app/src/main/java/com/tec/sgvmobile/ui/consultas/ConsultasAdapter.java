package com.tec.sgvmobile.ui.consultas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tec.sgvmobile.R;
import com.tec.sgvmobile.models.Consulta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConsultasAdapter extends RecyclerView.Adapter<ConsultasAdapter.ViewHolderConsulta> {

    private Context context;
    private List<Consulta> listado;
    private LayoutInflater li;

    public ConsultasAdapter(Context context, List<Consulta> listado, LayoutInflater li) {
        this.context = context;
        this.listado = listado;
        this.li = li;
    }

    @NonNull
    @Override
    public ConsultasAdapter.ViewHolderConsulta onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = li.inflate(R.layout.item_consulta, parent, false);
        return new ConsultasAdapter.ViewHolderConsulta(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultasAdapter.ViewHolderConsulta holder, int position) {
        Consulta consultaActual = listado.get(position);

        holder.tvFechaConsulta.setText("Fecha: " + consultaActual.getFecha());
        holder.tvMotivoConsulta.setText("Motivo: " + consultaActual.getMotivo());
        holder.tvDiagnosticoConsulta.setText("Diagnóstico: " + consultaActual.getDiagnostico());
        holder.tvTratamientoConsulta.setText("Tratamiento: " + consultaActual.getTratamiento());
        holder.tvVeterinarioConsulta.setText("Veterinario ID: " + consultaActual.getId_Veterinario());
    }

    @Override
    public int getItemCount() {
        return listado.size();
    }

    public class ViewHolderConsulta extends RecyclerView.ViewHolder {
        TextView tvFechaConsulta, tvMotivoConsulta, tvDiagnosticoConsulta, tvTratamientoConsulta, tvVeterinarioConsulta;
        public ViewHolderConsulta(@NonNull View itemView) {
            super(itemView);
            tvFechaConsulta = itemView.findViewById(R.id.tvFechaConsulta);
            tvMotivoConsulta = itemView.findViewById(R.id.tvMotivoConsulta);
            tvDiagnosticoConsulta = itemView.findViewById(R.id.tvDiagnosticoConsulta);
            tvTratamientoConsulta = itemView.findViewById(R.id.tvTratamientoConsulta);
            tvVeterinarioConsulta = itemView.findViewById(R.id.tvVeterinarioConsulta);
        }
    }
}
