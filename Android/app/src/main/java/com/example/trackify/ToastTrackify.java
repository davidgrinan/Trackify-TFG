package com.example.trackify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastTrackify {

    private ToastTrackify() {
    }

    public static void mostrar(Context context, String mensaje) {
        if (context == null) {
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(context);

        View layout = inflater.inflate(
                R.layout.toast_trackify,
                null,
                false
        );

        TextView texto = layout.findViewById(R.id.txtToast);
        texto.setText(mensaje);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
