package com.tec.sgvmobile.models;

import java.io.Serializable;
import java.lang.String;
import java.util.List;

public class Publicidad implements Serializable {
    private int id;
    private String titulo;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String enlaceURL;

    private List<Imagen> imagenes;
    public Publicidad() {
    }

    public Publicidad(int id, String titulo, String descripcion, String fechaInicio, String fechaFin, String enlaceURL, List<Imagen> imagenes) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.enlaceURL = enlaceURL;
        this.imagenes = imagenes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEnlaceURL() {
        return enlaceURL;
    }

    public void setEnlaceURL(String enlaceURL) {
        this.enlaceURL = enlaceURL;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }
}
