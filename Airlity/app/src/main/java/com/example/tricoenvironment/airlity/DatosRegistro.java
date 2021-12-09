package com.example.tricoenvironment.airlity;

public class DatosRegistro {
    private Usuario usuario;
    private Sensor sensor;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public DatosRegistro(Usuario usuario, Sensor sensor) {
        this.usuario = usuario;
        this.sensor = sensor;
    }

    @Override
    public String toString() {
        return "{" +
                "\"usuario\":" +usuario +
                ", \"sensor\":" +sensor +
                '}';
    }
}
