package com.example.tricoenvironment.airlity;

import java.util.HashMap;
import java.util.Map;
public class DatosUsuario {
    private String id;
    private String nombreUsuario;
    private String correo;
    private String contrasenya;
    private Integer telefono;
    private Object macSensor;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public Integer getTelefono() {
        return telefono;
    }
    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }
    public Object getMacSensor() {
        return macSensor;
    }
    public void setMacSensor(Object macSensor) {
        this.macSensor = macSensor;
    }
}