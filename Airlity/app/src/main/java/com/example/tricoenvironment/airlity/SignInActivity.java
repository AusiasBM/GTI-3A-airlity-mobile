/**
 * Adaptador.java
 * @fecha: 18/11/2021
 * @autor: Pere Márquez Barber
 *
 * @Descripcion:
 * Este fichero se encarga del layout SignIn, contiene los métodos para iniciar sesión y todo el front end de este
 */
package com.example.tricoenvironment.airlity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {

    String correoUsuario, contraseñaUsuario;
    EditText etCorreoSignIn, etContrasenyaSignIn;
    TextView tv_error;

    private int codigo;
    private String cuerpo;
    private LogicaFake logicaFake;
    private PeticionarioREST peticionarioREST;

    private IntentFilter intentFilter;
    private SignInActivity.ReceptorGetUsuario receptor;


    String idUsuarioDato, nombreUsuarioDato, correoUsusarioDato, contraseñaUsuarioDato, tokkenUsuarioDato;
    int telefonoUsuarioDato;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        logicaFake = new LogicaFake();
        intentFilter = new IntentFilter();
        intentFilter.addAction("Get_usuario_login");
        receptor = new SignInActivity.ReceptorGetUsuario();
        registerReceiver(receptor, intentFilter);
        //-------------------------------------------
        //Para el menu
        //Pegar esto en todas las clases de activity
        //-------------------------------------------
        final DrawerLayout drawerLayout = findViewById(R.id.signin_drawerLayout);
        tv_error = findViewById(R.id.tv_error_login);
        findViewById(R.id.signin_im_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        NavigationView navigationView = findViewById(R.id.signin_navigationView);
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
                startActivity(new Intent(SignInActivity.this, PopUpRegistro.class));
            }
        });

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correoUsuario = etCorreoSignIn.getText().toString();
                contraseñaUsuario = etContrasenyaSignIn.getText().toString();
                if(TextUtils.isEmpty(etCorreoSignIn.getText().toString())
                        || TextUtils.isEmpty(etContrasenyaSignIn.getText().toString())){
                    tvErrorSignIn.setVisibility(VISIBLE);
                    tvErrorSignIn.setText("Rellene todos los campos");
                } else{
                    logicaFake.iniciarSesion(correoUsuario, contraseñaUsuario, getApplicationContext());
                    SharedPreferences sharedPreferences = getSharedPreferences("com.example.tricoenvironment.airlity", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("nombreUsuario", "");
                    editor.putInt("telefonoUsuario", 10);
                    editor.putBoolean("sesionIniciada", true);

                    Log.d("USUARIO", correoUsuario);
                    Log.d("USUARIO", contraseñaUsuario);
                    //Intent i = new Intent(getApplicationContext(), MedicionesActivity.class);
                    //startActivity(i);
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
                lanzarMapa();
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
        Intent i = new Intent(this, SoporteTecnicoActivity.class);
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

    private void lanzarMediciones(){
        Intent i = new Intent(this, MedicionesActivity.class);
        startActivity(i);
    }

    private void lanzarContactanos(){
        Intent i = new Intent(this, ConoceTricoActivity.class);
        startActivity(i);
    }


    private class ReceptorGetUsuario extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            codigo = intent.getIntExtra("codigo_usuario_login", 0);

            cuerpo = intent.getStringExtra("cuerpo_usuario");
            if (codigo == 200) {
                Gson gson = new Gson();
                Root datosRoot = gson.fromJson(cuerpo, Root.class);

                tokkenUsuarioDato = datosRoot.getData().getToken();
                idUsuarioDato = datosRoot.getDatosUsuario().getId();
                nombreUsuarioDato = datosRoot.getDatosUsuario().getNombreUsuario();
                correoUsusarioDato = datosRoot.getDatosUsuario().getCorreo();
                telefonoUsuarioDato = datosRoot.getDatosUsuario().getTelefono();
                contraseñaUsuarioDato = datosRoot.getDatosUsuario().getContrasenya();

                Intent i = new Intent(getApplicationContext(), MapaActivity.class);
                //i.setAction("DatosUsuario");
                i.putExtra("sesionIniciada", true);
                i.putExtra("tokkenUsuario", tokkenUsuarioDato);
                i.putExtra("idUsuario", idUsuarioDato);
                i.putExtra("nombrUsuario", nombreUsuarioDato);
                i.putExtra("correoUsuario", correoUsusarioDato);
                i.putExtra("telefonoUsuario", telefonoUsuarioDato);
                i.putExtra("contraseñaUsuario", contraseñaUsuarioDato);

                SharedPreferences sharedPreferences = getSharedPreferences("com.example.tricoenvironment.airlity", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("usuarioLogeado", true);
                editor.putString("tokkenUsuario", tokkenUsuarioDato);
                editor.putString("idUsuario", idUsuarioDato );
                editor.commit();

                Log.d("DATOS", idUsuarioDato+", "+nombreUsuarioDato+", "+correoUsusarioDato
                        + ", "+telefonoUsuarioDato+", "+contraseñaUsuarioDato+", "+tokkenUsuarioDato);
                Toast.makeText(getApplicationContext(), "Bienvenido, "+ nombreUsuarioDato, Toast.LENGTH_LONG).show();

                /*
                SharedPreferences sharedPreferences = getSharedPreferences("com.example.tricoenvironment.airlity", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("sesionIniciada", true);
                editor.commit();
                 */
                startActivity(i);
            }else{
                tv_error.setVisibility(VISIBLE);
                tv_error.setText("Correo y/o contraseña incorrecto");
            }
        }

    }



}