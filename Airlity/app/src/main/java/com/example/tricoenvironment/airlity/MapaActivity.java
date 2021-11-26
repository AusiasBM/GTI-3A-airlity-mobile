/**
 * Adaptador.java
 * @fecha: 18/11/2021
 * @autor: Pere Márquez Barber
 *
 * @Descripcion:
 * Este fichero se encarga del layout Mapa, contiene el API del mapa de google y un botón para el código QR
 */

package com.example.tricoenvironment.airlity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuItemWrapperICS;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    boolean usuarioRegistrado, usuarioLogeado;
    String idUsuarioDato, nombreUsuarioDato, correoUsuarioDato, contraseñaUsuarioDato, tokkenUsuarioDato, telefonoUsuarioDato, macUsuarioDato;
    private IntentFilter intentFilter;
    //private MapaActivity.ReceptorDatosUsuario receptor;

    Bundle datos;
    Boolean usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        datos = getIntent().getExtras();
        if(datos!=null){
            usuarioRegistrado = datos.getBoolean("sesionIniciada");
            tokkenUsuarioDato = datos.getString("tokkenUsuario");
            idUsuarioDato  = datos.getString("idUsuario");
            nombreUsuarioDato = datos.getString("nombrUsuario");
            correoUsuarioDato = datos.getString("correoUsuario");
            telefonoUsuarioDato = datos.getString("telefonoUsuario");
            contraseñaUsuarioDato = datos.getString("contraseñaUsuario");
        }else{
            usuarioRegistrado = false;
            fab.setVisibility(View.GONE);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.tricoenvironment.airlity", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (usuarioRegistrado){
            editor.putBoolean("usuarioLogeado", true);
        }else{
            editor.putBoolean("usuarioLogeado", false);
        }
        editor.commit();

        /*
        SharedPreferences preferences=getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);
        usuarioRegistrado = preferences.getBoolean("sesionIniciada", false);


        Intent intent=getIntent();
        intentFilter = new IntentFilter();
        intentFilter.addAction("DatosUsuario");
        receptor = new MapaActivity.ReceptorDatosUsuario();
         */

        //-------------------------------------------
        //Para el menu
        //Pegar esto en todas las clases de activity
        //-------------------------------------------

        SharedPreferences preferences=getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);
        usuario = preferences.getBoolean("usuarioLogeado", false);

        final DrawerLayout drawerLayout = findViewById(R.id.mapa_drawerLayout);
        findViewById(R.id.mapa_im_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.mapa_navigationView);
        navigationView.setItemIconTintList(null);

        if (usuario){
            navigationView.getMenu().getItem(2).setVisible(false);
        }else{
            navigationView.getMenu().getItem(3).setVisible(false);
            navigationView.getMenu().getItem(4).setVisible(false);
            navigationView.getMenu().getItem(5).setVisible(false);
        }
        prepararDrawer(navigationView);
        //-------------------------------------------
        //-------------------------------------------

        /**
         * Para la cámara QR
         */

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Escanee el QR de un beacon", Toast.LENGTH_LONG).show();
                new IntentIntegrator(MapaActivity.this).initiateScan();
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
        i.putExtra("sesionIniciada", usuario);
        i.putExtra("tokkenUsuario", tokkenUsuarioDato);
        i.putExtra("idUsuario", idUsuarioDato);
        i.putExtra("nombrUsuario", nombreUsuarioDato);
        i.putExtra("correoUsuario", correoUsuarioDato);
        i.putExtra("telefonoUsuario", telefonoUsuarioDato);
        i.putExtra("contraseñaUsuario", contraseñaUsuarioDato);
        if (macUsuarioDato!=null){
            i.putExtra("macUsuario", macUsuarioDato);
        }

        startActivity(i);
    }

    private void lanzarInformacion() {
        Intent i = new Intent(this, InformacionActivity.class);
        i.putExtra("sesionIniciada", usuario);
        i.putExtra("tokkenUsuario", tokkenUsuarioDato);
        i.putExtra("idUsuario", idUsuarioDato);
        i.putExtra("nombrUsuario", nombreUsuarioDato);
        i.putExtra("correoUsuario", correoUsuarioDato);
        i.putExtra("telefonoUsuario", telefonoUsuarioDato);
        i.putExtra("contraseñaUsuario", contraseñaUsuarioDato);
        if (macUsuarioDato!=null){
            i.putExtra("macUsuario", macUsuarioDato);
        }
        startActivity(i);
    }

    private void lanzarSoporteTecnico() {
        Intent i = new Intent(this, SoporteTecnicoActivity.class);
        i.putExtra("sesionIniciada", usuario);
        i.putExtra("tokkenUsuario", tokkenUsuarioDato);
        i.putExtra("idUsuario", idUsuarioDato);
        i.putExtra("nombrUsuario", nombreUsuarioDato);
        i.putExtra("correoUsuario", correoUsuarioDato);
        i.putExtra("telefonoUsuario", telefonoUsuarioDato);
        i.putExtra("contraseñaUsuario", contraseñaUsuarioDato);
        if (macUsuarioDato!=null){
            i.putExtra("macUsuario", macUsuarioDato);
        }
        startActivity(i);
    }

    private void lanzarPerfilUsuario() {
        Intent i = new Intent(this, PerfilUsuario.class);
        i.putExtra("sesionIniciada", usuario);
        i.putExtra("tokkenUsuario", tokkenUsuarioDato);
        i.putExtra("idUsuario", idUsuarioDato);
        i.putExtra("nombrUsuario", nombreUsuarioDato);
        i.putExtra("correoUsuario", correoUsuarioDato);
        i.putExtra("telefonoUsuario", telefonoUsuarioDato);
        i.putExtra("contraseñaUsuario", contraseñaUsuarioDato);
        if (macUsuarioDato!=null){
            i.putExtra("macUsuario", macUsuarioDato);
        }
        startActivity(i);
    }

    private void lanzarSignIn() {
        Intent i = new Intent(this, SignInActivity.class);
        i.putExtra("sesionIniciada", usuario);
        startActivity(i);
    }

    private void lanzarMediciones() {
        Intent i = new Intent(this, MedicionesActivity.class);
        i.putExtra("sesionIniciada", usuario);
        i.putExtra("tokkenUsuario", tokkenUsuarioDato);
        i.putExtra("idUsuario", idUsuarioDato);
        i.putExtra("nombrUsuario", nombreUsuarioDato);
        i.putExtra("correoUsuario", correoUsuarioDato);
        i.putExtra("telefonoUsuario", telefonoUsuarioDato);
        i.putExtra("contraseñaUsuario", contraseñaUsuarioDato);
        if (macUsuarioDato!=null){
            i.putExtra("macUsuario", macUsuarioDato);
        }
        startActivity(i);
    }

    private void lanzarContactanos() {
        Intent i = new Intent(this, ConoceTricoActivity.class);
        i.putExtra("sesionIniciada", usuario);
        i.putExtra("tokkenUsuario", tokkenUsuarioDato);
        i.putExtra("idUsuario", idUsuarioDato);
        i.putExtra("nombrUsuario", nombreUsuarioDato);
        i.putExtra("correoUsuario", correoUsuarioDato);
        i.putExtra("telefonoUsuario", telefonoUsuarioDato);
        i.putExtra("contraseñaUsuario", contraseñaUsuarioDato);
        if (macUsuarioDato!=null){
            i.putExtra("macUsuario", macUsuarioDato);
        }
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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng epsgGandia = new LatLng(38.9959757, -0.1658417);
        mMap.addMarker(new MarkerOptions()
                .position(epsgGandia));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMinZoomPreference(6.0f);
        mMap.setMaxZoomPreference(12.0f);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(epsgGandia, 18));
    }

    /**
     * Método llamado al escanear un QR
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        macUsuarioDato = intentResult.getContents();
        Toast.makeText(this, "Mac del beacon encontrado: "+ datos, Toast.LENGTH_SHORT).show();
    }

     /*
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receptor, intentFilter);
    }

   private class ReceptorDatosUsuario extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            tokkenUsuarioDato = intent.getStringExtra("tokkenUsuario");
            idUsuarioDato = intent.getStringExtra("idUsuario");
            nombreUsuarioDato = intent.getStringExtra("nombreUsuario");
            correoUsuarioDato = intent.getStringExtra("correoUsuario");
            contraseñaUsuarioDato = intent.getStringExtra("contraseñaUsuario");
            telefonoUsuarioDato = intent.getStringExtra("telefonoUsuario");

            //Log.d("HOLA4", correoUsuarioDato);
        }

    }

     */
}
