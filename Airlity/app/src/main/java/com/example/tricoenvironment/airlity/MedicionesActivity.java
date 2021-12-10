/**
 * MedicionesActivity.java
 * @fecha: 07/10/2021
 * @autor: Aitor Benítez Estruch
 *
 * @Descripcion:
 * Este fichero abre la primera actividad de la aplicación.
 */

package com.example.tricoenvironment.airlity;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MedicionesActivity extends AppCompatActivity {

    private String[] nombres = new String[]{"Buscar dispositivo BLE","Listado Últimas Mediciones"};
    private static final int CODIGO_PETICION_PERMISOS = 11223344;
    Boolean sesionInicidad;
    String cuerpo;
    /**
     * Método onCreate se ejecuta antes de iniciar la actividad MedicionesActivity
     *
     * savedInstanceState:Bundle -> onCreate() ->
     *
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediciones);


        SharedPreferences preferences=getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);
        sesionInicidad = preferences.getBoolean("usuarioLogeado", false);
        cuerpo = preferences.getString("cuerpoUsuario", null);

        //Comprobamos si la app tiene los permisos para utilizar el bluetooth
        permisosBluetooth();

        //Referenciar el objeto TabLayout com la vista y añadir los tabs
        //Funcionalidad para crear los TabLayouts
        ViewPager2 viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(this));
        TabLayout tabs = findViewById(R.id.tabs);
        new TabLayoutMediator(tabs, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position){
                        tab.setText(nombres[position]);
                    }
                }
        ).attach();

        //-------------------------------------------
        //Para el menu
        //Pegar esto en todas las clases de activity
        //-------------------------------------------
        final DrawerLayout drawerLayout = findViewById(R.id.mediciones_drawerLayout);
        findViewById(R.id.mediciones_im_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.mediciones_navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().getItem(2).setVisible(false);

        prepararDrawer(navigationView);
        //-------------------------------------------
        //-------------------------------------------

    }

    /**
     * Método permisosBluetooth que comprueba la app tiene ya los permisos para utilizar
     * el bluetooth del dispositivo y en caso de no tenerlos los pedirá.
     *
     * permisosBluetooth() ->
     *
     */
    private void permisosBluetooth(){

        //Comprobamos si no tenemos los permisos...
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(
                    MedicionesActivity.this,
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION},
                    CODIGO_PETICION_PERMISOS);
        }
        else {
            Log.d("PERMISOS BLUETOOTH", " Parece que YA tengo los permisos necesarios !!!!");

        }
    }


    /**
     * Método onRequestPermissionsResult que comprueba el resultado de pedir permiso para utilizar
     * el bluetooth del dispositivo para la aplicación y en caso afirmativo, los concederá.
     *
     *
     *requestCode:Z,
     *permissions:[Texto],
     *grantResults: [Z] -> onRequestPermissionsResult() ->
     *
     * @param requestCode Valor entero
     * @param permissions
     * @param grantResults
     *
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults);

        switch (requestCode) {
            case CODIGO_PETICION_PERMISOS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d("PERMISOS BLUETOOTH", " onRequestPermissionResult(): permisos concedidos  !!!!");
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }  else {

                    Log.d("PERMISOS BLUETOOTH", " onRequestPermissionResult(): Socorro: permisos NO concedidos  !!!!");

                }
                return;
        }
    }
    ///////////////////////////

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
                lanzarMapa();
                break;
            case R.id.menu_signin:
                lanzarSignIn();
                break;
            case R.id.menu_perfilUsuario:
                lanzarPerfilUsuario();
                break;
            case R.id.menu_mediciones:
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
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(MedicionesActivity.this);
        alertDialog.setMessage("¿Segur que desea cerrar sesión?").setCancelable(false)
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

    private void lanzarMapa(){
        Intent i = new Intent(this, MapaActivity.class);
        startActivity(i);
    }

    private void lanzarPerfilUsuario(){
        Intent i = new Intent(this, PerfilUsuario.class);
        startActivity(i);
    }

    private void lanzarSignIn(){
        Intent i = new Intent(this, SignInActivity.class);
        startActivity(i);
    }

    private void lanzarContactanos(){
        Intent i = new Intent(this, ConoceTricoActivity.class);
        startActivity(i);
    }
}