package com.tec.sgvmobile.ui.inicio;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.tec.sgvmobile.R;
import com.tec.sgvmobile.models.Publicidad;
import java.util.List;
public class PublicidadAdapter extends RecyclerView.Adapter<PublicidadAdapter.ViewHolder> {

    private List<Publicidad> lista;
    private Context context;

    public PublicidadAdapter(List<Publicidad> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_publicidad, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Publicidad p = lista.get(position);

        holder.tvTitulo.setText(p.getTitulo());
        holder.tvDesc.setText(p.getDescripcion());

        if (p.getImagenes() != null && !p.getImagenes().isEmpty()) {
            ImagenesPublicidadAdapter imgAdapter =
                    new ImagenesPublicidadAdapter(p.getImagenes(), context);

            holder.vpImagenes.setAdapter(imgAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewPager2 vpImagenes;
        TextView tvTitulo, tvDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            vpImagenes = itemView.findViewById(R.id.vpImagenesPublicidad);
            tvTitulo = itemView.findViewById(R.id.tvTituloPublicidad);
            tvDesc = itemView.findViewById(R.id.tvDescripcionPublicidad);
        }
    }
}
