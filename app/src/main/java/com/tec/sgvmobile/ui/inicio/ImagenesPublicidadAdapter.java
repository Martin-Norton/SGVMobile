
package com.tec.sgvmobile.ui.inicio;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tec.sgvmobile.R;
import com.tec.sgvmobile.models.Imagen;
import com.tec.sgvmobile.request.ApiClient;

import java.util.List;

public class ImagenesPublicidadAdapter extends RecyclerView.Adapter<ImagenesPublicidadAdapter.ImgHolder> {

    private List<Imagen> imagenes;
    private Context context;

    public ImagenesPublicidadAdapter(List<Imagen> imagenes, Context context) {
        this.imagenes = imagenes;
        this.context = context;
    }

    @NonNull
    @Override
    public ImgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_imagen_publicidad, parent, false);
        return new ImgHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgHolder holder, int position) {
        String url = ApiClient.BASE_URL + imagenes.get(position).getRutaImagen();
        Glide.with(context).load(url).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return imagenes.size();
    }

    public static class ImgHolder extends RecyclerView.ViewHolder {

        ImageView iv;

        public ImgHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.ivImagenPublicidad);
        }
    }
}
