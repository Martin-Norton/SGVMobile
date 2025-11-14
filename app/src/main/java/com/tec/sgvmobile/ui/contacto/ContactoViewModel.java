package com.tec.sgvmobile.ui.contacto;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class ContactoViewModel extends AndroidViewModel {

    private final String telefonoFijo = "2614964012";
    private final String whatsapp = "+5492665119568";
    private final String direccion = "Av. Bandera de Los Andes 4013";

    public ContactoViewModel(@NonNull Application application) {
        super(application);
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public String getDireccion() {
        return direccion;
    }

    public Intent getIntentLlamar() {
        Uri uri = Uri.parse("tel:" + telefonoFijo);
        return new Intent(Intent.ACTION_DIAL, uri);
    }

    public Intent getIntentMaps() {
        String uri = "geo:0,0?q=" + direccion;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        return intent;
    }

    public Intent getIntentWhatsapp() {
        Uri uri = Uri.parse("https://wa.me/" + whatsapp);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.whatsapp");

        PackageManager pm = getApplication().getPackageManager();

        if (intent.resolveActivity(pm) != null) {
            return intent;
        }

        return new Intent(Intent.ACTION_VIEW, uri);
    }
}
