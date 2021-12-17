package com.example.tricoenvironment.airlity;

public class MedicionMapa {

    private String macSensor, tipoMedicion;
    private long fecha;
    private double medida, latitud, longitud;

    public MedicionMapa(){ }

    public String getMacSensor() {
        return macSensor;
    }

    public void setMacSensor(String macSensor) {
        this.macSensor = macSensor;
    }

    public String getTipoMedicion() {
        return tipoMedicion;
    }

    public void setTipoMedicion(String tipoMedicion) {
        this.tipoMedicion = tipoMedicion;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public double getMedida() {
        return medida;
    }

    public void setMedida(double medida) {
        this.medida = medida;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "{" +
                "macSensor='" + macSensor + '\'' +
                ", tipoMedicion='" + tipoMedicion + '\'' +
                ", fecha=" + fecha +
                ", medida=" + medida +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}
