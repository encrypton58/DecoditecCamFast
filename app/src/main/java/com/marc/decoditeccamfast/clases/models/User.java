package com.marc.decoditeccamfast.clases.models;

public class User {

    public String correo;
    public String nombre;
    public String token;

    public User(String correo, String nombre, String token){
        this.correo = correo;
        this.nombre = nombre;
        this.token = token;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
