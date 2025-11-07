package com.tec.sgvmobile.models;

import java.io.Serializable;

public class Dueno implements Serializable {
    private int id;
    private int idUsuario;

    public Dueno() {
    }

    public Dueno(int id, int idUsuario) {
        this.id = id;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

}