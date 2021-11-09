package com.example.tricoenvironment.airlity;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
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

import com.google.android.material.navigation.NavigationView;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

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
        final EditText etCorreoSignIn = findViewById(R.id.et_login_correo);
        final EditText etContrasenyaSignIn = findViewById(R.id.et_login_contrasenya);
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
            case R.id.menu_mediciones:
                lanzarMediciones();
                break;
            case R.id.menu_nosotros:
                lanzarContactanos();
                break;
        }

    }

    private void lanzarMapa(){

    }

    private void lanzarMediciones(){
        Intent i = new Intent(this, MedicionesActivity.class);
        startActivity(i);
    }

    private void lanzarContactanos(){

    }


}