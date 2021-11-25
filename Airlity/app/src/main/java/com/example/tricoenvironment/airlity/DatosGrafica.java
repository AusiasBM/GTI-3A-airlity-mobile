/**
 * DatosGrafica.java
 * @fecha: 23/11/2021
 * @autor: Aitor Benítez Estruch
 *
 * @Descripcion:
 * POJO para almacenar las listas de fechas y de medidas para mostrar en un gráfico
 *
 */


package com.example.tricoenvironment.airlity;

import java.util.ArrayList;

public class DatosGrafica {
    private ArrayList<Long> fechas = new ArrayList<>();
    private ArrayList<Double> medias = new ArrayList<>();

    /**
     * Constructor
     */
    public DatosGrafica() {
    }


    /**
     *Getters y setters
     */
    public ArrayList<Long> getFechas() {
        return fechas;
    }

    public void setFechas(ArrayList<Long> fechas) {
        this.fechas = fechas;
    }

    public ArrayList<Double> getMedias() {
        return medias;
    }

    public void setMedias(ArrayList<Double> medias) {
        this.medias = medias;
    }

    /**
     * Método toString()
     */
    @Override
    public String toString() {
        return "{" +
                "fechas=" + fechas +
                ", medias=" + medias +
                '}';
    }
}
