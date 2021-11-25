/**
 * AdvertenciaMedicion.java
 * @fecha: 23/11/2021
 * @autor: Aitor Benítez Estruch
 *
 * @Descripcion:
 * POJO para almacenar las advertencias generadas en el backend por sobrepasar el límite diario
 * de exposición a cierto gas al analizar un periodo de tiempo determinado. En esta
 * advertencia aparece la fecha de inicio de la detección a la sobreexposición, el tiempo final,
 * el tiempo transcurrido, la media del periodo y la máxima exposición durante este periodo.
 *
 */


package com.example.tricoenvironment.airlity;

public class AdvertenciaMedicion {

    private long fechaIni, fechaFin, periodoTiempoTranscurrido;
    private double mediaPeriodo, valorMaximoPeriodo;

    /**
     * Constructor
     */
    public AdvertenciaMedicion(){}

    /**
     * Getters y setters
     */
    public long getPeriodoTiempoTranscurrido() {
        return periodoTiempoTranscurrido;
    }

    public void setPeriodoTiempoTranscurrido(long periodoTiempoTranscurrido) {
        this.periodoTiempoTranscurrido = periodoTiempoTranscurrido;
    }

    public long getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(long fechaIni) {
        this.fechaIni = fechaIni;
    }

    public long getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(long fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getMediaPeriodo() {
        return mediaPeriodo;
    }

    public void setMediaPeriodo(double valorMedioMedicion) {
        this.mediaPeriodo = valorMedioMedicion;
    }

    public double getValorMaximoPeriodo() {
        return valorMaximoPeriodo;
    }

    public void setValorMaximoPeriodo(double valorMaximoPeriodo) {
        this.valorMaximoPeriodo = valorMaximoPeriodo;
    }

    /**
     *Método toString()
     */
    @Override
    public String toString() {
        return "{" +
                "fechaIni=" + fechaIni +
                ", fechaFin=" + fechaFin +
                ", periodoTiempoTranscurrido=" + periodoTiempoTranscurrido +
                ", mediaPeriodo=" + mediaPeriodo +
                ", valorMaximoPeriodo=" + valorMaximoPeriodo +
                '}';
    }
}
