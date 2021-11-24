/**
 * Adaptador.java
 * @fecha: 23/11/2021
 * @autor: Natxo Garcia Serquera
 *
 * @Descripcion:
 * Este fichero se encarga del layout Información
 * Muestra información de la aplicación, como usarla y que contiene
 */
package com.example.tricoenvironment.airlity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

/**
 * InformaciónActivity.
 * Página que muestra el layout de información
 */

public class InformacionActivity extends AppCompatActivity {

    boolean usuarioRegistrado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        cargarPreferencias();
        //-------------------------------------------
        //Para el menu
        //Pegar esto en todas las clases de activity
        //-------------------------------------------
        final DrawerLayout drawerLayout = findViewById(R.id.informacion_drawerLayout);
        findViewById(R.id.informacion_im_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.informacion_navigationView);
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


    /**
     * Métodos para el menú
     */
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
                lanzarMediciones();
                break;
            case R.id.menu_graficas:
                lanzarGraficas();
                break;
            case R.id.menu_informacion:
                break;
            case R.id.menu_soporte_tecnico:
                lanzarSoporteTecnico();
                break;
            case R.id.menu_nosotros:
                lanzarContactanos();
                break;
        }

    }

    private void lanzarSignIn() {
        Intent i = new Intent(this, SignInActivity.class);
        startActivity(i);
    }

    private void lanzarSoporteTecnico() {
    }

    private void lanzarMapa(){
        Intent i = new Intent(this, MapaActivity.class);
        startActivity(i);
    }

    private void lanzarPerfilUsuario(){
        Intent i = new Intent(this, PerfilUsuario.class);
        startActivity(i);
    }

    private void lanzarGraficas(){
        Intent i = new Intent(this, GraficasActivity.class);
        startActivity(i);
    }

    private void lanzarMediciones(){
        Intent i = new Intent(this, MedicionesActivity.class);
        startActivity(i);
    }

    private void lanzarContactanos(){

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