package com.example.tricoenvironment.airlity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Estacion {
    String nombreEstacion;
    String fotoEstacion;
    LatLng posicionEstacion;
    String codigoEstacion;
    ArrayList<Medicion> mediciones;

    public Estacion() {
    }

    public Estacion(String nombreEstacion, String fotoEstacion, LatLng posicionEstacion, String codigoEstacion) {
        this.nombreEstacion = nombreEstacion;
        this.fotoEstacion = fotoEstacion;
        this.posicionEstacion = posicionEstacion;
        this.codigoEstacion = codigoEstacion;
    }

    public String getCodigoEstacion() {
        return codigoEstacion;
    }

    public void setCodigoEstacion(String codigoEstacion) {
        this.codigoEstacion = codigoEstacion;
    }

    public String getNombreEstacion() {
        return nombreEstacion;
    }

    public void setNombreEstacion(String nombreEstacion) {
        this.nombreEstacion = nombreEstacion;
    }

    public String getFotoEstacion() {
        return fotoEstacion;
    }

    public void setFotoEstacion(String fotoEstacion) {
        this.fotoEstacion = fotoEstacion;
    }

    public LatLng getPosicionEstacion() {
        return posicionEstacion;
    }

    public void setPosicionEstacion(LatLng posicionEstacion) {
        this.posicionEstacion = posicionEstacion;
    }

    public ArrayList<Medicion> getMediciones() {
        return mediciones;
    }

    public void anyadirMedicion(String tipoMedicion, double medida, long fecha){
        Medicion medici = new Medicion(tipoMedicion, medida, fecha);
        mediciones.add(medici);
    }

    @Override
    public String toString() {
        return "Estacion{" +
                "nombreEstacion='" + nombreEstacion + '\'' +
                ", fotoEstacion='" + fotoEstacion + '\'' +
                ", posicionEstacion=" + posicionEstacion +
                ", codigoEstacion=" + codigoEstacion +
                '}';
    }
}


