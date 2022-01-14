package com.example.tricoenvironment.airlity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Mapa_interpolacion extends AppCompatActivity {

    boolean usuarioRegistrado, usuario;
    String idUsuarioDato, nombreUsuarioDato, correoUsuarioDato, contraseñaUsuarioDato, tokkenUsuarioDato, telefonoUsuarioDato, macUsuarioDato;

    Bundle datos;
    Boolean sesionIniciada;
    String rolUsuario;
    String url;
    RadioGroup rg_mapaInterpolacion;
    RadioButton rb_Iaq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_interpolacion);
        rg_mapaInterpolacion=findViewById(R.id.rg_mapaInterpolacion);
        final WebView myWebViewIAQ = (WebView) findViewById(R.id.webviewIAQ);
        rb_Iaq=findViewById(R.id.rb_Iaq);
        rb_Iaq.setChecked(true);
        myWebViewIAQ.setWebViewClient(new WebViewClient());
        myWebViewIAQ.setWebChromeClient(new WebChromeClient());
        myWebViewIAQ.loadUrl("http://217.76.155.97:5080/mapaIAQ.html");
        WebSettings webSettings = myWebViewIAQ.getSettings();
        webSettings.setJavaScriptEnabled(true);



        SharedPreferences preferences = getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);
        sesionIniciada = preferences.getBoolean("usuarioLogeado", false);
        rolUsuario = preferences.getString("rol", "");

        //-------------------------------------------
        //Para el menu
        //Pegar esto en todas las clases de activity
        //Prepara el drawer para la elección de items
        //-------------------------------------------
        final DrawerLayout drawerLayout = findViewById(R.id.mapaInterpolacion_drawerLayout);
        findViewById(R.id.mapaInterpolacion_im_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });



        NavigationView navigationView = findViewById(R.id.mapaInterpolacion_navigationView);
        navigationView.setItemIconTintList(null);
        if (sesionIniciada){
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


        rg_mapaInterpolacion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rg_mapaInterpolacion.getCheckedRadioButtonId()==R.id.rb_Iaq){
                    myWebViewIAQ.setWebViewClient(new WebViewClient());
                    myWebViewIAQ.setWebChromeClient(new WebChromeClient());
                    myWebViewIAQ.loadUrl("http://217.76.155.97:5080/mapaIAQ.html");
                    WebSettings webSettings = myWebViewIAQ.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                }else{
                    myWebViewIAQ.setWebViewClient(new WebViewClient());
                    myWebViewIAQ.setWebChromeClient(new WebChromeClient());
                    myWebViewIAQ.loadUrl("http://217.76.155.97:5080/mapaO3.html");
                    WebSettings webSettings = myWebViewIAQ.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                }
            }
        });

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
                lanzarContactanos();
                break;
            case R.id.menu_sensores:
                lanzarSensores();
                break;
            case R.id.menu_mapaInterpolacion:
                break;
        }

    }

    private void lanzarContactanos() {
        Intent i = new Intent(this, ConoceTricoActivity.class);
        startActivity(i);
    }

    private void lanzarSensores() {
        Intent i = new Intent(this, SensoresInactivosActivity.class);
        startActivity(i);
    }

    private void lanzarSignOut() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Mapa_interpolacion.this);
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

