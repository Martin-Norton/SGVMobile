package com.tec.sgvmobile.ui.turnos;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.tec.sgvmobile.R;
import com.tec.sgvmobile.models.Mascota;
import com.bumptech.glide.Glide;
import com.tec.sgvmobile.request.ApiClient;

import java.util.List;

public class MascotasConTurnosAdapter extends RecyclerView.Adapter<MascotasConTurnosAdapter.viewHolderMascota> {

    private Context context;
    private List<Mascota> listado;
    private LayoutInflater li;

    public MascotasConTurnosAdapter(Context context, List<Mascota> listado, LayoutInflater li) {
        this.context = context;
        this.listado = listado;
        this.li = li;
    }

    @NonNull
    @Override
    public MascotasConTurnosAdapter.viewHolderMascota onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = li.inflate(R.layout.item_mascota, parent, false);
        return new MascotasConTurnosAdapter.viewHolderMascota(itemView);
    }

    @Override// sacar el detallemascota del nav!!!!!!
    public void onBindViewHolder(@NonNull MascotasConTurnosAdapter.viewHolderMascota holder, int position) {
        Mascota mascotaActual = listado.get(position);
        holder.nombre.setText("Nombre: " + mascotaActual.getNombre());
        Glide.with(context)
                .load(ApiClient.BASE_URL + mascotaActual.getImagen())
                .placeholder(R.drawable.inmuebles)
                .error("null")
                .into(holder.imagen);
        ((MascotasConTurnosAdapter.viewHolderMascota) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("mascotaBundle", mascotaActual);
                Navigation.findNavController((Activity) context, R.id.nav_host_fragment_content_main).navigate(R.id.detalleTurnosFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listado.size();
    }

    public class viewHolderMascota extends RecyclerView.ViewHolder {

        TextView nombre;
        ImageView imagen;

        public viewHolderMascota(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombre);
            imagen = itemView.findViewById(R.id.ivImagen);
        }
    }
}


