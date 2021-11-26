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

/**
 * InformaciónActivity.
 * Página que muestra el layout de información
 */

public class InformacionActivity extends AppCompatActivity {

    boolean usuarioRegistrado, usuario;
    String idUsuarioDato, nombreUsuarioDato, correoUsuarioDato, contraseñaUsuarioDato, tokkenUsuarioDato, telefonoUsuarioDato;

    Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

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
        }

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.tricoenvironment.airlity", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (usuarioRegistrado){
            editor.putBoolean("usuarioLogeado", true);
        }else{
            editor.putBoolean("usuarioLogeado", false);
        }
        editor.commit();

        //-------------------------------------------
        //Para el menu
        //Pegar esto en todas las clases de activity
        //-------------------------------------------


        SharedPreferences preferences=getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);
        usuario = preferences.getBoolean("usuarioLogeado", false);

        final DrawerLayout drawerLayout = findViewById(R.id.informacion_drawerLayout);
        findViewById(R.id.informacion_im_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.informacion_navigationView);
        navigationView.setItemIconTintList(null);
        if (usuario){
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
            case R.id.menu_signout:
                lanzarSignOut();
                break;
            case R.id.menu_soporte_tecnico:
                lanzarSoporteTecnico();
                break;
            case R.id.menu_nosotros:
                lanzarContactanos();
                break;
        }

    }
    private void lanzarSignOut() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(InformacionActivity.this);
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

    private void lanzarSignIn() {
        Intent i = new Intent(this, SignInActivity.class);
        i.putExtra("sesionIniciada", usuario);
        i.putExtra("tokkenUsuario", tokkenUsuarioDato);
        i.putExtra("idUsuario", idUsuarioDato);
        i.putExtra("nombrUsuario", nombreUsuarioDato);
        i.putExtra("correoUsuario", correoUsuarioDato);
        i.putExtra("telefonoUsuario", telefonoUsuarioDato);
        i.putExtra("contraseñaUsuario", contraseñaUsuarioDato);
        startActivity(i);
    }

    private void lanzarSoporteTecnico() {
        Intent i = new Intent(this, SoporteTecnicoActivity.class);
        i.putExtra("tokkenUsuario", tokkenUsuarioDato);
        i.putExtra("idUsuario", idUsuarioDato);
        i.putExtra("nombrUsuario", nombreUsuarioDato);
        i.putExtra("correoUsuario", correoUsuarioDato);
        i.putExtra("telefonoUsuario", telefonoUsuarioDato);
        i.putExtra("contraseñaUsuario", contraseñaUsuarioDato);
        startActivity(i);
    }

    private void lanzarMapa(){
        Intent i = new Intent(this, MapaActivity.class);
        i.putExtra("sesionIniciada", usuario);
        i.putExtra("tokkenUsuario", tokkenUsuarioDato);
        i.putExtra("idUsuario", idUsuarioDato);
        i.putExtra("nombrUsuario", nombreUsuarioDato);
        i.putExtra("correoUsuario", correoUsuarioDato);
        i.putExtra("telefonoUsuario", telefonoUsuarioDato);
        i.putExtra("contraseñaUsuario", contraseñaUsuarioDato);
        startActivity(i);
    }

    private void lanzarPerfilUsuario(){
        Intent i = new Intent(this, PerfilUsuario.class);
        i.putExtra("sesionIniciada", usuario);
        i.putExtra("tokkenUsuario", tokkenUsuarioDato);
        i.putExtra("idUsuario", idUsuarioDato);
        i.putExtra("nombrUsuario", nombreUsuarioDato);
        i.putExtra("correoUsuario", correoUsuarioDato);
        i.putExtra("telefonoUsuario", telefonoUsuarioDato);
        i.putExtra("contraseñaUsuario", contraseñaUsuarioDato);
        startActivity(i);
    }

    private void lanzarGraficas(){
        Intent i = new Intent(this, GraficasActivity.class);
        i.putExtra("sesionIniciada", usuario);
        i.putExtra("tokkenUsuario", tokkenUsuarioDato);
        i.putExtra("idUsuario", idUsuarioDato);
        i.putExtra("nombrUsuario", nombreUsuarioDato);
        i.putExtra("correoUsuario", correoUsuarioDato);
        i.putExtra("telefonoUsuario", telefonoUsuarioDato);
        i.putExtra("contraseñaUsuario", contraseñaUsuarioDato);
        startActivity(i);
    }

    private void lanzarMediciones(){
        Intent i = new Intent(this, MedicionesActivity.class);
        i.putExtra("sesionIniciada", usuario);
        i.putExtra("tokkenUsuario", tokkenUsuarioDato);
        i.putExtra("idUsuario", idUsuarioDato);
        i.putExtra("nombrUsuario", nombreUsuarioDato);
        i.putExtra("correoUsuario", correoUsuarioDato);
        i.putExtra("telefonoUsuario", telefonoUsuarioDato);
        i.putExtra("contraseñaUsuario", contraseñaUsuarioDato);
        startActivity(i);
    }

    private void lanzarContactanos(){
        Intent i =new Intent(this, ConoceTricoActivity.class);
        i.putExtra("tokkenUsuario", tokkenUsuarioDato);
        i.putExtra("idUsuario", idUsuarioDato);
        i.putExtra("nombrUsuario", nombreUsuarioDato);
        i.putExtra("correoUsuario", correoUsuarioDato);
        i.putExtra("telefonoUsuario", telefonoUsuarioDato);
        i.putExtra("contraseñaUsuario", contraseñaUsuarioDato);
        startActivity(i);
    }
}