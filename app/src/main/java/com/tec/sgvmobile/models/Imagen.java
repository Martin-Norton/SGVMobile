package com.tec.sgvmobile.models;

import java.io.Serializable;

public class Imagen implements Serializable {
    private String rutaImagen;
    private int orden;

    public Imagen() {
    }

    public Imagen(String rutaImagen, int orden) {
        this.rutaImagen = rutaImagen;
        this.orden = orden;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
}
