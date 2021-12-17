package com.example.tricoenvironment.airlity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SensoresInactivosActivity extends AppCompatActivity {

    private LogicaFake logicaFake;
    String cuerpo, cuerpoSensoresErroneo, tokkenUsuarioDato;
    Boolean sesionInicidad;
    private RecyclerView recyclerView, recyclerViewErroneos;
    private Context context;
    private AdaptadorSensoresInactivos adaptador;
    private AdaptadorSensoresErroneos adaptadorSensoresErroneos;
    private static ProgressDialog progressDialog;
    private ArrayList<Sensor> listaSensoresInactivos;
    private ArrayList<Sensor> listaSensoresErroneos;

    private IntentFilter intentFilter;
    private ReceptoGetSensores receptor;
    LinearLayout l_inactivos;
    ImageView iv_abrir_inactivos, iv_abrir_erroneos;

    ConstraintLayout cl_sensores_errorneos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores_inactivos);

        l_inactivos=findViewById(R.id.l_inactivos);
        iv_abrir_inactivos=findViewById(R.id.iv_abrir_inactivos);

        logicaFake = new LogicaFake();
        listaSensoresInactivos = new ArrayList<>();

        intentFilter = new IntentFilter();
        intentFilter.addAction("SensoresInactivos");

        receptor = new ReceptoGetSensores();
        registerReceiver(receptor,intentFilter);
        recyclerView = this.findViewById(R.id.recView);

        SharedPreferences preferences=getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);
        sesionInicidad = preferences.getBoolean("usuarioLogeado", false);
        cuerpo = preferences.getString("cuerpoUsuario", null);
        Gson gson = new Gson();


        Root datosRoot = gson.fromJson(cuerpo, Root.class);
        tokkenUsuarioDato = datosRoot.getData().getToken();

        logicaFake.sensoresInactivos(tokkenUsuarioDato, this);
        //logicaFake.sensoresErroneos("Gandia", "SO2", this);

        iv_abrir_inactivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l_inactivos.getVisibility()==VISIBLE){
                    l_inactivos.setVisibility(GONE);
                    iv_abrir_inactivos.setImageResource(R.drawable.flecha_abajo);
                } else{
                    l_inactivos.setVisibility(VISIBLE);
                    iv_abrir_inactivos.setImageResource(R.drawable.flecha_abajo);
                }

            }
        });

        //-------------------------------------------
        //-------------------------------------------

        //-------------------------------------------
        //Para el menu
        //Pegar esto en todas las clases de activity
        //-------------------------------------------
        final DrawerLayout drawerLayout = findViewById(R.id.sensores_drawerLayout);
        findViewById(R.id.sensores_im_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.sensores_navigationView);
        navigationView.setItemIconTintList(null);


        navigationView.getMenu().getItem(2).setVisible(false);

        prepararDrawer(navigationView);
        //-------------------------------------------
        //-------------------------------------------

        progressDialog = new ProgressDialog(SensoresInactivosActivity.this);
        progressDialog.setMessage("Obteniendo información de los sensores");
        progressDialog.show();
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

    private void lanzarSensores() {
        Intent i = new Intent(this, SensoresInactivosActivity.class);
        startActivity(i);
    }

    private void lanzarSignOut() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(SensoresInactivosActivity.this);
        alertDialog.setMessage("¿Segur que desea cerrar sesión?").setCancelable(false)
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

    private void lanzarGraficas() {
        Intent i = new Intent(this, GraficasActivity.class);
        startActivity(i);
    }

    private void lanzarInformacion() {
        Intent i = new Intent(this, InformacionActivity.class);
        startActivity(i);
    }

    private void lanzarMediciones(){
        Intent i = new Intent(this, MedicionesActivity.class);
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

    private void lanzarSignIn(){
        Intent i = new Intent(this, SignInActivity.class);
        startActivity(i);
    }

    private void lanzarContactanos(){
        Intent i = new Intent(this, ConoceTricoActivity.class);
        startActivity(i);
    }

    private class ReceptoGetSensores extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int codigo = intent.getIntExtra("codigoSensor", 0);

            cuerpo = intent.getStringExtra("cuerpoSensor");
            if (codigo == 200) {
                Gson gson = new Gson();
                listaSensoresInactivos = gson.fromJson(cuerpo, new TypeToken<List<Sensor>>(){}.getType());

                recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                adaptador = new AdaptadorSensoresInactivos(listaSensoresInactivos);
                recyclerView.setAdapter(adaptador);
            } else {
                Toast.makeText(SensoresInactivosActivity.this, "Fallo al recibir sensores", Toast.LENGTH_LONG).show();
            }

            /*
            int codigoSensoresErroneos=intent.getIntExtra("codigoSensorErroneo", 0);

            cuerpoSensoresErroneo =intent.getStringExtra("cuerpoSensorErroneo");
            if (codigoSensoresErroneos == 200){
                Gson gson = new Gson();
                //listaSensoresErroneos = gson.fromJson(cuerpoSensoresErroneo, );

                //Log.d("Sensores_erroneos", listaSensoresErroneos.toString());
                //recyclerViewErroneos.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                //adaptadorSensoresErroneos = new AdaptadorSensoresErroneos(listaSensoresErroneos);
                //recyclerViewErroneos.setAdapter(adaptadorSensoresErroneos);
            }*/

            progressDialog.dismiss();
        }
    }
}