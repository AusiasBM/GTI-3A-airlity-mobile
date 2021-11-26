/**
 * GraficasActivity.java
 * @fecha: 21/11/2021
 * @autor: Pere Márquez Barber
 *
 * @Descripcion:
 * Este fichero se encarga del layout Gráficas
 * Muestra una gráfica con las últimas mediciones del sensor e información sobre este
 */
package com.example.tricoenvironment.airlity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.lang.System.currentTimeMillis;

/**
 * Activity Graficas
 *
 * Clase que carga el contenido del layout de gráficas
 * Añade datos a la gráfica
 */
public class GraficasActivity extends AppCompatActivity {

    private BarChart barChart;
    boolean usuarioRegistrado;
    Bundle datos;
    private IntentFilter intentFilter;
    private ReceptorDatos receptor;
    private TextView fechaDia, valorMedio, valorMax, tiempoMidiendo, calidadAire;
    private long medianoche;
    private String gas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficas);

        usuarioRegistrado = datos.getBoolean("sesionIniciada");
        datos = getIntent().getExtras();
        graficaTemps = (BarChart) findViewById(R.id.graficaTemperatura);
        createCharts();
        barChart = findViewById(R.id.chart);
        fechaDia = findViewById(R.id.fechaDia);
        valorMedio = findViewById(R.id.valorMedio);
        valorMax = findViewById(R.id.valorMax);
        tiempoMidiendo = findViewById(R.id.tiempoMidiendo);
        calidadAire = findViewById(R.id.calidadAire);


        //-------------------------------------------
        //Carga datos usuario
        //-------------------------------------------
        cargarPreferencias();
        //-------------------------------------------
        //Para el menu
        //Pegar esto en todas las clases de activity
        //Prepara el drawer para la elección de items
        //-------------------------------------------
        final DrawerLayout drawerLayout = findViewById(R.id.graficas_drawerLayout);
        findViewById(R.id.graficas_im_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.graficas_navigationView);
        navigationView.setItemIconTintList(null);
        if (usuarioRegistrado){
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

        //Preparamos el receptor de anuncios
        intentFilter = new IntentFilter();
        intentFilter.addAction("DatosEstadisticos");
        receptor = new ReceptorDatos();

        //Obtenemos la hora 00:00 del dia actual para saber las estadísticas de hoy (desde las 12 de la noche hasta la hora actual)
        medianoche = fechaHoyMedianoche();

        //Llamamos a los métodos de la lógica fake obtenerEstadisticas y obtenerDatosParaGrafico
        // para mostrar los datos
        LogicaFake.obtenerEstadisticas(GraficasActivity.this, currentTimeMillis()-15708000, currentTimeMillis()- 15701000);
        LogicaFake.obtenerDatosParaGrafico(GraficasActivity.this, medianoche, currentTimeMillis());
    }

    @Override
    public void onResume() {
        super.onResume();
        this.registerReceiver(receptor, intentFilter);
    }


    /**
     * fechaHoyMedianoche()
     * Descripción:
     * Método para sacar la fecha en milisegundos del dia a las 00:00
     *
     * @return long con la fecha en milisegundos de ese dia a las 00:00
     */
    private long fechaHoyMedianoche(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date date =  c.getTime();
        return date.getTime();
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
                break;
            case R.id.menu_soporte_tecnico:
                lanzarSoporteTecnico();
                break;
            case R.id.menu_nosotros:
                lanzarContactanos();
                break;
        }

    }

    private void lanzarSoporteTecnico() {
    }

    private void lanzarMapa(){
        Intent i = new Intent(this, MapaActivity.class);
        i.putExtra("sesionIniciada", true);
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

    private void lanzarContactanos() {

    }
    private void cargarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);

        String nombreUsuario = preferences.getString("nombreUsuario", "Sesion no iniciada todavia");
        String correoUsuario = preferences.getString("correoUsuario", "Sesion no iniciada todavia");
        String apellidoUsuario = preferences.getString("apellidoUsuario", "");

        String contraseñaUsuario = preferences.getString("contraseñaUsuario", "Sesion no iniciada todavia");
        int telefonoUsuario = preferences.getInt("telefonoUsuario", 00000);
        usuarioRegistrado = preferences.getBoolean("sesionIniciada", false);

    }

    //--------------------------------------------------------------------------------
    //Receptor de anuncios de los métodos obtenerEstadisticas y obtenerDatosParaGrafico de la clase LógicaFake
    //--------------------------------------------------------------------------------

    /**
     * Clase ReceptorDatos
     */
    private class ReceptorDatos extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

            /**
             * Esperamos a la respuesta desde el servidor con el resultado de las estadisticas y datos
             * obtenidos para generar la gráfica
             */
            String estadisticas = intent.getStringExtra("Estadisticas");
            String datosGrafica = intent.getStringExtra("DatosGrafica");

            if(estadisticas != null){
                //Convertimos los datos estadísticos de JSON a objeto de tipo EstadisticasMediciones
                Gson gson = new Gson();
                EstadisticasMediciones estadisticasMediciones = gson.fromJson(estadisticas, EstadisticasMediciones.class);

                //Rellenamos los campos con los datos del objeto obtenido
                fechaDia.setText("Fecha: " +  new SimpleDateFormat("dd-MM-yyyy").format(currentTimeMillis()));
                valorMedio.setText(estadisticasMediciones.getMedia() +"");
                valorMax.setText(estadisticasMediciones.getValorMaximo()+"");
                calidadAire.setText(estadisticasMediciones.getValoracionCalidadAire());

                gas = estadisticasMediciones.getTipoGas();

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                tiempoMidiendo.setText(sdf.format(estadisticasMediciones.getTiempo()));

            }

            if(datosGrafica != null){
                //Convertimos los datos para la gráfica de JSON a objeto de tipo DatosGrafica
                Gson gson = new Gson();
                DatosGrafica datos = gson.fromJson(datosGrafica, DatosGrafica.class);

                //Creamos el objeto de tipo GeneradorGrafica y generamos la gráfica con los datos almacenados en el objeto anterior
                GeneradorGrafica generadorGrafica = new GeneradorGrafica(datos.getMedias(), datos.getFechas(), barChart, gas);
                generadorGrafica.createChart();

            }


        }
    }

}