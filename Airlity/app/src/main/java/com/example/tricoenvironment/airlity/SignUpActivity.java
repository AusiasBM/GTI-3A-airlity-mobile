package com.example.tricoenvironment.airlity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
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
import com.google.gson.Gson;

import static android.view.View.VISIBLE;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    private String nombreUsuario, macSensorUsuario;
    private String correoUsuario;
    private String contraseñaUsuario;
    private String contraseñaVerificada;
    private String telefonoUsuario;
    private int codigo;
    private String cuerpo;
    private LogicaFake logicaFake;
    private PeticionarioREST peticionarioREST;

    private IntentFilter intentFilter;
    private ReceptorGetUsuario receptor;

    Bundle dato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        logicaFake = new LogicaFake();

        dato = getIntent().getExtras();
        macSensorUsuario = dato.getString("macUsuario");

        Log.d("Sensor", macSensorUsuario+"");

        intentFilter = new IntentFilter();
        intentFilter.addAction("Get_usuario");
        receptor = new ReceptorGetUsuario();
        registerReceiver(receptor, intentFilter);


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

        navigationView.getMenu().getItem(3).setVisible(false);
        navigationView.getMenu().getItem(4).setVisible(false);
        navigationView.getMenu().getItem(5).setVisible(false);
        
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
        final EditText etTelefonoSignUp = findViewById(R.id.et_signup_telefono);

        //------------------------------------------------------------
        //------------------------------------------------------------
        //Limpiamos campos al iniciar layout
        //------------------------------------------------------------
        //------------------------------------------------------------
        etNombreSignUp.setText("");
        etCorreoSignUp.setText("");
        etContrasenyaSignUp.setText("");
        etVerificarContrasenyaSignUp.setText("");
        etTelefonoSignUp.setText("");

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
                nombreUsuario = etNombreSignUp.getText().toString();
                correoUsuario = etCorreoSignUp.getText().toString();
                contraseñaUsuario = etContrasenyaSignUp.getText().toString();
                contraseñaVerificada = etVerificarContrasenyaSignUp.getText().toString();
                telefonoUsuario = etTelefonoSignUp.getText().toString();
                int telefonoUsuarioInt = Integer.parseInt(telefonoUsuario);
                if (TextUtils.isEmpty(etNombreSignUp.getText().toString())
                        || TextUtils.isEmpty(etCorreoSignUp.getText().toString())
                        || TextUtils.isEmpty(etTelefonoSignUp.getText().toString())
                        || TextUtils.isEmpty(etContrasenyaSignUp.getText().toString())
                        || TextUtils.isEmpty(etVerificarContrasenyaSignUp.getText().toString())) {
                    tvErrorSignUp.setVisibility(VISIBLE);
                    tvErrorSignUp.setText("Rellene todos los campos");
                } else if (contraseñaUsuario.equals(contraseñaVerificada)) {
                    logicaFake.registrarUsuario(nombreUsuario, correoUsuario, contraseñaUsuario, telefonoUsuarioInt, macSensorUsuario,getApplicationContext());
                    SharedPreferences sharedPreferences = getSharedPreferences("com.example.tricoenvironment.airlity", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("usuarioLogeado", true);
                    editor.commit();
                } else {
                    tvErrorSignUp.setVisibility(VISIBLE);
                    tvErrorSignUp.setText("Las contraseñas no coinciden");
                }

            }
        });
    }
    /*
    private void guardarPreferencias(String nombreUsuario, String correoUsuario, String contraseñaUsuario, String apellidoUsuario, int telefonoUsuario, boolean sesionUsuario) {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.tricoenvironment.airlity", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nombreUsuario", nombreUsuario);
        editor.putString("correoUsuario", correoUsuario);
        editor.putString("contraseñaUsuario", contraseñaUsuario);
        editor.putInt("telefonoUsuario", telefonoUsuario);
        editor.putBoolean("sesionIniciada", sesionUsuario);

        editor.commit();
    }
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
        Intent i = new Intent(this, SoporteTecnicoActivity.class);
        startActivity(i);
    }

    private void lanzarMapa() {
        Intent i = new Intent(this, MapaActivity.class);
        startActivity(i);
    }

    private void lanzarPerfilUsuario() {
        Intent i = new Intent(this, PerfilUsuario.class);
        startActivity(i);
    }

    private void lanzarSignIn() {
        Intent i = new Intent(this, SignInActivity.class);
        startActivity(i);
    }

    private void lanzarMediciones() {
        Intent i = new Intent(this, MedicionesActivity.class);
        startActivity(i);
    }

    private void lanzarContactanos() {
        Intent i = new Intent(this, ConoceTricoActivity.class);
        startActivity(i);

    }
    @Override
    public void onResume() {
        super.onResume();
        this.registerReceiver(receptor, intentFilter);
    }


    private class ReceptorGetUsuario extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            codigo = intent.getIntExtra("codigo_usuario", 0);
            TextView tvErrorSignUp = findViewById(R.id.tv_error_signup);
            //cuerpo = intent.getStringExtra("cuerpo_usuario");
            cuerpo = intent.getStringExtra("cuerpo_usuario");

            Log.d("codigo3", codigo+", "+cuerpo);
            if (codigo == 200) {
                logicaFake.iniciarSesion(correoUsuario, contraseñaUsuario, getApplicationContext());
                SharedPreferences sharedPreferences = getSharedPreferences("com.example.tricoenvironment.airlity", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("usuarioLogeado", true);
                editor.putString("cuerpoUsuario", cuerpo);
                editor.commit();
                Toast.makeText(getApplicationContext(), "Usuario registrado, por favor ve a perfil para añadir sus credenciales", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), MapaActivity.class);
                startActivity(i);
            } else if(codigo == 403){
                tvErrorSignUp.setVisibility(VISIBLE);
                tvErrorSignUp.setText("Sensor ya registrado");
            } else {
                tvErrorSignUp.setVisibility(VISIBLE);
                tvErrorSignUp.setText("Usuario ya registrado");
            }
        }

    }
}