/**
 * AdvertenciaMedicion.java
 * @fecha: 23/11/2021
 * @autor: Aitor Benítez Estruch
 *
 * @Descripcion:
 * POJO para almacenar las estadisticas generadas en el backend para un determinado periodo de
 * tiempo. Estas estadísticas constan de la media ponderada por tiempo de la exposición a un gas,
 * el valor máximo de exposición a ese gas, el tiempo que el usuario ha estado midiendo y registrando
 * mediciones durante ese periodo, así como una lista de advertencias (ver POJO AdvertenciaMedicion).
 *
 */

package com.example.tricoenvironment.airlity;

import java.util.ArrayList;

public class EstadisticasMediciones {
    private double valorMaximo, media;
    private long tiempo;
    private String valoracionCalidadAire, tipoGas;
    private ArrayList<AdvertenciaMedicion> advertencias = new ArrayList<>();

    /**
     * Constructor
     */
    public EstadisticasMediciones(){ }


    /**
     * Getters y setters
     */
    public double getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(double valorMaximo) {
        this.valorMaximo = valorMaximo;
    }

    public double getMedia() {
        return media;
    }

    public void setMedia(double media) {
        this.media = media;
    }

    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }

    public ArrayList<AdvertenciaMedicion> getAdvertencias() {
        return advertencias;
    }

    public void setAdvertencias(ArrayList<AdvertenciaMedicion> advertencias) {
        this.advertencias = advertencias;
    }

    public String getValoracionCalidadAire() {
        return valoracionCalidadAire;
    }

    public void setValoracionCalidadAire(String valoracionCalidadAire) {
        this.valoracionCalidadAire = valoracionCalidadAire;
    }

    public String getTipoGas() {
        return tipoGas;
    }

    public void setTipoGas(String tipoGas) {
        this.tipoGas = tipoGas;
    }

    /**
     * Método toString()
     */
    @Override
    public String toString() {
        return "{" +
                "valorMaximo=" + valorMaximo +
                ", media=" + media +
                ", tiempo=" + tiempo +
                ", valoracionCalidadAire='" + valoracionCalidadAire + '\'' +
                ", tipoGas='" + tipoGas + '\'' +
                ", advertencias=" + advertencias +
                '}';
    }
}
