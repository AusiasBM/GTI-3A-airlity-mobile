package com.example.tricoenvironment.airlity;

import static java.lang.System.currentTimeMillis;

public class Sensor {
    private String  macSensor,correoUsuario, tipoMedicion;
    private long fechaRegistro;

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getMac() {
        return macSensor;
    }

    public void setMac(String mac) {
        this.macSensor = mac;
    }



    public long getFecha() {
        return fechaRegistro;
    }

    /**
     * Método setFecha. Este método coge la fecha en milisegundos del momento en que se llama.
     *
     * setFecha() ->
     *
     */
    public void setFecha() {
        this.fechaRegistro = currentTimeMillis();
    }

    /**
     * Método setFecha. Este método se le pasa la fecha en milisegundos. Se utilizará para crear
     * objetos de tipo Medicion a partir de JSON.
     *
     * fecha: N -> setFecha() ->
     *
     */
    public void setFecha(long fecha) {
        this.fechaRegistro = fecha;
    }

    public String getTipoMedicion() {
        return tipoMedicion;
    }

    public void setTipoMedicion(String tipoMedicion) {
        this.tipoMedicion = tipoMedicion;
    }

    public Sensor() {
    }

    public Sensor(String mac, String tipoMedicion) {
        this.macSensor = mac;
        this.tipoMedicion = tipoMedicion;
    }

    public Sensor(String macSensor, String correoUsuario, String tipoMedicion, long fechaRegistro) {
        this.macSensor = macSensor;
        this.correoUsuario = correoUsuario;
        this.tipoMedicion = tipoMedicion;
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "{" +
                "macSensor='" + macSensor + '\'' +
                ", correoUsuario='" + correoUsuario + '\'' +
                ", tipoMedicion='" + tipoMedicion + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}
