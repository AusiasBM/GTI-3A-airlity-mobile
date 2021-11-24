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
import android.content.Context;
import android.content.Intent;
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
    boolean usuarioRegistrado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        cargarPreferencias();
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

        /**
         * Para la cámara QR
         */
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
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

        NavigationView navigationView = findViewById(R.id.mapa_navigationView);
        navigationView.setItemIconTintList(null);
        if (usuarioRegistrado){
            Log.d("HOLA", "usus");
            navigationView.getMenu().getItem(2).setVisible(false);
        }else{
            Log.d("HOLA", "susu");
            navigationView.getMenu().getItem(3).setVisible(false);
            navigationView.getMenu().getItem(4).setVisible(false);
            navigationView.getMenu().getItem(5).setVisible(false);
        }
        prepararDrawer(navigationView);
        //-------------------------------------------
        //-------------------------------------------
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

        SharedPreferences preferences=getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);
        boolean sesionIniciada = preferences.getBoolean("sesionIniciada", false);

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
        }

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

        String datos = intentResult.getContents();
        Toast.makeText(this, "Mac del beacon detectado: "+ datos, Toast.LENGTH_SHORT).show();
    }

    private void cargarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);

        String nombreUsuario = preferences.getString("nombreUsuario", "Sesion no iniciada todavia");
        String correoUsuario = preferences.getString("correoUsuario", "Sesion no iniciada todavia");
        String apellidoUsuario = preferences.getString("apellidoUsuario", "");

        String contraseñaUsuario = preferences.getString("contraseñaUsuario", "Sesion no iniciada todavia");
        int telefonoUsuario = preferences.getInt("telefonoUsuario", 00000);
        usuarioRegistrado = preferences.getBoolean("sesionIniciada", false);

    }
}