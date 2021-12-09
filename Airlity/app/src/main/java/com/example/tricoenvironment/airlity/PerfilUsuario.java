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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

public class PerfilUsuario extends AppCompatActivity {

    private TextView tv_nombreUsuario,  tv_correoElectronico, tv_macSensorUsuario;
    private EditText et_nombreUsuario,  et_telefonoUsuario;
    String macSensor;
    String nombreCambiado, telefonoCambiado;

    /*
    private IntentFilter intentFilter;
    private PerfilUsuario.ReceptorDatosUsuario receptor;
     */
    int codigo;
    Boolean sesionInicidad;
    String cuerpo;
    String idUsuarioDato, nombreUsuarioDato, correoUsuarioDato, contraseñaUsuarioDato, tokkenUsuarioDato, telefonoUsuarioDato, macUsuarioDato;

    Menu menu;
    Bundle datos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        SharedPreferences preferences=getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);
        cuerpo = preferences.getString("cuerpoUsuario", null);
        Log.d("Cuerpo",cuerpo+"");
        //------------------------------------------------------------
        //------------------------------------------------------------
        //Conexión con elementos del layout
        //------------------------------------------------------------
        //------------------------------------------------------------
        tv_nombreUsuario = findViewById(R.id.tv_nombreUsuario_perfilUsuario);
        et_nombreUsuario = findViewById(R.id.et_nombreUsuario_perfilUsuario);
        tv_correoElectronico = findViewById(R.id.et_correoUsuario_perfilUsuario);
        et_telefonoUsuario =findViewById(R.id.et_telefonoUsuario_perfilUsuario);
        tv_macSensorUsuario = findViewById(R.id.tv_infoSensor_perfilUsuario);
        tv_macSensorUsuario = findViewById(R.id.tv_infoSensor_perfilUsuario);

        Gson gson = new Gson();
        Root datosRoot = gson.fromJson(cuerpo, Root.class);

        tokkenUsuarioDato = datosRoot.getData().getToken();
        idUsuarioDato = datosRoot.getDatosUsuario().getId();
        nombreUsuarioDato = datosRoot.getDatosUsuario().getNombreUsuario();
        correoUsuarioDato = datosRoot.getDatosUsuario().getCorreo();
        telefonoUsuarioDato = datosRoot.getDatosUsuario().getTelefono().toString();
        macUsuarioDato = datosRoot.getDatosUsuario().getMacSensor().toString();

        tv_nombreUsuario.setText(nombreUsuarioDato);
        et_nombreUsuario.setText(nombreUsuarioDato);
        tv_correoElectronico.setText(correoUsuarioDato);
        et_telefonoUsuario.setText(telefonoUsuarioDato);


        SpannableString mitextoU = new SpannableString("MAC del sensor");
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        tv_macSensorUsuario.setText(mitextoU);
        tv_macSensorUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PerfilUsuario.this);
                alertDialog.setMessage(macUsuarioDato).setCancelable(true);
                AlertDialog titulo = alertDialog.create();
                titulo.setTitle("MAC del sensor");
                titulo.show();
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
        navigationView.getMenu().getItem(2).setVisible(false);
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
            case R.id.menu_signout:
                lanzarSignOut();
                break;
        }
    }

    private void lanzarSignOut() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(PerfilUsuario.this);
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
        Intent i = new Intent(this, ConoceTricoActivity.class);
        startActivity(i);
    }
}