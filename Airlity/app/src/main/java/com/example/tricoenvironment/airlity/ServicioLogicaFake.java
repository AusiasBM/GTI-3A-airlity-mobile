/**
 * ServicioLogicaFake.java
 * @fecha: 07/10/2021
 * @autor: Aitor Benítez Estruch
 *
 * @Descripcion:
 * Este fichero contiene la clase ServicioLogicaFake, para ejecutar un servicio en un nuevo hilo
 * que permite establecer una comunicación REST con el servidor, y enviar y recibir información.
 *
 */

package com.example.tricoenvironment.airlity;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

/**
 * Clase ServicioLogicaFake
 * Clase que hereda de IntentService que permite ejecutar el servicio en un nuevo hilo
 */
public class ServicioLogicaFake extends IntentService {
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    private static final String ETIQUETA_LOG = ">>>>";

    private int tiempoDeEsperaHilo = 30000;

    private int tiempoDeEsperaEnvioPost = 60000;
    private long tiempo;

    private boolean seguir = true;

    private List<String> medicionesString = new ArrayList<>();

    IntentFilter filterRecepcionMedicion, filterIniciarGetMediciones;

    Context context;

    ReceptorMediciones receptorMedicion;
    InicializadorGetMediciones inicializadorGetMediciones;

    /**
     * Constructor de la clase ServicioLogicaFake.
     *
     * Constructor() ->
     */
    public ServicioLogicaFake(  ) {
        super("ServicioLogicaFake");

    }


    /**
     * onCreate() se ejecuta antes de iniciar el servicio
     *
     * onCreate() ->
     */
    @Override
    public void onCreate() {
        super.onCreate();

        //Registramos el recibidor de mensaje broadcast "Nueva_Medicion"
        filterRecepcionMedicion  = new IntentFilter();
        filterRecepcionMedicion.addAction("Nueva_Medicion");
        receptorMedicion = new ReceptorMediciones();
        registerReceiver(receptorMedicion, filterRecepcionMedicion);

        //Registramos el recibidor de mensaje broadcast "Iniciar_GET_Mediciones"
        filterIniciarGetMediciones = new IntentFilter();
        filterIniciarGetMediciones.addAction("Iniciar_GET_Mediciones");
        inicializadorGetMediciones = new InicializadorGetMediciones();
        registerReceiver(inicializadorGetMediciones, filterIniciarGetMediciones);

        context = this;

        tiempo = currentTimeMillis();
    }



    /**
     * parar() finaliza la escucha de los receptores de mensajes broadcast y para el intentService
     *
     * parar() ->
     */
    public void parar () {
        Log.d(ETIQUETA_LOG, " ServicioEscucharBeacons.parar() " );
        if ( this.seguir == false ) {
            return;
        }
        this.seguir = false;

        unregisterReceiver(receptorMedicion);
        unregisterReceiver(inicializadorGetMediciones);
        this.stopSelf();
        Log.d(ETIQUETA_LOG, " ServicioEscucharBeacons.parar() : acaba " );

    }

    /**
     * onDestroy() se ejecuta al finalizar el servicio
     *
     * onDestroy() ->
     */
    public void onDestroy() {
        Log.d(ETIQUETA_LOG, " ServicioEscucharBeacons.onDestroy() " );
        this.parar(); // posiblemente no haga falta, si stopService() ya se carga el servicio y su worker thread
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        this.seguir = true;

        // esto lo ejecuta un WORKER THREAD !

        Log.d(ETIQUETA_LOG, " ServicioEscucharBeacons.onHandleIntent: empieza : thread=" + Thread.currentThread().getId() );

        try {

            while ( this.seguir ) {
                Log.d(ETIQUETA_LOG, "LONGITUD: " + medicionesString.size());

                //Enviar mediciones cada 60s al servidor
                if(currentTimeMillis() > tiempo + tiempoDeEsperaEnvioPost){
                    LogicaFake.guardarMediciones(medicionesString);
                    tiempo = currentTimeMillis();
                }

                LogicaFake.obtenerUltimasMediciones(context);

                Thread.sleep(tiempoDeEsperaHilo);
            }

            Log.d(ETIQUETA_LOG, " ServicioEscucharBeacons.onHandleIntent : tarea terminada ( tras while(true) )" );


        } catch (InterruptedException e) {
            // Restore interrupt status.
            Log.d(ETIQUETA_LOG, " ServicioEscucharBeacons.onHandleItent: problema con el thread");

            Thread.currentThread().interrupt();
        }

        Log.d(ETIQUETA_LOG, " ServicioEscucharBeacons.onHandleItent: termina");

        //Si llega aquí es que pasa antes por onDestroy, ponemos un try-catch por si acaso
        try{
            unregisterReceiver(receptorMedicion);
            unregisterReceiver(inicializadorGetMediciones);
            this.stopSelf();
        }catch(Exception e){}

    }


    /**
     * Clase ReceptorMediciones
     * Receptor de mensajes broadcast de tipo "Nueva_Medicion"
     */
    private class ReceptorMediciones extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String m = intent.getStringExtra("Medicion");
            Log.d(ETIQUETA_LOG, m);
            Gson g = new Gson();
            Medicion medicion = g.fromJson(m, Medicion.class);
            String str =  g.toJson(medicion);
            Log.d(ETIQUETA_LOG, "" + str);
            medicionesString.add(str);


            Log.d(ETIQUETA_LOG, "" + medicionesString.size());

        }
    }

    /**
     * Clase InicializadorGetMediciones
     * Receptor de mensajes broadcast de tipo "Iniciar_GET_Mediciones"
     * Se utilizará para forzar cargar las últimas mediciones desde Tab2 al recyclerView
     * cuando entramos en esa pestaña por primera vez (y no tener que esperar a que en onHandleIntent
     * actualize los valores)
     */
    private class InicializadorGetMediciones extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(ETIQUETA_LOG, "INICIALIZADOR OBTENCIÓN DE MEDIDASSSSSSSSSSSSSSSSSSSSS");

            LogicaFake.obtenerUltimasMediciones(context);

        }
    }
}
