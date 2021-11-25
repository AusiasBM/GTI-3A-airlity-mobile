/**
 * LogicaFake.java
 * @fecha: 08/10/2021
 * @autor: Aitor Benítez Estruch
 *
 * @Descripcion:
 * Este fichero se encarga de la lógica fake de la aplicación, mediciones, sensor y usuarios
 */

package com.example.tricoenvironment.airlity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.List;

public class LogicaFake {

    private static final String ETIQUETA_LOG = ">>>>";

    private static final String direccionIP = "192.168.0.107";


    /**
     * guardarMediciones() ejecuta una petición POST al servidor enviando un array de objetos Medicion
     *
     * guardarMediciones() ->
     */
    public static void guardarMediciones(List<String> medicionesString) {

        if(medicionesString.size() > 0){
            PeticionarioREST elPeticionario = new PeticionarioREST();

            JSONArray jsArray = new JSONArray(medicionesString);
            Log.d(ETIQUETA_LOG, "" + jsArray);
            medicionesString.clear();
            //192.168.0.107
            //10.236.29.250
            elPeticionario.hacerPeticionREST("POST",  "http://"+direccionIP+":3500/mediciones",
                    String.valueOf(jsArray),
                    new PeticionarioREST.RespuestaREST () {
                        @Override
                        public void callback(int codigo, String cuerpo) {
                            Log.d(ETIQUETA_LOG, "codigo respuesta= " + codigo + " <-> \n" + cuerpo);
                        }
                    }
            );
        }

    }

    /**
     * obtenerUltimasMediciones() ejecuta una petición GET al servidor para recuperar las últimas
     * 10 mediciones guardadas en la bd. El resultado lo envia con un intent de forma broadcast
     * para que lo recupere en el Fragment Tab2 donde se listará el resultado
     *
     * obtenerUltimasMediciones() <-
     */
    public static void obtenerUltimasMediciones(final Context context){
        PeticionarioREST elPeticionario = new PeticionarioREST();
        //Direccion ip en UPVNET10.236.29.250
        //Direccion ip en casa 192.168.0.107
        elPeticionario.hacerPeticionREST("GET",  "http://"+direccionIP+":3500/ultimasMediciones/10", null,
                new PeticionarioREST.RespuestaREST () {
                    @Override
                    public void callback(int codigo, String cuerpo) {
                        Log.d(ETIQUETA_LOG, "codigo respuesta= " + codigo + " <-> \n" + cuerpo);

                        Intent i = new Intent();
                        i.setAction("Get_Mediciones");
                        i.putExtra("Mediciones", cuerpo);
                        context.sendBroadcast(i);

                    }
                }
        );
    }

    /**
     * registrarUsuario() ejecuta una petición POST al servidor enviando los datos de un Usuario
     *
     * registrarUsuario() ->
     */
    public static void registrarUsuario(String nombre, String correo, String contraseña, int numero, final Context context) {
            PeticionarioREST elPeticionario = new PeticionarioREST();

            final Usuario usuario = new Usuario(nombre, correo, contraseña, numero);
            elPeticionario.hacerPeticionREST("POST",  "http://"+direccionIP+":3500/registrarUsuario",
                    usuario.toString(),
                    new PeticionarioREST.RespuestaREST () {
                        @Override
                        public void callback(int codigo, String cuerpo) {
                            Log.d(ETIQUETA_LOG, "codigo respuesta= " + usuario.toString());
                            Log.d(ETIQUETA_LOG, "codigo respuesta= " + codigo + " <-> \n" + cuerpo);

                            Intent i = new Intent();
                            i.setAction("Get_usuario");
                            i.putExtra("codigo_usuario", codigo);
                            context.sendBroadcast(i);
                        }
                    }
            );
    }

    public static boolean comprobarSiEsteUsuarioEstaRegistrado(String correo){
        boolean existe=true;
        //Comprobar si existe el correo, consulta a la ruta y que devuelva t o f
        return existe;
    }

    public static boolean comprobarSiLaContraseñaEsCorrecta(String correo, String contraseña){
        boolean esCorrecto=true;
        //Comprobar si la contraseña es correcta
        return esCorrecto;
    }

    public boolean iniciarSesion(String correo, String contraseña) {
        if (comprobarSiEsteUsuarioEstaRegistrado(correo)) {
            if (comprobarSiLaContraseñaEsCorrecta(correo, contraseña)) {
                return true;
            }
        }else{
            return false;
        }
        return false;
    }


