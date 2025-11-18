package com.tec.sgvmobile.models;

import java.io.Serializable;

public class Consulta implements Serializable {
    private int id;
    private Integer id_Turno;
    private String fecha;
    private String motivo;
    private String diagnostico;
    private String descripcion;
    private int id_Mascota;
    private int id_Veterinario;

    public Consulta() {
    }

    public Consulta(int id, Integer id_Turno, String fecha, String motivo, String diagnostico, String descripcion, int id_Mascota, int id_Veterinario) {
        this.id = id;
        this.id_Turno = id_Turno;
        this.fecha = fecha;
        this.motivo = motivo;
        this.diagnostico = diagnostico;
        this.descripcion = descripcion;
        this.id_Mascota = id_Mascota;
        this.id_Veterinario = id_Veterinario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId_Turno() {
        return id_Turno;
    }

    public void setId_Turno(Integer id_Turno) {
        this.id_Turno = id_Turno;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_Mascota() {
        return id_Mascota;
    }

    public void setId_Mascota(int id_Mascota) {
        this.id_Mascota = id_Mascota;
    }

    public int getId_Veterinario() {
        return id_Veterinario;
    }

    public void setId_Veterinario(int id_Veterinario) {
        this.id_Veterinario = id_Veterinario;
    }
}
