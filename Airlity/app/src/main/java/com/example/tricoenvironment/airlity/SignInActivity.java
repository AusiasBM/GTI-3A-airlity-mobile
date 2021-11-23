/**
 * Adaptador.java
 * @fecha: 18/11/2021
 * @autor: Pere Márquez Barber
 *
 * @Descripcion:
 * Este fichero se encarga del layout SignIn, contiene los métodos para iniciar sesión y todo el front end de este
 */
package com.example.tricoenvironment.airlity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import static android.view.View.VISIBLE;

public class SignInActivity extends AppCompatActivity {

    private String correoUsuario, contraseñaUsuario;
    private LogicaFake logicaFake;
    EditText etCorreoSignIn, etContrasenyaSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        logicaFake = new LogicaFake();
        //-------------------------------------------
        //Para el menu
        //Pegar esto en todas las clases de activity
        //-------------------------------------------
        final DrawerLayout drawerLayout = findViewById(R.id.signin_drawerLayout);
        findViewById(R.id.signin_im_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.signin_navigationView);
        navigationView.setItemIconTintList(null);

        prepararDrawer(navigationView);
        //-------------------------------------------
        //-------------------------------------------

        //------------------------------------------------------------
        //------------------------------------------------------------
        //Conexión con elementos del layout
        //------------------------------------------------------------
        //------------------------------------------------------------
        etCorreoSignIn = findViewById(R.id.et_login_correo);
        etContrasenyaSignIn = findViewById(R.id.et_login_contrasenya);
        final Button cambiarVisibilidadContrasenya = findViewById(R.id.bt_login_contrasenya_cambio);
        Button btSignIn = findViewById(R.id.bt_login_login);
        final TextView tvRegistrarse = findViewById(R.id.tv_login_registrarse_clickable);
        final TextView tvErrorSignIn = findViewById(R.id.tv_error_login);
        //------------------------------------------------------------
        //------------------------------------------------------------
        //Limpiamos campos al iniciar layout
        //------------------------------------------------------------
        //------------------------------------------------------------
        etCorreoSignIn.setText("");
        etContrasenyaSignIn.setText("");

        //------------------------------------------------------------
        //------------------------------------------------------------
        //Intent text view registrarse
        //------------------------------------------------------------
        //------------------------------------------------------------
        SpannableString mitextoU = new SpannableString("Registrese");
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        tvRegistrarse.setText(mitextoU);

        tvRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
            }
        });

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etCorreoSignIn.getText().toString())
                        || TextUtils.isEmpty(etContrasenyaSignIn.getText().toString())){
                    tvErrorSignIn.setVisibility(VISIBLE);
                    tvErrorSignIn.setText("Rellene todos los campos");
                } else{
                    if(logicaFake.iniciarSesion(correoUsuario, contraseñaUsuario)){
                        guardarPreferencias();
                        Intent i = new Intent(getApplicationContext(), MedicionesActivity.class);
                        startActivity(i);

                    }
                }
            }
        });

        cambiarVisibilidadContrasenya.setOnClickListener(new View.OnClickListener() {
            boolean isVisible=false;
            @Override
            public void onClick(View v) {
                if(!isVisible){
                    etContrasenyaSignIn.setTransformationMethod(new PasswordTransformationMethod());
                    isVisible=true;
                    cambiarVisibilidadContrasenya.setBackgroundResource(R.drawable.oculto);
                }else{
                    etContrasenyaSignIn.setTransformationMethod(null);
                    isVisible=false;
                    cambiarVisibilidadContrasenya.setBackgroundResource(R.drawable.ojo);
                }

            }
        });
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

    private void lanzarMapa(){
        Intent i = new Intent(this, MapaActivity.class);
        startActivity(i);
    }

    private void lanzarPerfilUsuario(){
        Intent i = new Intent(this, PerfilUsuario.class);
        startActivity(i);
    }

    private void lanzarMediciones(){
        Intent i = new Intent(this, MedicionesActivity.class);
        startActivity(i);
    }

    private void lanzarContactanos(){

    }

    private void guardarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("Credenciales", Context.MODE_PRIVATE);

        String correoUsuario = etCorreoSignIn.getText().toString();
        String contraseñaUsuario = etContrasenyaSignIn.getText().toString();

        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("correoUsuario", correoUsuario);
        editor.putString("contraseñUsuario", contraseñaUsuario);

        editor.commit();
    }

}