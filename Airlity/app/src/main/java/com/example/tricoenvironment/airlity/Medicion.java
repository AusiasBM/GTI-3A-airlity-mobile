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

    private enum TipoMedida{IAQ, CO, NO2, O3};
    private TipoMedida tipoMedicion;
    private String macSensor;
    private int medida, temperatura, humedad; //de moment serà un nº enter
    private double latitud, longitud;
    private long fecha;

    /**
     * Constructor por defecto.
     */
    public Medicion() {
    }

    //Este constructor no lo necesito ahora
    /*public Medicion( String nombreSensor, String macSensor, String uuidSensor, int medida, double latitud, double longitud, long fecha) {
        this.tipo = TipoMedida.CONCENTRACION_GAS;
        this.nombreSensor = nombreSensor;
        this.macSensor = macSensor;
        this.uuidSensor = uuidSensor;
        this.medida = medida;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
    }*/


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

    /**
     * Método getTipo.
     *
     *  Texto <- getTipo() <-
     *
     * @return tipo.name(). Devuelve el nombre del tipo de medida de la que es cada medición.
     */
    public String getTipoMedida() {
        try{
            return tipoMedicion.name();
        }catch (Exception e){
            return tipoMedicion.IAQ.name();
        }

    }


    /**
     * Método setTipo. Determina el tipo de medición que es cada objeto según un el texto que se
     * le pasa como parámetro. Se utilizará para saber el tipo cuando se convierta un JSON a objeto
     * de tipo Medicion
     *
     *  tipoM : Texto -> setTipo() ->
     *
     * @param tipoM Texto que identifica el tipo de medida.
     */
    public void setTipoMedicion(String tipoM) {
        switch (tipoM) {
            case "IAQ":
                this.tipoMedicion = tipoMedicion.IAQ;
                break;
            case "CO":
                this.tipoMedicion = tipoMedicion.CO;
                break;
            case "NO2":
                this.tipoMedicion = tipoMedicion.NO2;
                break;
            case "O3":
                this.tipoMedicion = tipoMedicion.O3;
                break;

            default:
                // Como se ha cambiado el tipo CO2 por GAS, poner default GAS
                this.tipoMedicion = tipoMedicion.IAQ;
                break;
        }
    }

    /**
     * Método setTipo. Determina el tipo de medición que es cada objeto según un identificador (entero)
     *
     *  identificador : N -> setTipo() ->
     *
     * @param identificador Número entero
     *
     */
    public void setTipoMedicion(int identificador) {
        switch (identificador) {
            case 0:
                this.tipoMedicion = tipoMedicion.IAQ;
                break;
            case 1:
                this.tipoMedicion = tipoMedicion.CO;
                break;
            case 2:
                this.tipoMedicion = tipoMedicion.NO2;
                break;
            case 3:
                this.tipoMedicion = tipoMedicion.O3;
                break;
            default:
                // Como se ha cambiado el tipo CO2 por GAS, poner default GAS
                this.tipoMedicion = tipoMedicion.IAQ;
                break;
        }
    }



    /**
     * Método getMedida.
     *
     *  Z <- getMedida() <-
     *
     * @return medida Devuelve el valor de la medida.
     */
    public int getMedida() {
        return medida;
    }


    /**
     * Método setMedida.
     *
     * Z -> setMedida() ->
     *
     * @param  medida Valor de la medida que ha captado el sensor.
     */
    public void setMedida(int medida) {
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
                ", medida=" + medida +
                ", temperatura=" + temperatura +
                ", humedad=" + humedad +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", fecha=" + fecha +
                '}';
    }
}
