package com.tec.sgvmobile.models;

import java.io.Serializable;
import java.util.Date;

public class Tratamiento implements Serializable {
    private int id;
    private String nombre;
    private String descripcion;
    private int duracion_en_dias;
    private Date fechaVencimiento;

    public Tratamiento() {
    }

    public Tratamiento(int id, String nombre, String descripcion, int duracion_en_dias, Date fechaVencimiento) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion_en_dias = duracion_en_dias;
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracion_en_dias() {
        return duracion_en_dias;
    }

    public void setDuracion_en_dias(int duracion_en_dias) {
        this.duracion_en_dias = duracion_en_dias;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}
