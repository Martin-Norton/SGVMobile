package com.tec.sgvmobile.ui.tratamientos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
import com.tec.sgvmobile.models.Tratamiento;
import com.tec.sgvmobile.models.Turno;
import com.tec.sgvmobile.request.ApiClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TratamientoAdapter extends RecyclerView.Adapter<TratamientoAdapter.ViewHolderTratamiento> {

    private Context context;
    private List<Tratamiento> listaTratamientos;
    private Mascota mascota;
    private LayoutInflater inflater;

    public TratamientoAdapter(Context context, List<Tratamiento> listaTratamientos, Mascota mascota, LayoutInflater inflater) {
        this.context = context;
        this.listaTratamientos = listaTratamientos;
        this.mascota = mascota;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolderTratamiento onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_tratamiento, parent, false);
        return new ViewHolderTratamiento(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTratamiento holder, int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Tratamiento tratamiento = listaTratamientos.get(position);

        holder.tvNombreTratamiento.setText("Nombre: " + tratamiento.getNombre());
        holder.tvFechaVencimiento.setText("Se vence el dia: " + (tratamiento.getFechaVencimiento() != null ? dateFormat.format(tratamiento.getFechaVencimiento()) : "-"));
        Date fechaVenc = tratamiento.getFechaVencimiento();
        Date hoy = new Date();
        long diffMillis = fechaVenc.getTime() - hoy.getTime();
        long diffDias = diffMillis / (1000 * 60 * 60 * 24);
        String estado;
        if (diffDias < 0) {
            estado = "Vencido";
            holder.tvEstado.setTextColor(Color.RED);
        } else if (diffDias <= 30) {
            estado = "Por vencer";
            holder.tvEstado.setTextColor(Color.YELLOW);
        } else {
            estado = "Al día";
            holder.tvEstado.setTextColor(Color.GREEN);
        }
        holder.tvEstado.setText("Estado: " + estado);
    }

    @Override
    public int getItemCount() {
        return listaTratamientos.size();
    }

    public static class ViewHolderTratamiento extends RecyclerView.ViewHolder {
        TextView tvNombreTratamiento, tvFechaVencimiento, tvEstado;
        public ViewHolderTratamiento(@NonNull View itemView) {
            super(itemView);
            tvNombreTratamiento = itemView.findViewById(R.id.tvNombreTratamiento);
            tvFechaVencimiento = itemView.findViewById(R.id.tvFechaVencimicento);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}
