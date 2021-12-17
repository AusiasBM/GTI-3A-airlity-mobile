package com.example.tricoenvironment.airlity;

public class DatosScanner {

    private String macSensor, tipoMedicion;

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

    @Override
    public String toString() {
        return "{" +
                "macSensor='" + macSensor + '\'' +
                ", tipoMedicion='" + tipoMedicion + '\'' +
                '}';
    }
}
