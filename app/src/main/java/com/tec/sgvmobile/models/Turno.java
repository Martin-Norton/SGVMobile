package com.tec.sgvmobile.models;

import java.io.Serializable;
import java.util.Date;

public class Turno implements Serializable {
    private int id;
    private int id_Mascota;
    private String motivo;
    private Date fecha;
    private String hora;
    private int estado;

    public Turno() {
    }

    public Turno(int id, int id_Mascota, String motivo, Date fecha, String hora, int estado) {
        this.id = id;
        this.id_Mascota = id_Mascota;
        this.motivo = motivo;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_Mascota() {
        return id_Mascota;
    }

    public void setId_Mascota(int id_Mascota) {
        this.id_Mascota = id_Mascota;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
