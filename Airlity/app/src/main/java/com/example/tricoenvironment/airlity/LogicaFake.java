package com.example.tricoenvironment.airlity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;

import java.util.List;

public class LogicaFake {

    private static final String ETIQUETA_LOG = ">>>>";


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
            elPeticionario.hacerPeticionREST("POST",  "http://10.236.29.250:8080/mediciones",
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
        elPeticionario.hacerPeticionREST("GET",  "http://10.236.29.250:8080/ultimasMediciones/10", null,
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
}
