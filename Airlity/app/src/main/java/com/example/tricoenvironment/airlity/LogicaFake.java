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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LogicaFake {

    private static final String ETIQUETA_LOG = ">>>>";
    private static String url="172.20.10.2";
    //217.76.155.97


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
            elPeticionario.hacerPeticionREST("POST",  "http://"+url+":3500/mediciones",
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
        elPeticionario.hacerPeticionREST("GET",  "http://"+url+":3500/ultimasMediciones/10", null,
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
    public static void registrarUsuario(String nombre, String correo, String contraseña, int numero, String macSensor, final Context context) {
            PeticionarioREST elPeticionario = new PeticionarioREST();

            final Usuario usuario = new Usuario(nombre, correo, contraseña, numero);
            elPeticionario.hacerPeticionREST("POST",  "http://"+url+":3500/registrarUsuario",
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

    /**
     * registrarUsuario() ejecuta una petición POST al servidor enviando los datos de un Usuario
     *
     * registrarUsuario() ->
     */
    public static void iniciarSesion(String correo, String contraseña, final Context context) {
        PeticionarioREST elPeticionario = new PeticionarioREST();

        final JSONObject obj = new JSONObject();
        try {
            obj.put("correo", correo+"");
            obj.put("contrasenya", contraseña+"");
        } catch (JSONException e)
        { // TODO Auto-generated catch block e.printStackTrace();
        }
        elPeticionario.hacerPeticionREST("POST",  "http://"+url+":3500/login",
                obj.toString(),
                new PeticionarioREST.RespuestaREST () {
                    @Override
                    public void callback(int codigo, String cuerpo) {
                        Log.d(ETIQUETA_LOG, "codigo respuesta login= " + codigo + " <-> \n" + cuerpo);
                        Intent i = new Intent();
                        i.setAction("Get_usuario_login");
                        i.putExtra("codigo_usuario_login", codigo);
                        i.putExtra("cuerpo_usuario", cuerpo);
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


}
