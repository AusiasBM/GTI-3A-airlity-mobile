package com.example.tricoenvironment.airlity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class PerfilUsuario extends AppCompatActivity {

    private TextView tv_nombreUsuario;
    private EditText et_nombreUsuario, et_apellidoUsuario, et_correoElectronico, et_telefonoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        cargarPreferencias();
        //------------------------------------------------------------
        //------------------------------------------------------------
        //Conexión con elementos del layout
        //------------------------------------------------------------
        //------------------------------------------------------------
        tv_nombreUsuario = findViewById(R.id.tv_nombreUsuario_perfilUsuario);
        et_nombreUsuario = findViewById(R.id.et_nombreUsuario_perfilUsuario);
        et_apellidoUsuario = findViewById(R.id.et_apellidosUsuario_perfilUsuario);
        et_correoElectronico = findViewById(R.id.et_correoUsuario_perfilUsuario);
        et_telefonoUsuario =findViewById(R.id.et_telefonoUsuario_perfilUsuario);
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

    private void lanzarSoporteTecnico() {
    }

    private void lanzarMapa(){

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

    private void cargarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("Credenciales", Context.MODE_PRIVATE);

        String correoUsuario = preferences.getString("correoUsuario", "HOLA");
        String contraseñaUsuario = preferences.getString("contraseñaUsuario", "HOLA");

        Log.d("HOLA", correoUsuario+"");
        //tv_nombreUsuario.setText(correoUsuario+"");
    }
}