package com.example.tricoenvironment.airlity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import static android.view.View.VISIBLE;

public class SignUpActivity extends AppCompatActivity {

    private String nombreUsuario, correoUsuario, contraseñaUsuario;
    private LogicaFake logicaFake;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        logicaFake = new LogicaFake();
        //-------------------------------------------
        //Para el menu
        //Pegar esto en todas las clases de activity
        //-------------------------------------------
        final DrawerLayout drawerLayout = findViewById(R.id.signup_drawerLayout);
        findViewById(R.id.signup_im_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.signup_navigationView);
        navigationView.setItemIconTintList(null);

        prepararDrawer(navigationView);
        //-------------------------------------------
        //-------------------------------------------

        //------------------------------------------------------------
        //------------------------------------------------------------
        //Conexión con elementos del layout
        //------------------------------------------------------------
        //------------------------------------------------------------
        final EditText etNombreSignUp = (EditText) findViewById(R.id.et_signup_nombre);
        final EditText etCorreoSignUp = (EditText) findViewById(R.id.et_signup_correo);
        final EditText etContrasenyaSignUp = (EditText) findViewById(R.id.et_signup_contrasenya);
        final EditText etVerificarContrasenyaSignUp = (EditText) findViewById(R.id.et_signup_verificar_contrasenya);
        final TextView tvLogearse = (TextView) findViewById(R.id.tv_signup_logearse_clickable);
        Button btSignUp = (Button) findViewById(R.id.bt_signup_signup);
        final TextView tvErrorSignUp = findViewById(R.id.tv_error_signup);

        //------------------------------------------------------------
        //------------------------------------------------------------
        //Limpiamos campos al iniciar layout
        //------------------------------------------------------------
        //------------------------------------------------------------
        etNombreSignUp.setText("");
        etCorreoSignUp.setText("");
        etContrasenyaSignUp.setText("");
        etVerificarContrasenyaSignUp.setText("");

        //------------------------------------------------------------
        //------------------------------------------------------------
        //Intent text view registrarse
        //------------------------------------------------------------
        //------------------------------------------------------------
        SpannableString mitextoU = new SpannableString("Iniciar sesión");
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        tvLogearse.setText(mitextoU);

        tvLogearse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "COntraseña: "+ etContrasenyaSignUp.getText()+"..", Toast.LENGTH_LONG).show();
                if(TextUtils.isEmpty(etNombreSignUp.getText().toString())
                        || TextUtils.isEmpty(etCorreoSignUp.getText().toString())
                        || TextUtils.isEmpty(etContrasenyaSignUp.getText().toString())
                        || TextUtils.isEmpty(etVerificarContrasenyaSignUp.getText().toString())){
                    tvErrorSignUp.setVisibility(VISIBLE);
                    tvErrorSignUp.setText("Rellene todos los campos");
                }
                else if (!etContrasenyaSignUp.getText().equals(etVerificarContrasenyaSignUp.getText())){
                    tvErrorSignUp.setVisibility(VISIBLE);
                    tvErrorSignUp.setText("Las contraseñas no coinciden");
                }
                else{
                    nombreUsuario = etNombreSignUp.getText().toString();
                    correoUsuario = etCorreoSignUp.getText().toString();
                    contraseñaUsuario = etContrasenyaSignUp.getText().toString();

                    logicaFake.registrarUsuario(nombreUsuario, correoUsuario, contraseñaUsuario);

                    Intent i = new Intent(getApplicationContext(), MedicionesActivity.class);
                    startActivity(i);
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

    private void lanzarInformacion() {
        Intent i = new Intent(this, InformacionActivity.class);
        startActivity(i);
    }

    private void lanzarGraficas() {
        Intent i = new Intent(this, GraficasActivity.class);
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


}