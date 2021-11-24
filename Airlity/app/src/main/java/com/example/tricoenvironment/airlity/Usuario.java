package com.example.tricoenvironment.airlity;

public class Usuario {
    private String _id, nombreUsuario, correo, contraseña, macSensor;
    private int numero;

    public Usuario() {
    }

    public Usuario(String correo, String contraseña) {
        this.correo = correo;
        this.contraseña = contraseña;
    }

    public Usuario(String nombreUsuario, String correo, int numero) {
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.numero = numero;
    }

    public Usuario(String nombreUsuario, String correo, String contraseña, int numero) {
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contraseña = contraseña;
        this.numero = numero;
    }

    public Usuario(String nombreUsuario, String correo, String contraseña) {
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    public Usuario(String _id, String nombreUsuario, String correo, String contraseña, String macSensor, int numero) {
        this._id = _id;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contraseña = contraseña;
        this.macSensor = macSensor;
        this.numero = numero;
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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getMacSensor() {
        return macSensor;
    }

    public void setMacSensor(String macSensor) {
        this.macSensor = macSensor;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "{" +
                "\"nombreUsuario\":" + "\"" +nombreUsuario + "\"" +
                ", \"correo\":" +"\"" +correo + "\"" +
                ", \"contrasenya\":" +"\"" +contraseña + "\"" +
                ", \"telefono\":"  + numero +
                "}";
    }
}
