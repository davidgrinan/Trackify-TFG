package com.example.trackify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import API.ContenidoModel;

public class ContenidoAdapter extends BaseAdapter {

    private Context context;
    private List<ContenidoModel> contenidos;

    public ContenidoAdapter(Context context, List<ContenidoModel> contenidos) {
        this.context = context;
        this.contenidos = contenidos;
    }

    @Override
    public int getCount() {
        return contenidos.size();
    }

    @Override
    public Object getItem(int position) {
        return contenidos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contenidos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_contenido, parent, false);
        }

        ImageView imgPortada = convertView.findViewById(R.id.imgPortada);
        TextView tvTitulo = convertView.findViewById(R.id.tvTitulo);
        TextView tvGenero = convertView.findViewById(R.id.tvGenero);
        TextView tvEstado = convertView.findViewById(R.id.tvEstado);
        TextView tvValoracion = convertView.findViewById(R.id.tvValoracion);

        ContenidoModel contenido = contenidos.get(position);

        tvTitulo.setText(contenido.getTitulo());
        tvGenero.setText(contenido.getGenero());
        tvEstado.setText(contenido.getEstado());

        android.content.SharedPreferences preferences =
                context.getSharedPreferences(AjustesActivity.PREFS_NAME, Context.MODE_PRIVATE);

        String valoracionMaxima =
                preferences.getString(
                        AjustesActivity.KEY_VALORACION_MAXIMA,
                        AjustesActivity.VALORACION_5
                );

        if (contenido.getValoracion() != null) {
            int valoracion = contenido.getValoracion();

            if (valoracionMaxima.equals(AjustesActivity.VALORACION_10)) {
                tvValoracion.setText("⭐ " + (valoracion * 2) + "/10");
            } else {
                tvValoracion.setText("⭐ " + valoracion + "/5");
            }
        } else {
            tvValoracion.setText(getStringSeguro(context, R.string.sin_valoracion));
        }

        if (contenido.getImagenUrl() != null && !contenido.getImagenUrl().isEmpty()) {
            Glide.with(context)
                    .load(contenido.getImagenUrl())
                    .placeholder(R.drawable.logo_trackify)
                    .error(R.drawable.logo_trackify)
                    .into(imgPortada);
        } else {
            imgPortada.setImageResource(R.drawable.logo_trackify);
        }

        return convertView;
    }
    private String getStringSeguro(Context context, int id) {
        return context.getString(id);
    }
}