/**
 * Adaptador.java
 * @fecha: 18/11/2021
 * @autor: Pere Márquez Barber
 *
 * @Descripcion:
 * Este fichero se encarga del layout Mapa, contiene el API del mapa de google y un botón para el código QR
 */

package com.example.tricoenvironment.airlity;

import static android.view.View.VISIBLE;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuItemWrapperICS;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.util.ArrayList;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    boolean usuarioRegistrado, usuarioLogeado;
    String idUsuarioDato, nombreUsuarioDato, correoUsuarioDato, contraseñaUsuarioDato, tokkenUsuarioDato, telefonoUsuarioDato, macUsuarioDato;

    private IntentFilter intentFilter;
    private MapaActivity.ReceptorGetMedicion receptor;
    //private MapaActivity.ReceptorDatosUsuario receptor;

    Bundle datos;
    Boolean sesionInicidad;
    String cuerpo, macSensor;
    LogicaFake logicaFake;

    TextView tv_scan;

    LatLng posicionGandia = new LatLng(38.96797739, -0.19109882);
    LatLng posicionAlzira = new LatLng(39.14996506, -0.45786026);
    LatLng posicionAlcoi = new LatLng(38.70651691, -0.46721615);
    LatLng posicionBenidorm = new LatLng(38.57297101, -0.14637164);

    ArrayList<Estacion> estacionesOficiales = new ArrayList<Estacion>();

    private Intent intentServicioBLE = null;
    private boolean bluetoothActivo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        final FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        FloatingActionButton fabFiltros = findViewById(R.id.fab_filtro);
        tv_scan=findViewById(R.id.tv_scan);

        intentServicioBLE = new Intent(this, ServicioEscuharBeacons.class);

        intentFilter = new IntentFilter();
        intentFilter.addAction("Get_Mediciones");
        receptor = new MapaActivity.ReceptorGetMedicion();
        registerReceiver(receptor, intentFilter);

        logicaFake = new LogicaFake();

        logicaFake.obtenerUltimasMediciones(this);

        SharedPreferences preferences=getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);
        sesionInicidad = preferences.getBoolean("usuarioLogeado", false);
        cuerpo = preferences.getString("cuerpoUsuario", null);
        Log.d("sesion", sesionInicidad+"");
        Log.d("sesion", sesionInicidad+", "+cuerpo);
        if(sesionInicidad==false && cuerpo==null){
            fab.setVisibility(View.GONE);
            fabFiltros.setVisibility(View.GONE);
            tv_scan.setVisibility(View.GONE);
        }else {
            Gson gson = new Gson();
            Root datosRoot = gson.fromJson(cuerpo, Root.class);

            macUsuarioDato = datosRoot.getDatosUsuario().getMacSensor().toString();
        }



        Estacion estacionGandia = new Estacion("Gandia", "https://webcat-web.gva.es/webcat_web/img/Estaciones/ES_00005.jpg", posicionGandia, "46131002");
        Estacion estacionAlcoi = new Estacion("Alcoi - Verge dels Lliris", "https://webcat-web.gva.es/webcat_web/img/Estaciones/ES_00278.jpg", posicionAlcoi, "03009006");
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

        if (sesionInicidad && cuerpo!=null){
            navigationView.getMenu().getItem(2).setVisible(false);
        }else{

            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(3).setVisible(false);
            navigationView.getMenu().getItem(4).setVisible(false);
            navigationView.getMenu().getItem(5).setVisible(false);
        }
        prepararDrawer(navigationView);
        //-------------------------------------------
        //-------------------------------------------
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bluetoothActivo){
                    Log.d("Escaner", " Inicio del servicio" );
                    //ServicioEscucharBeacons
                    intentServicioBLE.putExtra("macDispositivo", macSensor);
                    getApplicationContext().startService(intentServicioBLE);
                    fab.setImageResource(R.drawable.pausa);
                    tv_scan.setVisibility(View.GONE);
                    bluetoothActivo=true;


                }else{
                    getApplicationContext().stopService(intentServicioBLE);
                    fab.setImageResource(R.drawable.play);
                    tv_scan.setVisibility(VISIBLE);
                    bluetoothActivo=false;
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        }

    }

    private void lanzarSignOut() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(MapaActivity.this);
        alertDialog.setMessage("¿Segur que desea cerrar sesión?").setCancelable(false)
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datos=null;
                        Intent i = new Intent(getApplicationContext(), MapaActivity.class);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(epsgGandia, 18));

        //ESTACIONES OFICIALES DE MEDIDA
        for (int i = 0; i<estacionesOficiales.size();i++){
            mMap.addMarker(new MarkerOptions().position(estacionesOficiales.get(i).posicionEstacion).title(estacionesOficiales.get(i).nombreEstacion));
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (marker.getTitle().length()<30){

                    SharedPreferences sharedPreferences = getSharedPreferences("infoEstacion", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("nombreEstacion", marker.getTitle());
                    editor.putString("fotoEstacion", buscarEstacionPorLatLng(marker.getPosition()).fotoEstacion);
                    editor.putString("codigoEstacion", buscarEstacionPorLatLng(marker.getPosition()).codigoEstacion);
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
                Medicion[] mediciones = gson.fromJson(cuerpo, Medicion[].class);
                for(Medicion medicion: mediciones){
                    MarkerOptions marker = new MarkerOptions();
                    LatLng coordenada = new LatLng(medicion.getLatitud(), medicion.getLongitud());
                    String tipoMedicion = medicion.getTipoMedicion();

                    double valorMedicion = medicion.getMedida();
                    int valorTemperatura = medicion.getTemperatura();
                    int valorHumedad = medicion.getHumedad();

                    JsonObject datos = new JsonObject();

                    if (tipoMedicion.equals("O2")){
                        datos.addProperty("Valor 02", valorMedicion);
                        marker = marker.position(coordenada).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)); //.title(medicion.getTipoMedida());
                    }else if(tipoMedicion.equals("CO2")){
                        datos.addProperty("Valor C02", valorMedicion);
                        marker = marker.position(coordenada).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)); //.title(medicion.getTipoMedida());
                    }else if(tipoMedicion.equals("03")){
                        datos.addProperty("Valor 03", valorMedicion);
                        marker = marker.position(coordenada).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)); //.title(medicion.getTipoMedida());
                    }else if(tipoMedicion.equals("NO2")){
                        datos.addProperty("Valor NO2", valorMedicion);
                        marker = marker.position(coordenada).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)); //.title(medicion.getTipoMedida());
                    }else {
                        datos.addProperty("Valor", valorMedicion);
                        marker = marker.position(coordenada).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)); //.title(medicion.getTipoMedida());
                    }
                    datos.addProperty("Temperatura(ºC)", valorTemperatura);
                    datos.addProperty("Humedad(%)", valorHumedad);
                    mMap.addMarker(marker).setTitle(datos+"");
                }

            } else {
                Toast.makeText(MapaActivity.this, "Fallo al recibir mediciones", Toast.LENGTH_LONG).show();
            }
        }
    }

}
