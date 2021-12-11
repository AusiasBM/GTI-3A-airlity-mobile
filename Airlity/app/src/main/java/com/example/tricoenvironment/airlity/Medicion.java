/**
 * Medicion.java
 * @fecha: 07/10/2021
 * @autor: Aitor Benítez Estruch
 *
 * @Descripcion:
 * Este fichero contiene el POJO Medicion.
 */

package com.example.tricoenvironment.airlity;

import static java.lang.System.currentTimeMillis;

public class Medicion {

    private enum TipoMedida{IAQ, SO2, NO2, O3};
    private String tipoMedicion;
    private String macSensor, idUsuario;
    private int temperatura, humedad; //de moment serà un nº enter
    private double medida, latitud, longitud;
    private long fecha;

    /**
     * Constructor por defecto.
     */
    public Medicion() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(int temperatura) {
        this.temperatura = temperatura;
    }

    public int getHumedad() {
        return humedad;
    }

    public void setHumedad(int humedad) {
        this.humedad = humedad;
    }

    /**
     * Método getFecha.
     *
     * N <- getFecha() <-
     *
     * @return fecha. Devuelve la fecha en milisegundos
     */
    public long getFecha() {
        /*SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultado = new Date(this.fecha);
        return resultado.toString();*/
        return this.fecha;
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

    public String getTipoMedicion() {
        return tipoMedicion;
    }

    public void setTipoMedicion(String tipoMedicion) {
        this.tipoMedicion = tipoMedicion;
    }

    public double getMedida() {
        return medida;
    }

    public void setMedida(double medida) {
        this.medida = medida;
    }

    /**
     * Método getMacSensor.
     *
     *  Texto <- getMacSensor() <-
     *
     * @return macSensor Devuelve un texto con la MAC del sensor que ha captado la medida.
     */
    public String getMacSensor() {
        return macSensor;
    }


    /**
     * Método setMacSensor.
     *
     * Texto -> setMacSensor() ->
     *
     * @param  macSensor Texto de la MAC del sensor que ha captado la medida.
     */
    public void setMacSensor(String macSensor) {
        this.macSensor = macSensor;
    }


    /**
     * Método getLatitud.
     *
     *  R <- getLatitud() <-
     *
     * @return latitud Devuelve un número real con la latitud donde se ha obtenido la medida.
     */
    public double getLatitud() {
        return latitud;
    }


    /**
     * Método setLatitud.
     *
     *  R -> setLatitud() ->
     *
     * @param latitud Número real con la latitud donde se ha obtenido la medida.
     */
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }


    /**
     * Método getLongitud.
     *
     *  R <- getLongitud() <-
     *
     * @return longitud Devuelve un número real con la longitud donde se ha obtenido la medida.
     */
    public double getLongitud() {
        return longitud;
    }


    /**
     * Método setLongitud.
     *
     *  R -> setLongitud() ->
     *
     * @param longitud Número real con la longitud donde se ha obtenido la medida.
     */
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    /**
     * Método toString. Coge toda la información almacenada dentro del objeto y lo convierte en una
     * cadena de texto con formato JSON.
     *
     *  Texto <- toString() <-
     *
     * @return Texto Devuelve una cadena de texto con formato JSON.
     */
    @Override
    public String toString() {
        return "{" +
                "tipoMedicion=" + tipoMedicion +
                ", macSensor='" + macSensor + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                ", medida=" + medida +
                ", temperatura=" + temperatura +
                ", humedad=" + humedad +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", fecha=" + fecha +
                '}';
    }
}
