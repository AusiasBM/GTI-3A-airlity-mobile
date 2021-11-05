package com.example.tricoenvironment.airlity;

import static java.lang.System.currentTimeMillis;

public class Sensor {
    private String nombre, mac, uuid;
    private long fecha;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuidSensor) {
        this.uuid = uuidSensor;
    }

    public long getFecha() {
        return fecha;
    }

    /**
     * Método setFecha. Este método coge la fecha en milisegundos del momento en que se llama.
     *
     * setFecha() ->
     *
     */
    public void setFecha() {
        this.fecha = currentTimeMillis();
    }

    /**
     * Método setFecha. Este método se le pasa la fecha en milisegundos. Se utilizará para crear
     * objetos de tipo Medicion a partir de JSON.
     *
     * fecha: N -> setFecha() ->
     *
     */
    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "{" +
                "nombre='" + nombre + '\'' +
                ", mac='" + mac + '\'' +
                ", uuid='" + uuid + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}
