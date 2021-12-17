package com.example.tricoenvironment.airlity;

public class Usuario {
    private String _id, nombreUsuario, correo, contrasenya;
    private int telefono;

    public Usuario() {
    }

    public Usuario(String correo, String contrasenya) {
        this.correo = correo;
        this.contrasenya = contrasenya;
    }

    public Usuario(String nombreUsuario, String correo, int telefono) {
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.telefono = telefono;
    }

    public Usuario(String nombreUsuario, String correo, String contrasenya, int telefono) {
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contrasenya = contrasenya;
        this.telefono = telefono;
    }

    public Usuario(String nombreUsuario, String correo, String contrasenya) {
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contrasenya = contrasenya;
    }

    public Usuario(String _id, String nombreUsuario, String correo, String contrasenya, int telefono) {
        this._id = _id;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contrasenya = contrasenya;
        this.telefono = telefono;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "{" +
                "\"nombreUsuario\":" + "\"" +nombreUsuario + "\"" +
                ", \"correo\":" +"\"" +correo + "\"" +
                ", \"contrasenya\":" +"\"" + contrasenya + "\"" +
                ", \"telefono\":"  + telefono +
                "}";
    }
}
