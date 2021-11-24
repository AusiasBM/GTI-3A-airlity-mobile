/**
 * Adaptador.java
 * @fecha: 15/11/2021
 * @autor: Pere Márquez Barber
 *
 * @Descripcion:
 * Este fichero se encarga del layout Perfil Usuario, contiene la información del usuario registra
 * Esta página solamente saldrá si el usuario está registrado
 */
package com.example.tricoenvironment.airlity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class PerfilUsuario extends AppCompatActivity {

    private TextView tv_nombreUsuario,  tv_correoElectronico, tv_macSensorUsuario;
    private EditText et_nombreUsuario, et_apellidoUsuario, et_telefonoUsuario;
    boolean usuarioRegistrado;

    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        Log.d("HOLA", "ANTES"+usuarioRegistrado);
        //------------------------------------------------------------
        //------------------------------------------------------------
        //Conexión con elementos del layout
        //------------------------------------------------------------
        //------------------------------------------------------------
        tv_nombreUsuario = findViewById(R.id.tv_nombreUsuario_perfilUsuario);
        et_nombreUsuario = findViewById(R.id.et_nombreUsuario_perfilUsuario);
        et_apellidoUsuario = findViewById(R.id.et_apellidosUsuario_perfilUsuario);
        tv_correoElectronico = findViewById(R.id.et_correoUsuario_perfilUsuario);
        et_telefonoUsuario =findViewById(R.id.et_telefonoUsuario_perfilUsuario);
        tv_macSensorUsuario = findViewById(R.id.tv_infoSensor_perfilUsuario);

        //-------------------------------------------
        //Carga datos usuario
        //-------------------------------------------
        cargarPreferencias();

        tv_macSensorUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //-------------------------------------------
        //Para el menu
        //Pegar esto en todas las clases de activity
        //-------------------------------------------
        final DrawerLayout drawerLayout = findViewById(R.id.perfilusuario_drawerLayout);
        findViewById(R.id.perfilusuario_im_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.perfilusuario_navigationView);
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
        seleccionarItem(navigationView.getMenu().getItem(4));
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
        switch (itemDrawer.getItemId()) {
            case R.id.menu_mapa:
                lanzarMapa();
                break;
            case R.id.menu_signin:
                lanzarSignIn();
                break;
            case R.id.menu_perfilUsuario:
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

    private void lanzarMapa(){
        Intent i = new Intent(this, MapaActivity.class);
        startActivity(i);
    }

    private void lanzarSignIn(){
        Intent i = new Intent(this, SignInActivity.class);
        startActivity(i);
    }

    private void lanzarMediciones(){
        Intent i = new Intent(this, MedicionesActivity.class);
        startActivity(i);
    }

    private void lanzarContactanos(){

    }




    @Override
    protected void onPause() {
        super.onPause();
        et_nombreUsuario = findViewById(R.id.et_nombreUsuario_perfilUsuario);
        et_apellidoUsuario = findViewById(R.id.et_apellidosUsuario_perfilUsuario);
        guardarPreferencias(et_nombreUsuario+"", et_apellidoUsuario+"");
    }

    private void cargarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);

        String nombreUsuario = preferences.getString("nombreUsuario", "Sesion no iniciada todavia");
        String correoUsuario = preferences.getString("correoUsuario", "Sesion no iniciada todavia");
        String apellidoUsuario = preferences.getString("apellidoUsuario", "");

        String contraseñaUsuario = preferences.getString("contraseñaUsuario", "Sesion no iniciada todavia");
        int telefonoUsuario = preferences.getInt("telefonoUsuario", 00000);
        usuarioRegistrado = preferences.getBoolean("sesionIniciada", false);



        Log.d("HOLA", correoUsuario+"");
        Log.d("HOLA", nombreUsuario+"");
        Log.d("HOLA", contraseñaUsuario+"");
        Log.d("HOLA", telefonoUsuario+"");
        Log.d("HOLA", usuarioRegistrado+"");
        tv_nombreUsuario.setText(nombreUsuario +" "+ apellidoUsuario);
        et_nombreUsuario.setText(nombreUsuario + "");
        tv_correoElectronico.setText(correoUsuario+"");
        et_telefonoUsuario.setText(telefonoUsuario+"");

    }

    private void guardarPreferencias(String nombreUsuario, String apellidoUsuario) {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.tricoenvironment.airlity2", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nombreUsuario", nombreUsuario);
        editor.putString("apellidoUsuario", apellidoUsuario);

        editor.commit();
    }
}