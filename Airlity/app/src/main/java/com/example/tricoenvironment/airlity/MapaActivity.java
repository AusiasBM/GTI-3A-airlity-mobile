/**
 * Adaptador.java
 * @fecha: 18/11/2021
 * @autor: Pere Márquez Barber
 *
 * @Descripcion:
 * Este fichero se encarga del layout Mapa, contiene el API del mapa de google y un botón para el código QR
 */

package com.example.tricoenvironment.airlity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tricoenvironment.airlity.dialogos.DialogMarkerFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;


import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;


import java.util.ArrayList;
import java.util.List;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private List<Marker> markersMediciones = new ArrayList<>();
    private List<Marker> markersEstaciones = new ArrayList<>();
    private GoogleMap mMap;
    boolean usuarioRegistrado, usuarioLogeado;
    String idUsuarioDato, nombreUsuarioDato, correoUsuarioDato, contraseñaUsuarioDato, tokkenUsuarioDato, telefonoUsuarioDato, macUsuarioDato;

    private IntentFilter intentFilter;
    private MapaActivity.ReceptorGetMedicion receptor;
    //private MapaActivity.ReceptorDatosUsuario receptor;

    Bundle datos;
    Boolean sesionIniciada;
    String cuerpo, macSensor, rolUsuario;
    LogicaFake logicaFake;
    ConstraintLayout cl_leyenda;
    ImageView iv_close_leyenda, iv_abrir_leyenda, iv_tipoMedicion;
    LinearLayout l_iaq, l_estaciones;

    TextView tv_scan, tv_tipoMedicion;
    ImageView iv_filtros;

    LatLng posicionGandia = new LatLng(38.96797739, -0.19109882);
    LatLng posicionAlzira = new LatLng(39.14996506, -0.45786026);
    LatLng posicionAlcoi = new LatLng(38.70651691, -0.46721615);
    LatLng posicionBenidorm = new LatLng(38.57297101, -0.14637164);

    ArrayList<Estacion> estacionesOficiales = new ArrayList<Estacion>();

    private Intent intentServicioBLE = null;
    private boolean bluetoothActivo = false;

    int autorMediciones = 0, tipoMedicion = 0, mostrarEstacionesOficiales=0;
    long fechaInicio = 0;
    long fechaFin = 0;

    private  Intent intentServicioREST = null;

    MedicionMapa[] mediciones = new MedicionMapa[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        final FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        iv_filtros = findViewById(R.id.iv_filtros);
        tv_scan=findViewById(R.id.tv_scan);
        cl_leyenda=findViewById(R.id.cl_leyenda);
        iv_close_leyenda=findViewById(R.id.iv_close_leyenda);
        l_iaq=findViewById(R.id.l_IAQ);
        l_estaciones=findViewById(R.id.l_estaciones);
        iv_abrir_leyenda=findViewById(R.id.iv_borde);
        tv_tipoMedicion=findViewById(R.id.tv_tipoMedicion);
        iv_tipoMedicion=findViewById(R.id.iv_tipoMedicion);


        intentServicioREST = new Intent(this, ServicioLogicaFake.class);
        this.startService(intentServicioREST);

        cl_leyenda.setVisibility(VISIBLE);

        intentServicioBLE = new Intent(this, ServicioEscuharBeacons.class);
        //mostrarEstaciones();
        intentFilter = new IntentFilter();
        intentFilter.addAction("Get_Mediciones");
        receptor = new MapaActivity.ReceptorGetMedicion();
        registerReceiver(receptor, intentFilter);

        logicaFake = new LogicaFake();

        logicaFake.getMedicionesPorTiempoZona(this);

        SharedPreferences preferences=getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);
        sesionIniciada = preferences.getBoolean("usuarioLogeado", false);

        Log.d("sesion", sesionIniciada +"");

        if(sesionIniciada == false){
            fab.setVisibility(GONE);
            iv_filtros.setVisibility(GONE);
            tv_scan.setVisibility(GONE);
        }else {

            macUsuarioDato = preferences.getString("mac", "");
            rolUsuario= preferences.getString("rol", "");

        }



        Estacion estacionGandia = new Estacion("Gandia", "https://webcat-web.gva.es/webcat_web/img/Estaciones/ES_00005.jpg", posicionGandia, "46131002");
        Estacion estacionAlcoi = new Estacion("Alcoi - Verge", "https://webcat-web.gva.es/webcat_web/img/Estaciones/ES_00278.jpg", posicionAlcoi, "03009006");
        Estacion estacionAlzira = new Estacion("Alzira", "https://webcat-web.gva.es/webcat_web/img/Estaciones/ES_00239.jpg", posicionAlzira, "46017002");
        Estacion estacionBenidorm = new Estacion("Benidorm", "https://webcat-web.gva.es/webcat_web/img/Estaciones/ES_00329.jpg", posicionBenidorm, "03031002");
        estacionesOficiales.add(estacionGandia);
        estacionesOficiales.add(estacionAlcoi);
        estacionesOficiales.add(estacionAlzira);
        estacionesOficiales.add(estacionBenidorm);

        //-------------------------------------------
        //Para el menu
        //Pegar esto en todas las clases de activity
        //-------------------------------------------
        final DrawerLayout drawerLayout = findViewById(R.id.mapa_drawerLayout);
        findViewById(R.id.mapa_im_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.mapa_navigationView);
        navigationView.setItemIconTintList(null);

        if (sesionIniciada){
            if (!rolUsuario.equals("Admin")){
                navigationView.getMenu().getItem(6).setVisible(false);
            }
            navigationView.getMenu().getItem(2).setVisible(false);
        }else{
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(3).setVisible(false);
            navigationView.getMenu().getItem(4).setVisible(false);
            navigationView.getMenu().getItem(5).setVisible(false);
            navigationView.getMenu().getItem(6).setVisible(false);
        }
        prepararDrawer(navigationView);
        //-------------------------------------------
        //-------------------------------------------
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bluetoothActivo){
                    Log.d("Escaner", " Inicio del servicio" );
                    Log.d("Escaner", macUsuarioDato + "");
                    //ServicioEscucharBeacons
                    intentServicioBLE.putExtra("macDispositivo", macUsuarioDato);
                    getApplicationContext().startService(intentServicioBLE);
                    fab.setImageResource(R.drawable.pausa);
                    tv_scan.setVisibility(GONE);
                    bluetoothActivo=true;


                }else{
                    getApplicationContext().stopService(intentServicioBLE);
                    fab.setImageResource(R.drawable.play);
                    tv_scan.setVisibility(VISIBLE);
                    Toast.makeText(getApplicationContext(), "Se ha detenido la búsqueda de beacons", Toast.LENGTH_SHORT).show();
                    bluetoothActivo=false;
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        iv_filtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FiltrosActivity.class);
                intent.putExtra("autorMediciones", autorMediciones);
                intent.putExtra("fechaInicio", fechaInicio);
                intent.putExtra("fechaFin", fechaFin);
                intent.putExtra("tipoMedicion", tipoMedicion);
                startActivityForResult(intent, 200);

            }
        });

        iv_close_leyenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cl_leyenda.setVisibility(GONE);
            }
        });

        iv_abrir_leyenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cl_leyenda.getVisibility()==VISIBLE){
                    cl_leyenda.setVisibility(GONE);
                } else{
                    cl_leyenda.setVisibility(VISIBLE);
                }

            }
        });
    }

    

    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        seleccionarItem(menuItem);
                        return true;
                    }
                });
    }

    private void seleccionarItem(MenuItem itemDrawer) {

        switch (itemDrawer.getItemId()) {
            case R.id.menu_mapa:
                break;
            case R.id.menu_signin:
                lanzarSignIn();
                break;
            case R.id.menu_perfilUsuario:
                lanzarPerfilUsuario();
                break;
            case R.id.menu_mediciones:
                lanzarMediciones();
                break;
            case R.id.menu_graficas:
                lanzarGraficas();
                break;
            case R.id.menu_informacion:
                lanzarInformacion();
                break;
            case R.id.menu_soporte_tecnico:
                lanzarSoporteTecnico();
                break;
            case R.id.menu_nosotros:
                lanzarContactanos();
                break;
            case R.id.menu_signout:
                lanzarSignOut();
                break;
            case R.id.menu_sensores:
                lanzarSensores();
                break;
        }

    }

    private void lanzarSensores() {
        Intent i = new Intent(this, SensoresInactivosActivity.class);
        startActivity(i);
    }

    private void lanzarSignOut() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(MapaActivity.this);
        alertDialog.setMessage("¿Segur que desea cerrar sesión?").setCancelable(false)
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datos=null;
                        Intent i = new Intent(getApplicationContext(), MapaActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        SharedPreferences settings = getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);
                        settings.edit().clear().commit();
                        startActivity(i);
                        dialog.cancel();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog titulo = alertDialog.create();
        titulo.setTitle("Cerrar sesión");
        titulo.show();
    }

    private void lanzarGraficas() {
        Intent i = new Intent(this, GraficasActivity.class);
        startActivity(i);
    }

    private void lanzarInformacion() {
        Intent i = new Intent(this, InformacionActivity.class);
        startActivity(i);

    }

    private void lanzarSoporteTecnico() {
        Intent i = new Intent(this, SoporteTecnicoActivity.class);
        startActivity(i);
    }

    private void lanzarPerfilUsuario() {
        Intent i = new Intent(this, PerfilUsuario.class);
        startActivity(i);
    }

    private void lanzarSignIn() {
        Intent i = new Intent(this, SignInActivity.class);
        startActivity(i);
    }

    private void lanzarMediciones() {
        Intent i = new Intent(this, MedicionesActivity.class);
        startActivity(i);
    }

    private void lanzarContactanos() {
        Intent i = new Intent(this, ConoceTricoActivity.class);
        startActivity(i);
    }

    /**
     * Método para configurar el mapa y los componentes como posición, vista del mapa, zoom, ubicación actual, ...
     * @param googleMap
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        // Add a marker in Sydney and move the camera
        LatLng epsgGandia = new LatLng(38.9959757, -0.1658417);


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMinZoomPreference(6.0f);
        mMap.setMaxZoomPreference(25.0f);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(epsgGandia, 13));

        //crearMapadeCalor();
        //mostrarEstaciones();
    }

    private void crearMapadeCalor() {
        HeatmapTileProvider provider;
        TileOverlay mOverlay;

        float[] list= new float[mediciones.length];
        List<LatLng> result = new ArrayList<>();

        List<WeightedLatLng> datos = new ArrayList<>();
        for(int i = 0; i <mediciones.length; i++) {

            LatLng l = new LatLng(mediciones[i].getLatitud(), mediciones[i].getLongitud());
            double m = mediciones[i].getMedida();
            WeightedLatLng w = new WeightedLatLng( l, m);
            datos.add(w);
            result.add(new LatLng(mediciones[i].getLatitud(), mediciones[i].getLongitud()));
        }

        Log.d("MEDICIONES MAPA: ", datos + "");
        Log.d("MEDICIONES MAPA: ", datos.size() + "");

        provider = new HeatmapTileProvider.Builder()
                .weightedData(datos)
                .radius(20)
                .build();

        // Create the gradient.
        int[] colors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                0.01f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);
        provider.setGradient(gradient);

        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
    }

    private void mostrarEstaciones() {
        if (mostrarEstacionesOficiales == 0) {
            for (int i = 0; i<estacionesOficiales.size();i++){
                mMap.addMarker(new MarkerOptions().position(estacionesOficiales.get(i).posicionEstacion).title("Estación: "+estacionesOficiales.get(i).nombreEstacion));
            }

            markersEstaciones = new ArrayList<>();

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    if (marker.getTitle().startsWith("Estación: ")){

                        SharedPreferences sharedPreferences = getSharedPreferences("infoEstacion", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("nombreEstacion", marker.getTitle());
                        editor.putString("fotoEstacion", buscarEstacionPorLatLng(marker.getPosition()).fotoEstacion);
                        editor.putString("codigoEstacion", buscarEstacionPorLatLng(marker.getPosition()).codigoEstacion);
                        editor.putFloat("latitudEstacion", (float)marker.getPosition().latitude);
                        editor.putFloat("longitudEstacion", (float)marker.getPosition().longitude);
                        editor.commit();
                        Intent i = new Intent();
                        i.setAction("info_mediciones");
                        i.putExtra("nombreEstacion", marker.getTitle());
                        getApplicationContext().sendBroadcast(i);

                        DialogMarkerFragment dialogMarkerFragment=new DialogMarkerFragment();
                        dialogMarkerFragment.show(getSupportFragmentManager(), "DialogMarkerFragment");
                    }
                    return false;
                }
            });
        }else{
            mMap.clear();
            mostrarMediciones();
        }
    }

    private Estacion buscarEstacionPorLatLng( LatLng posicion){
        for (int i = 0; i< estacionesOficiales.size(); i++){
            if (estacionesOficiales.get(i).posicionEstacion.equals(posicion)) return estacionesOficiales.get(i);
        }
        return estacionesOficiales.get(0);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private class ReceptorGetMedicion extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int codigo = intent.getIntExtra("codigoMedicion", 0);

            cuerpo = intent.getStringExtra("Mediciones");
            if (codigo == 200) {
                Gson gson = new Gson();
                mediciones = gson.fromJson(cuerpo, MedicionMapa[].class);
                //mostrarMediciones();
                crearMapadeCalor();
            } else {
                //Toast.makeText(MapaActivity.this, "Fallo al recibir mediciones", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void mostrarMediciones(){
       for(Marker marker: markersMediciones){
           marker.remove();
       }

       markersMediciones = new ArrayList<>();

        for(MedicionMapa medicion: mediciones) {

            if (autorMediciones==1){
                if (!medicion.getMacSensor().equals(macSensor)){
                    continue;
                }
            }

            if(fechaInicio!=0){
                if (medicion.getFecha()<fechaInicio){
                    continue;
                }
            }

            if(fechaFin!=0){
                if (medicion.getFecha()>fechaFin){
                    continue;
                }
            }

            MarkerOptions marker = new MarkerOptions();
            LatLng coordenada = new LatLng(medicion.getLatitud(), medicion.getLongitud());

            double valorMedicion = medicion.getMedida();
            String tipoMed = medicion.getTipoMedicion();

            if (tipoMedicion==0){
                Log.d("TIPOMEDICION", "0");
                mMap.clear();
                crearMapadeCalor();
                mostrarEstaciones();
            } else if (tipoMedicion==1){
                Log.d("TIPOMEDICION", "1");
                mostrarEstaciones();
                if (tipoMed.equals("SO2")){
                    Log.d("TIPOMEDICION2", "1"+ medicion);
                    marker = marker.position(coordenada).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)); //.title(medicion.getTipoMedida());
                    Marker marcador = mMap.addMarker(marker);
                    marcador.setTitle("Medición "+ medicion.getTipoMedicion());
                    marcador.setSnippet("       "+valorMedicion);
                    markersMediciones.add(marcador);
                }
            } else if (tipoMedicion==2){
                Log.d("TIPOMEDICION", "2");
                mostrarEstaciones();
                if (tipoMed.equals("O3")){
                    Log.d("TIPOMEDICION2", "2"+medicion);
                    marker = marker.position(coordenada).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)); //.title(medicion.getTipoMedida());
                    Marker marcador = mMap.addMarker(marker);
                    marcador.setTitle("Medición "+ medicion.getTipoMedicion());
                    marcador.setSnippet("       "+valorMedicion);
                    markersMediciones.add(marcador);
                }

            } else if (tipoMedicion==3){
                Log.d("TIPOMEDICION", "3");
                mostrarEstaciones();
                if (tipoMed.equals("NO2")){
                    Log.d("TIPOMEDICION2", "3"+medicion);
                    marker = marker.position(coordenada).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)); //.title(medicion.getTipoMedida());
                    Marker marcador = mMap.addMarker(marker);
                    marcador.setTitle("Medición "+ medicion.getTipoMedicion());
                    marcador.setSnippet("       "+valorMedicion);
                    markersMediciones.add(marcador);
                }
            } else if (tipoMedicion==4){
                Log.d("TIPOMEDICION", "4"+medicion);
                mostrarEstaciones();
                if (tipoMed.equals("CO")){
                    Log.d("TIPOMEDICION2", "4");
                    marker = marker.position(coordenada).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)); //.title(medicion.getTipoMedida());
                    Marker marcador = mMap.addMarker(marker);
                    marcador.setTitle("Medición "+ medicion.getTipoMedicion());
                    marcador.setSnippet("       "+valorMedicion);
                    markersMediciones.add(marcador);
                }
            }  else {
                continue;
            }
            //datos.addProperty("Temperatura(ºC)", valorTemperatura);
            //datos.addProperty("Humedad(%)", valorHumedad);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==200 && resultCode==RESULT_OK){
            autorMediciones= data.getIntExtra("autorMediciones", 0);
            //mostrarEstacionesOficiales= data.getIntExtra("mostrarEstaciones", 0);
            fechaInicio = data.getLongExtra("fechaInicio", 0);
            fechaFin = data.getLongExtra("fechaFin", 0);
            tipoMedicion = data.getIntExtra("tipoMedicion", 0);

            if (tipoMedicion==0){
                tv_tipoMedicion.setText("  ICA             ");
            } else if (tipoMedicion==1){
                tv_tipoMedicion.setText("  SO2             ");
            } else if (tipoMedicion==2){
                tv_tipoMedicion.setText("  O3              ");
            } else if (tipoMedicion==3){
                tv_tipoMedicion.setText("  NO2             ");
            } else if (tipoMedicion==4){
                tv_tipoMedicion.setText("  CO              ");
            }

            if (mostrarEstacionesOficiales==0){
                l_estaciones.setVisibility(VISIBLE);
            }else{
                l_estaciones.setVisibility(GONE);
                //mMap.clear();

            }
            mostrarMediciones();
            mostrarEstaciones();
            //crearMapadeCalor();

        }
    }
}
