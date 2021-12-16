package com.example.tricoenvironment.airlity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

public class ConoceTricoActivity extends AppCompatActivity {

    boolean usuarioRegistrado, usuario;
    String idUsuarioDato, nombreUsuarioDato, correoUsuarioDato, contraseñaUsuarioDato, tokkenUsuarioDato, telefonoUsuarioDato, macUsuarioDato;

    Bundle datos;
    Boolean sesionInicidad;
    String cuerpo, rolUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conoce_trico);

        SharedPreferences preferences=getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);
        sesionInicidad = preferences.getBoolean("usuarioLogeado", false);
        cuerpo = preferences.getString("cuerpoUsuario", null);

        cuerpo = preferences.getString("cuerpoUsuario", null);
        Gson gson = new Gson();
        Root datosRoot = gson.fromJson(cuerpo, Root.class);

        rolUsuario = datosRoot.getDatosUsuario().getRol();

        //-------------------------------------------
        //Para el menu
        //Pegar esto en todas las clases de activity
        //Prepara el drawer para la elección de items
        //-------------------------------------------
        final DrawerLayout drawerLayout = findViewById(R.id.conocenos_drawerLayout);
        findViewById(R.id.conocenos_im_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });



        NavigationView navigationView = findViewById(R.id.conocenos_navigationView);
        navigationView.setItemIconTintList(null);
        if (sesionInicidad && cuerpo!=null){
            navigationView.getMenu().getItem(2).setVisible(false);
            if (!rolUsuario.equals("Admin")){
                navigationView.getMenu().getItem(6).setVisible(false);
            }
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
    }

    //-----------------------------------------------------------------------------
    // MENU
    //-----------------------------------------------------------------------------
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
            case R.id.menu_soporte_tecnico:
                lanzarSoporteTecnico();
                break;
            case R.id.menu_informacion:
                lanzarInformacion();
                break;
            case R.id.menu_signout:
                lanzarSignOut();
                break;
            case R.id.menu_nosotros:
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
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(ConoceTricoActivity.this);
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

        private void lanzarInformacion(){
            Intent i = new Intent(this, InformacionActivity.class);
            startActivity(i);
        }

        private void lanzarSoporteTecnico() {
            Intent i = new Intent(this, SoporteTecnicoActivity.class);
            startActivity(i);
        }

        private void lanzarGraficas(){
            Intent i = new Intent(this, GraficasActivity.class);
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

        private void lanzarMediciones(){
            Intent i = new Intent(this, MedicionesActivity.class);
            startActivity(i);
        }

}