package com.tec.sgvmobile.dtos;

import java.io.Serializable;
import java.util.Date;

public class CrearTurnoDto implements Serializable {
    private int idMascota;
    private String motivo;
    private Date fechaHora;

    public CrearTurnoDto(int idMascota, String motivo, Date fechaHora) {
        this.idMascota = idMascota;
        this.motivo = motivo;
        this.fechaHora = fechaHora;
    }

    public CrearTurnoDto() {
    }

    public int getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }
}
