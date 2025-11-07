package com.tec.sgvmobile.models;

import java.io.Serializable;

public class Mascota implements Serializable {
    private int id;
    private String nombre;
    private String especie;
    private String raza;
    private int edad;
    private int peso;
    private String sexo;
    private String imagen;
    private int id_Dueno;
    private int estado;

    public Mascota() {
    }

    public Mascota(int id, String nombre, String especie, String raza, int edad, int peso, String sexo, String imagen, int id_Dueno, int estado) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;
        this.peso = peso;
        this.sexo = sexo;
        this.imagen = imagen;
        this.id_Dueno = id_Dueno;
        this.estado = estado;
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

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public int getId_Dueno() {
        return id_Dueno;
    }

    public void setId_Dueno(int id_Dueno) {
        this.id_Dueno = id_Dueno;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
