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
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    private Context context;
    private Intent intentServicioBLE = null;
    private  Intent intentServicioREST = null;

    private IntentFilter intentFilter;
    private Receptor receptor;

    private IntentFilter intentFilterMediciones;
    private ReceptorMediciones receptorMediciones;

    private static final String ETIQUETA_LOG = ">>>>";

    private String nombreNotificacion;
    boolean esActivo = false;
    boolean sensorNoEncontrado = false;

    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 2;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = getActivity();

        intentFilter = new IntentFilter();
        intentFilter.addAction("DeteccionSensor");
        receptor = new Receptor();

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

        //Textviews donde se mostrará la información del dispositivo una vez se ha conectado
        textNombreDispositivo = v.findViewById(R.id.textNombreDelDispositivo);
        textNMacDispositivo = v.findViewById(R.id.textMacDelDispositivo);
        textUuidDispositivo = v.findViewById(R.id.textUuidDelDispositivo);
        textFechaDispositivo = v.findViewById(R.id.textFechaUltimoBeacon);

        //EditText donde poner los filtros para buscar nuestro dispositivo
        nombreDispositivo = v.findViewById(R.id.editNombre);
        macDispositivo = v.findViewById(R.id.editMAC);

        //Botones
        buscarDispositivo = v.findViewById(R.id.BotonBuscar);
        detenerDispositivo = v.findViewById(R.id.BotonFinalizar);

        intentServicioBLE = new Intent(context, ServicioEscuharBeacons.class);

        //Creamos el intent que permitirá lanzar el servicio ServicioLogicaFake
        intentServicioREST = new Intent(context, ServicioLogicaFake.class);
        context.startService(intentServicioREST);

        botonBuscarNuestroDispositivoBTLEPulsado();
        botonDetenerBusquedaDispositivosBTLEPulsado();

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

    public void botonBuscarNuestroDispositivoBTLEPulsado() {

        //this.buscarEsteDispositivoBTLE( Utilidades.stringToUUID( "EPSG-GTI-PROY-3A" ) ); GTI-3A-ABENEST C5:BC:C9:2D:5C:D0
        buscarDispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!esActivo && !sensorNoEncontrado) {
                    ctd.start();
                }
                Log.d(ETIQUETA_LOG, " boton nuestro dispositivo BTLE Pulsado" );

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

    /**
     * botonDetenerBusquedaDispositivosBTLEPulsado() se encarga de parar el servicio
     * ServicioEscucharBeacons dejando de recibir beacons
     *
     * botonBuscarNuestroDispositivoBTLEPulsado() ->
     */
    public void botonDetenerBusquedaDispositivosBTLEPulsado() {
        detenerDispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(ETIQUETA_LOG, " boton detener busqueda dispositivos BTLE Pulsado" );

                try {
                    //Parar servicio escucha BLE
                    context.stopService(intentServicioBLE);
                    ctd.cancel();
                    /*//Parar servicio rest
                    Intent stopIntent = new Intent();
                    stopIntent.setAction(ServicioLogicaFake.StopServicioREST.ACTION_STOP);
                    context.sendBroadcast(stopIntent);*/

                    Toast.makeText(context,"Servicio bluetooth detenido",
                            Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Log.d(ETIQUETA_LOG, "" + e );
                }

            }
        });
    } // ()


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
                notificationManager.createNotificationChannel(notificationChannel);
            }
            if(nombreNotificacion=="noReciveIbeacons"){
                NotificationCompat.Builder notificacion =
                        new NotificationCompat.Builder(getContext(), CANAL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Medidas no encontradas")
                                .setContentText("El sensor no encuentra ninguna medida");
                notificationManager.notify(NOTIFICACION_ID, notificacion.build());
            }
            if(nombreNotificacion=="notificacionHumedadErronea"){
                NotificationCompat.Builder notificacion =
                        new NotificationCompat.Builder(getContext(), CANAL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Humedad ERRONEA")
                                .setContentText("Se ha producido un error respecto a los valores de humedad");
                notificationManager.notify(NOTIFICACION_ID, notificacion.build());
            }
            if(nombreNotificacion=="notificacionGasErronea"){
                NotificationCompat.Builder notificacion =
                        new NotificationCompat.Builder(getContext(), CANAL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Gas ERRONEA")
                                .setContentText("Se ha producido un error respecto a los valores de gas");
                notificationManager.notify(NOTIFICACION_ID, notificacion.build());
            }
            if(nombreNotificacion=="notificacionTemperaturaErronea"){
                NotificationCompat.Builder notificacion =
                        new NotificationCompat.Builder(getContext(), CANAL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Temperatura ERRONEA")
                                .setContentText("Se ha producido un error respecto a los valores de temperatura");
                notificationManager.notify(NOTIFICACION_ID, notificacion.build());
            }
            if(nombreNotificacion=="notificacionLimiteGas"){
                NotificationCompat.Builder notificacion =
                        new NotificationCompat.Builder(getContext(), CANAL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Calidad del aire pobre")
                                .setContentText("El valor de gas en esta zona es demasiado alto");
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
