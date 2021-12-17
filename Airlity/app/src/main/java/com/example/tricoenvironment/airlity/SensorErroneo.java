package com.example.tricoenvironment.airlity;

public class SensorErroneo {
    private Integer mediaGlobal;
    private String sensor;
    private Integer mediaSensor;
    private Integer desviacionRespectoMediaGlobal;
    private Integer limiteDesviacionSuperior;
    private Integer limiteDesviacionInferior;
    private Boolean valoresIrregularesNoValidos;
    private Integer mediaDesviada;

    public SensorErroneo() {
    }

    public Integer getMediaGlobal() {
        return mediaGlobal;
    }
    public void setMediaGlobal(Integer mediaGlobal) {
        this.mediaGlobal = mediaGlobal;
    }
    public String getSensor() {
        return sensor;
    }
    public void setSensor(String sensor) {
        this.sensor = sensor;
    }
    public Integer getMediaSensor() {
        return mediaSensor;
    }
    public void setMediaSensor(Integer mediaSensor) {
        this.mediaSensor = mediaSensor;
    }
    public Integer getDesviacionRespectoMediaGlobal() {
        return desviacionRespectoMediaGlobal;
    }
    public void setDesviacionRespectoMediaGlobal(Integer desviacionRespectoMediaGlobal) {
        this.desviacionRespectoMediaGlobal = desviacionRespectoMediaGlobal;
    }
    public Integer getLimiteDesviacionSuperior() {
        return limiteDesviacionSuperior;
    }
    public void setLimiteDesviacionSuperior(Integer limiteDesviacionSuperior) {
        this.limiteDesviacionSuperior = limiteDesviacionSuperior;
    }
    public Integer getLimiteDesviacionInferior() {
        return limiteDesviacionInferior;
    }
    public void setLimiteDesviacionInferior(Integer limiteDesviacionInferior) {
        this.limiteDesviacionInferior = limiteDesviacionInferior;
    }
    public Boolean getValoresIrregularesNoValidos() {
        return valoresIrregularesNoValidos;
    }
    public void setValoresIrregularesNoValidos(Boolean valoresIrregularesNoValidos) {
        this.valoresIrregularesNoValidos = valoresIrregularesNoValidos;
    }
    public Integer getMediaDesviada() {
        return mediaDesviada;
    }
    public void setMediaDesviada(Integer mediaDesviada) {
        this.mediaDesviada = mediaDesviada;
    }

    @Override
    public String toString() {
        return "SensorErroneo{" +
                "mediaGlobal=" + mediaGlobal +
                ", sensor='" + sensor + '\'' +
                ", mediaSensor=" + mediaSensor +
                ", desviacionRespectoMediaGlobal=" + desviacionRespectoMediaGlobal +
                ", limiteDesviacionSuperior=" + limiteDesviacionSuperior +
                ", limiteDesviacionInferior=" + limiteDesviacionInferior +
                ", valoresIrregularesNoValidos=" + valoresIrregularesNoValidos +
                ", mediaDesviada=" + mediaDesviada +
                '}';
    }
}
