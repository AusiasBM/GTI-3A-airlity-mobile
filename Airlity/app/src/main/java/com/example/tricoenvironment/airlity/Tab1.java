/**
 * Tab1.java
 * @fecha: 08/10/2021
 * @autor: Aitor Benítez Estruch
 *
 * @Descripcion:
 * Este fichero ejecuta la clase Tab1 que ofrece la vista, los campos de relleno y las
 * funcionalidades de los botones
 */

package com.example.tricoenvironment.airlity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tab1 extends Fragment {

    private EditText nombreDispositivo;
    private EditText macDispositivo;

    private TextView textNombreDispositivo;
    private TextView textNMacDispositivo;
    private TextView textUuidDispositivo;
    private TextView textFechaDispositivo;

    private Button buscarDispositivo;
    private Button detenerDispositivo;

    private ProgressBar progressBar;

    private Context context;
    private Intent intentServicioBLE = null;
    private  Intent intentServicioREST = null;

    private IntentFilter intentFilter;
    private Receptor receptor;

    private IntentFilter intentDistanciaSernsor;
    private ReceptorDistancia receptorDistanciaSensor;

    private IntentFilter intentFilterMediciones;
    private ReceptorMediciones receptorMediciones;

    private static final String ETIQUETA_LOG = ">>>>";

    private String nombreNotificacion;
    boolean esActivo = false;
    boolean sensorNoEncontrado = false;
    double distancia;
    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 2;

    ServicioEscuharBeacons servicio= new ServicioEscuharBeacons();
    Button btn_distancia;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = getActivity();

        intentFilter = new IntentFilter();
        intentFilter.addAction("DeteccionSensor");
        receptor = new Receptor();

        intentDistanciaSernsor = new IntentFilter();
        intentDistanciaSernsor.addAction("Nueva_distancia");
        receptorDistanciaSensor = new ReceptorDistancia();

        intentFilterMediciones = new IntentFilter();
        intentFilterMediciones.addAction("Nueva_Medicion");
        receptorMediciones = new ReceptorMediciones();
    }


    /**
     * onCreateView() carga la vista y los elementos que hay en tab1.xml
     *
     * onCreateView() ->
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab1, container, false);

        progressBar = v.findViewById(R.id.progressBar2);
        btn_distancia = v.findViewById(R.id.boton_distancia2);
        intentServicioBLE = new Intent(context, ServicioEscuharBeacons.class);

        //Creamos el intent que permitirá lanzar el servicio ServicioLogicaFake
        intentServicioREST = new Intent(context, ServicioLogicaFake.class);
        context.startService(intentServicioREST);

        //botonBuscarNuestroDispositivoBTLEPulsado();
        //botonDetenerBusquedaDispositivosBTLEPulsado();
        btn_averiguarDistancia();


        return v;
    }

    /**
     * onResume() se ejecuta cuando este Fragment está en primer plano
     *
     * onResume() ->
     */
    @Override
    public void onResume() {
        super.onResume();
        context.registerReceiver(receptor, intentFilter);
        context.registerReceiver(receptorMediciones,intentFilterMediciones);
        context.registerReceiver(receptorDistanciaSensor,intentDistanciaSernsor);
    }

    /**
     * medicion->detectarLecturasErroneas()->
     * funcion que envia una notificacion si la medicion que recibe supera valores atipicos
     * Es decir si esta fuera del rango
     */

            public void detectarLecturasErroneas(Medicion medicion){

                    if(medicion.getHumedad()<15 || medicion.getHumedad()>16) {
                       nombreNotificacion="notificacionHumedadErronea";
                        //crear notificacion
                       crearNotificaciónSegundoPlano();
                    }

                    if(medicion.getMedida()<0 || medicion.getMedida()>40) {
                        nombreNotificacion="notificacionGasErronea";
                        //crear notificacion
                        crearNotificaciónSegundoPlano();
                    }

                    if(medicion.getTemperatura()<-20 || medicion.getTemperatura()>4) {
                        nombreNotificacion="notificacionTemperaturaErronea";
                        //crear notificacion
                        crearNotificaciónSegundoPlano();
                    }


            }
    /**
     * funcion que detecta la medicion de GAS y si execede de un valor de 150 manda una notificacion
     * medicion->detectarLimiteGas()->
     */
            public void detectarLimiteGas(Medicion medicion){
                //si la medicion es de GAS

                if(medicion.getMedida()>15000){
                    //para elegir el tipo de notificacion
                    nombreNotificacion="notificacionLimiteGas";
                    //crear una notificacion
                    crearNotificaciónSegundoPlano();
                }

            }
    /**
     * Contador timer
     * el objeto crea un contador de 30 seg donde empieza una cuenta atras
     * objeto ctd
     */

    CountDownTimer ctd = new CountDownTimer(30000, 1000) {

        public void onTick(long millisUntilFinished) {
            Log.d("Contador","seconds remaining: " + millisUntilFinished / 1000);
            esActivo = true;
        }

        public void onFinish() {
            Log.d("Contador finalizado","done!");
            esActivo = false;
            sensorNoEncontrado = true;
            nombreNotificacion="noReciveIbeacons";
            crearNotificaciónSegundoPlano();
        }
    };
    /**
     * botonBuscarNuestroDispositivoBTLEPulsado() se encarga de lanzar el servicio
     * ServicioEscucharBeacons pasando con un Intent el contenido de los EditText nombreDispositivo
     * y macDispositivo
     *
     * botonBuscarNuestroDispositivoBTLEPulsado() ->
     */

    /*
    public void botonBuscarNuestroDispositivoBTLEPulsado() {

        //this.buscarEsteDispositivoBTLE( Utilidades.stringToUUID( "EPSG-GTI-PROY-3A" ) ); GTI-3A-ABENEST C5:BC:C9:2D:5C:D0
        buscarDispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!esActivo && !sensorNoEncontrado) {
                    ctd.start();
                }
                Log.d(ETIQUETA_LOG, " boton nuestro dispositivo BTLE Pulsado" );
                if(esActivo){
                    ctd.cancel();
                    ctd.start();
                }
                if(sensorNoEncontrado==true){
                    ctd.start();
                }

                //Asegurarnos que almenos tenga un campo para filtrar nuestro dispositivo:
                if(nombreDispositivo.getText().toString().isEmpty()  && macDispositivo.getText().toString().isEmpty() ){
                    Toast.makeText(context, "Rellene almenos uno de los dos campos" , Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(ETIQUETA_LOG, " Inicio del servicio" );
                //ServicioEscucharBeacons
                intentServicioBLE.putExtra("nombreDispositivo", nombreDispositivo.getText().toString());
                intentServicioBLE.putExtra("macDispositivo", macDispositivo.getText().toString());
                context.startService(intentServicioBLE);

            }
        });

        //this.buscarEsteDispositivoBTLE( "GTI-3A-ABENEST" , "C5:BC:C9:2D:5C:D0" );
    } // ()

     */

    public void btn_averiguarDistancia(){
        btn_distancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mostarDistancia(distancia);
            }
        });
    }
    /**
     * botonDetenerBusquedaDispositivosBTLEPulsado() se encarga de parar el servicio
     * ServicioEscucharBeacons dejando de recibir beacons
     *
     * botonBuscarNuestroDispositivoBTLEPulsado() ->
     */
    /*
    public void botonDetenerBusquedaDispositivosBTLEPulsado() {
        detenerDispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(ETIQUETA_LOG, " boton detener busqueda dispositivos BTLE Pulsado" );

                try {
                    //Parar servicio escucha BLE
                    context.stopService(intentServicioBLE);
                    ctd.cancel();
                    Intent stopIntent = new Intent();
                    stopIntent.setAction(ServicioLogicaFake.StopServicioREST.ACTION_STOP);
                    context.sendBroadcast(stopIntent);


                }catch (Exception e){
                    Log.d(ETIQUETA_LOG, "" + e );
                }

            }
        });
    } // ()

     */



    /**
     * Clase Receptor
     * Receptor de mensajes broadcast de tipo "Nueva_Medicion"
     */
    private class Receptor extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getStringExtra("Sensor");
            Gson gson = new Gson();
            Sensor sensor = gson.fromJson(s, Sensor.class);

            Log.d("INTENT", "" + sensor);

            textNombreDispositivo.setText(sensor.getNombre());
            textNMacDispositivo.setText(sensor.getMac());
            textUuidDispositivo.setText(sensor.getUuid());

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm");
            Date resultado = new Date(sensor.getFecha());
            textFechaDispositivo.setText(resultado.toString());
            ctd.cancel();
            ctd.start();

        }
    }


    /**
     * Clase Receptor
     * Receptor de mensajes broadcast de tipo "Nueva_Medicion"
     */
    private class ReceptorMediciones extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String m = intent.getStringExtra("Medicion");
            Gson gson = new Gson();
            Medicion medicion = gson.fromJson(m, Medicion.class);

            Log.d("INTENT", "" + medicion);
            detectarLecturasErroneas(medicion);
            detectarLimiteGas(medicion);

        }
    }
    /*
    * Receptor distancia se emplea pra recoger el parametro distancia de la clase ServicioEscucharBeacons
    * */

    private class ReceptorDistancia extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
             distancia = intent.getDoubleExtra("Distancia",0.0);

            Log.d("INTENT", "" + distancia);

        }
    }
    /*
    double->mostrarDistancia()->
    este meetodo se utiliza para que caundo cliques un boton mire si la distancia
    es superior de 1500 que es valor normal a menos de 5 metros
        */
    public void mostarDistancia( double resultado ) {
        Log.d("distancia", "" + resultado);
        resultado=1;
        if (resultado < 0.1) {
            Toast.makeText(context, "Error al rastrear el sensor, vuelva a intentarlo",
                    Toast.LENGTH_SHORT).show();
            progressBar.setProgress(0);
        }else if(resultado>=0.1 && resultado<500){
            progressBar.setProgress(1);
        } else if (resultado >= 500 && resultado < 1200) {
            progressBar.setProgress(6);
        }else if(resultado>=1200 && resultado<2500){
            progressBar.setProgress(7);
        }
        else if(resultado>=2500){
            progressBar.setProgress(9);
        }
        else {
            Toast.makeText(context, "No se ha podido encontrar el dispositivo",
                    Toast.LENGTH_SHORT).show();
            progressBar.setProgress(0);
        }

    }


    private void crearNotificaciónSegundoPlano(){
        //Crear la notificación
        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        CANAL_ID, "Mis Notificaciones",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("Descripcion del canal");
                notificationChannel.setVibrationPattern(new long[]{0, 100, 300, 100});
                notificationChannel.enableVibration(true);

                notificationManager.createNotificationChannel(notificationChannel);
            }

            if(nombreNotificacion=="noReciveIbeacons"){

                NotificationCompat.Builder notificacion =
                        new NotificationCompat.Builder(getContext(), CANAL_ID)
                                .setWhen(System.currentTimeMillis() + 1000 * 60 * 60)
                                .setSmallIcon(R.mipmap.logo)
                                .setContentTitle("Datos no encontrados")
                                .setContentText("Se ha perdido la conexión con el sensor");

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificacion.setSound(alarmSound);
                notificationManager.notify(NOTIFICACION_ID, notificacion.build());
            }
            if(nombreNotificacion=="notificacionHumedadErronea"){
                NotificationCompat.Builder notificacion =

                        new NotificationCompat.Builder(getContext(), CANAL_ID)
                                .setSmallIcon(R.mipmap.logo)
                                .setContentTitle("Medidas no validas")
                                .setContentText("Ha habido un problema con la lectura de mediciones");


                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificacion.setSound(alarmSound);
                notificationManager.notify(NOTIFICACION_ID, notificacion.build());
            }
            if(nombreNotificacion=="notificacionGasErronea"){
                NotificationCompat.Builder notificacion =
                        new NotificationCompat.Builder(getContext(), CANAL_ID)
                                .setSmallIcon(R.mipmap.logo)
                                .setContentTitle("Medidas no validas")
                                .setContentText("Ha habido un problema con la lectura de mediciones");
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificacion.setSound(alarmSound);
                notificationManager.notify(NOTIFICACION_ID, notificacion.build());
            }
            if(nombreNotificacion=="notificacionTemperaturaErronea"){
                NotificationCompat.Builder notificacion =
                        new NotificationCompat.Builder(getContext(), CANAL_ID)
                                .setSmallIcon(R.mipmap.logo)
                                .setContentTitle("Medidas no validas")
                                .setContentText("Ha habido un problema con la lectura de mediciones");
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificacion.setSound(alarmSound);
                notificationManager.notify(NOTIFICACION_ID, notificacion.build());
            }
            if(nombreNotificacion=="notificacionLimiteGas") {
                NotificationCompat.Builder notificacion =
                        new NotificationCompat.Builder(getContext(), CANAL_ID)
                                .setSmallIcon(R.mipmap.icon_prec)
                                .setContentTitle("Zona no recomendada")
                                .setContentText("Calidad del aire no recomendada para respirar en periodos largos");
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificacion.setSound(alarmSound);
                notificationManager.notify(NOTIFICACION_ID, notificacion.build());
            }

        }catch (Exception e){

        }




        //Llançar l'aplicació des de la notificació
        /*PendingIntent intencionPendiente = PendingIntent.getActivity(
                this, 0, new Intent(this, Tab1.class), 0);
        notificacion.setContentIntent(intencionPendiente);*/

        //Servici en primer pla (DECLARAR EN EL MANIFEST)
        //startForeground(NOTIFICACION_ID, notificacion.build());

        //Servici en segon pla

    }
}
