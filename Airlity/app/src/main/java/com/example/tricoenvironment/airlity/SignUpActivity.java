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
import android.widget.CheckBox;
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

    private String datos;
    private String nombreUsuario, macSensorUsuario, tipoMedicion;
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
    private IntentFilter intentFilterLogin;
    private ReceptorGetUsuarioLogin receptorLogin;

    CheckBox cb_terminos;
    TextView tv_terminos;

    Bundle dato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        logicaFake = new LogicaFake();

        dato = getIntent().getExtras();
        datos = dato.getString("macUsuario");

        Gson gson = new Gson();

        DatosScanner dc = gson.fromJson(datos, DatosScanner.class);

        Log.d("Sensor", dc.getMacSensor()+"");
        macSensorUsuario = dc.getMacSensor();
        tipoMedicion = dc.getTipoMedicion();

        intentFilter = new IntentFilter();
        intentFilter.addAction("Get_usuario");
        receptor = new ReceptorGetUsuario();

        intentFilterLogin = new IntentFilter();
        intentFilterLogin.addAction("Get_usuario_login");
        receptorLogin = new ReceptorGetUsuarioLogin();



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

        navigationView.getMenu().getItem(1).setVisible(false);
        navigationView.getMenu().getItem(3).setVisible(false);
        navigationView.getMenu().getItem(4).setVisible(false);
        navigationView.getMenu().getItem(5).setVisible(false);
        navigationView.getMenu().getItem(6).setVisible(false);


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
        final Button btSignUp = (Button) findViewById(R.id.bt_signup_signup);
        final TextView tvErrorSignUp = findViewById(R.id.tv_error_signup);
        final EditText etTelefonoSignUp = findViewById(R.id.et_signup_telefono);
        cb_terminos = findViewById(R.id.cb_proteccion_datos);
        tv_terminos=findViewById(R.id.tv_terminos_condiciones);

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

        SpannableString mix = new SpannableString("términos y condiciones");
        mix.setSpan(new UnderlineSpan(), 0, mix.length(), 0);
        tv_terminos.setText(mix);

        tv_terminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, PopUpTerminos.class));
            }
        });

        tvLogearse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
            }
        });

        cb_terminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_terminos.isChecked()){
                    btSignUp.setBackgroundResource(R.drawable.boton_log_in);
                    btSignUp.setEnabled(true);
                }else{
                    btSignUp.setBackgroundResource(R.drawable.boton_deshabilitado);
                    btSignUp.setEnabled(false);
                }
            }
        });


        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ENTRA AL BOTON SIGNUP ONCLICK", "CLIIIIIIIIIIIIIIICK");
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
                    logicaFake.registrar(nombreUsuario, correoUsuario, contraseñaUsuario, telefonoUsuarioInt, macSensorUsuario, tipoMedicion, getApplicationContext());
                } else {
                    tvErrorSignUp.setVisibility(VISIBLE);
                    tvErrorSignUp.setText("Las contraseñas no coinciden");
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
        this.registerReceiver(receptorLogin, intentFilterLogin);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(receptorLogin);
        this.unregisterReceiver(receptor);
    }



    private class ReceptorGetUsuario extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            codigo = intent.getIntExtra("codigo_usuario", 0);
            TextView tvErrorSignUp = findViewById(R.id.tv_error_signup);
            cuerpo = intent.getStringExtra("cuerpo_usuario");

            Log.d("codigo3", codigo+", "+cuerpo);
            if (codigo == 200) {

                Log.d("REGISTRO", "Entra en REGISTRO");

                logicaFake.iniciarSesion(correoUsuario, contraseñaUsuario, getApplicationContext());

            } else if(codigo == 403){
                tvErrorSignUp.setVisibility(VISIBLE);
                tvErrorSignUp.setText("Sensor ya registrado");
            } else {
                tvErrorSignUp.setVisibility(VISIBLE);
                tvErrorSignUp.setText("Usuario ya registrado");
            }
        }
    }

    private class ReceptorGetUsuarioLogin extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            codigo = intent.getIntExtra("codigo_usuario_login", 0);

            cuerpo = intent.getStringExtra("cuerpo_usuario");
            if (codigo == 200) {

                Log.d("LOGIN", "Entra en LOGIN");
                Gson gson = new Gson();
                Log.d("CUERPO", cuerpo);
                Root datosRoot = gson.fromJson(cuerpo, Root.class);

                SharedPreferences sharedPreferences = getSharedPreferences("com.example.tricoenvironment.airlity", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //Guardamos toda la informacion del usuario descompuesta en campos
                editor.putBoolean("usuarioLogeado", true);
                editor.putString("tokken", datosRoot.getData().getToken());
                editor.putString("id", datosRoot.getDatosUsuario().getId());
                editor.putString("nombre", datosRoot.getDatosUsuario().getNombreUsuario());
                editor.putString("correo", datosRoot.getDatosUsuario().getCorreo());
                editor.putString("telefono", datosRoot.getDatosUsuario().getTelefono().toString());
                editor.putString("mac", datosRoot.getDatosUsuario().getMacSensor());
                editor.putString("rol", datosRoot.getDatosUsuario().getRol());
                editor.putString("contrrasenya", datosRoot.getDatosUsuario().getContrasenya());

                editor.commit();

                Intent i = new Intent(getApplicationContext(), MapaActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Toast.makeText(getApplicationContext(), "Bienvenido, "+ datosRoot.getDatosUsuario().getNombreUsuario(), Toast.LENGTH_LONG).show();
                startActivity(i);
            }
        }

    }
}