    /**
     * obtenerEstadisticas()
     * Descripción:
     * Método de la lógica fake para obtener datos estadísticos de las mediciones durante un periodo de tiempo determinado
     *
     * @param context Objeto de tipo Context de la Activity
     * @param fechaIni Long con la fecha inicio del periodo que se quiere mostrar
     * @param fechaFin Long con la fecha final del periodo que se quiere mostrar
     *
     * @return estadsticas Objeto de tipo EstadisticasMediciones
     *
     *
     */
    public static void obtenerEstadisticas(final Context context, long fechaIni, long fechaFin){
        PeticionarioREST elPeticionario = new PeticionarioREST();
        //Direccion ip en UPVNET10.236.29.250
        //Direccion ip en casa 192.168.0.107
        elPeticionario.hacerPeticionREST("GET",
                "http://"+direccionIP+":3500/estadisticasMedicionesUsuario?fechaIni="+ fechaIni + "&fechaFin="+ fechaFin, null,
                new PeticionarioREST.RespuestaREST () {
                    @Override
                    public void callback(int codigo, String cuerpo) {
                        /*Log.d("PROVA", "codigo respuesta= " + codigo + " <-> \n" + cuerpo);
                        Log.d("CODIGO", "" + codigo);
                        Log.d("CUERPO", "" + cuerpo);*/
                        Gson gson = new Gson();
                        EstadisticasMediciones estadisticas = gson.fromJson(cuerpo, EstadisticasMediciones.class);

                        Log.d("RESULTADO DE MEDICIONES", "VALOR MÁXIMO = "  + estadisticas.getValorMaximo());
                        Log.d("RESULTADO DE MEDICIONES", "MEDIA PONDERADA = "  + estadisticas.getMedia());
                        Log.d("RESULTADO DE MEDICIONES", "TIEMPO MIDIENDO = "  + estadisticas.getTiempo());
                        Log.d("RESULTADO DE MEDICIONES", "VALORACION = "  + estadisticas.getValoracionCalidadAire());
                        Log.d("RESULTADO DE MEDICIONES", "GAS = "  + estadisticas.getTipoGas());
                        Log.d("RESULTADO DE MEDICIONES", "ADVERTENCIAS = "  + estadisticas.getAdvertencias().size());
                        Log.d("RESULTADO DE MEDICIONES", "ADVERTENCIAS = "  + estadisticas.getAdvertencias());

                        Intent i = new Intent();
                        i.setAction("DatosEstadisticos");
                        i.putExtra("Estadisticas", estadisticas.toString());
                        context.sendBroadcast(i);
                    }
                }
        );
    }


    /**
     * obtenerDatosParaGrafico()
     * Descripción:
     * Método de la lógica fake para obtener datos para generar un gráfico con el resumen de las mediciones
     * durante un periodo de tiempo determinado.
     *
     * @param context Objeto de tipo Context de la Activity
     * @param fechaIni Long con la fecha inicio del periodo que se quiere mostrar
     * @param fechaFin Long con la fecha final del periodo que se quiere mostrar
     *
     * @return datos  Objeto de tipo DatosGrafica
     *
     *
     */
    public static void obtenerDatosParaGrafico(final Context context, long fechaIni, long fechaFin){
        PeticionarioREST elPeticionario = new PeticionarioREST();
        //Direccion ip en UPVNET10.236.29.250
        //Direccion ip en casa 192.168.0.107
        elPeticionario.hacerPeticionREST("GET",
                "http://"+direccionIP+":3500/datosGraficaUsuario?fechaIni="+ fechaIni + "&fechaFin="+ fechaFin, null,
                new PeticionarioREST.RespuestaREST () {
                    @Override
                    public void callback(int codigo, String cuerpo) {
                        //Log.d("PROVA", "codigo respuesta= " + codigo + " <-> \n" + cuerpo);
                        //Log.d("CODIGO", "" + codigo);
                        //Log.d("CUERPO", "" + cuerpo);
                        Gson gson = new Gson();
                        DatosGrafica datos = gson.fromJson(cuerpo, DatosGrafica.class);

                        Intent i = new Intent();
                        i.setAction("DatosEstadisticos");
                        i.putExtra("DatosGrafica", datos.toString());
                        context.sendBroadcast(i);

                    }
                }
        );
    }


}
