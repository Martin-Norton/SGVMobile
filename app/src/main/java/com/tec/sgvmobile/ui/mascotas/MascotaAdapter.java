package com.tec.sgvmobile.ui.mascotas;

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

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.viewHolderMascota> {
    private Context context;
    private List<Mascota> listado;
    private LayoutInflater li;

    public MascotaAdapter(Context context, List<Mascota> listado, LayoutInflater li) {
        this.context = context;
        this.listado = listado;
        this.li = li;
    }

    @NonNull
    @Override
    public viewHolderMascota onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = li.inflate(R.layout.item_mascota, parent, false);
        return new viewHolderMascota(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderMascota holder, int position) {
        Mascota mascotaActual = listado.get(position);
        holder.nombre.setText("Nombre: " + mascotaActual.getNombre());
        Glide.with(context)
                .load(ApiClient.BASE_URL + mascotaActual.getImagen())
                .placeholder(R.drawable.inmuebles)
                .error("null")
                .into(holder.imagen);
        ((viewHolderMascota) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("mascotaBundle", mascotaActual);
                Navigation.findNavController((Activity) context, R.id.nav_host_fragment_content_main).navigate(R.id.detalleMascotaFragment, bundle);
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
