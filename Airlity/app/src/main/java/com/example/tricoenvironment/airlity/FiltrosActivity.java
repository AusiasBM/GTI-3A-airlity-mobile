package com.example.tricoenvironment.airlity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FiltrosActivity extends AppCompatActivity {

    Button bt_aplicarFiltros, bt_borrarFiltros;
    RadioGroup radioGroup, rg_tipo;
    RadioButton radioTodas;
    RadioButton radioMias;
    CalendarView cv_inicio, cv_fin;
    Boolean fechaInicioSeleccionada = false;
    Boolean fechaFinSeleccionada = false;
    ImageView iv_abrir_autor, iv_abrir_fechas, iv_abrir_tipo_mediciones, iv_abrir_estaciones, iv_volver;
    TextView tv_fechaIni, tv_fechaFin;
    RadioButton rb_o3, rb_no2, rb_co, rb_so2, rb_iaq;
    Switch switch_estaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        bt_aplicarFiltros=findViewById(R.id.bt_aplicarFiltros);
        radioGroup=findViewById(R.id.rg_autoria);
        cv_inicio=findViewById(R.id.cv_inicio);
        cv_fin=findViewById(R.id.cv_fin);
        radioTodas = findViewById(R.id.rb_todasLasMediciones);
        radioMias = findViewById(R.id.rb_misMediciones);
        iv_abrir_autor=findViewById(R.id.iv_abrir_autor);
        iv_abrir_fechas=findViewById(R.id.iv_abrir_calendarios);
        tv_fechaIni=findViewById(R.id.tv_fechaIni);
        tv_fechaFin=findViewById(R.id.tv_fechaFin);
        bt_borrarFiltros=findViewById(R.id.bt_borrar_filtros);
        iv_abrir_tipo_mediciones=findViewById(R.id.iv_abrir_tipo_medicion);
        rb_co=findViewById(R.id.rb_co);
        rb_o3=findViewById(R.id.rb_o3);
        rb_no2=findViewById(R.id.rb_no2);
        rb_so2=findViewById(R.id.rb_so2);
        rb_iaq=findViewById(R.id.rb_iaq);
        rg_tipo=findViewById(R.id.rg_tipo);
        iv_volver=findViewById(R.id.iv_volver);
        iv_abrir_estaciones=findViewById(R.id.iv_abrir_estaciones);

        Intent intent = getIntent();
        long fechaInicioIntent = intent.getLongExtra("fechaInicio", 0);
        if(fechaInicioIntent != 0){
            fechaInicioSeleccionada = true;
            cv_inicio.setDate(fechaInicioIntent);
        }

        long fechaFinIntent = intent.getLongExtra("fechaFin", 0);
        if(fechaFinIntent != 0){
            fechaFinSeleccionada = true;
            cv_fin.setDate(fechaFinIntent);
        }

        int autorMedicionesIntent = intent.getIntExtra("autorMediciones", 0);
        if(autorMedicionesIntent == 0){
            radioTodas.setChecked(true);
        } else {
            radioMias.setChecked(true);
        }

        int tipoMedicion =  intent.getIntExtra("tipoMedicion", 0);
        if(tipoMedicion == 0){
            rb_iaq.setChecked(true);
        } else if (tipoMedicion == 1){
            rb_so2.setChecked(true);
        } else if (tipoMedicion == 2){
            rb_o3.setChecked(true);
        } else if (tipoMedicion == 3){
            rb_no2.setChecked(true);
        } else if (tipoMedicion == 4){
            rb_co.setChecked(true);
        }


        cv_inicio.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                fechaInicioSeleccionada = true;
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                view.setDate(c.getTimeInMillis());
            }
        });

        cv_fin.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                fechaFinSeleccionada = true;
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                view.setDate(c.getTimeInMillis());
            }
        });

        iv_abrir_autor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getVisibility()==View.VISIBLE){
                    radioGroup.setVisibility(View.GONE);
                    iv_abrir_autor.setImageResource(R.drawable.flecha_abajo);
                } else{
                    radioGroup.setVisibility(View.VISIBLE);
                    iv_abrir_autor.setImageResource(R.drawable.flecha_arriba);
                }

            }
        });

        iv_abrir_estaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch_estaciones.getVisibility()==View.VISIBLE){
                    switch_estaciones.setVisibility(View.GONE);
                    iv_abrir_estaciones.setImageResource(R.drawable.flecha_abajo);
                }else{
                    switch_estaciones.setVisibility(View.VISIBLE);
                    iv_abrir_estaciones.setImageResource(R.drawable.flecha_arriba);
                }
            }
        });
        iv_abrir_fechas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cv_inicio.getVisibility()==View.VISIBLE){
                    cv_inicio.setVisibility(View.GONE);
                    cv_fin.setVisibility(View.GONE);
                    tv_fechaIni.setVisibility(View.GONE);
                    tv_fechaFin.setVisibility(View.GONE);
                    iv_abrir_fechas.setImageResource(R.drawable.flecha_abajo);
                } else{
                    cv_inicio.setVisibility(View.VISIBLE);
                    cv_fin.setVisibility(View.VISIBLE);
                    tv_fechaIni.setVisibility(View.VISIBLE);
                    tv_fechaFin.setVisibility(View.VISIBLE);
                    iv_abrir_fechas.setImageResource(R.drawable.flecha_arriba);
                }

            }
        });

        iv_abrir_tipo_mediciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rg_tipo.getVisibility()==View.VISIBLE){
                    rg_tipo.setVisibility(View.GONE);
                    iv_abrir_tipo_mediciones.setImageResource(R.drawable.flecha_abajo);
                } else{
                    rg_tipo.setVisibility(View.VISIBLE);
                    iv_abrir_tipo_mediciones.setImageResource(R.drawable.flecha_arriba);
                }
            }
        });

        iv_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(FiltrosActivity.this, MapaActivity.class);
                startActivity(i);
            }
        });

        bt_borrarFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long fechaActual = System.currentTimeMillis();
                long unMes = 2629750000L;
                radioTodas.setChecked(true);
                radioMias.setChecked(false);
                cv_inicio.setDate(fechaActual-unMes);
                cv_fin.setDate(fechaActual);
                rb_iaq.setChecked(true);
                switch_estaciones.setChecked(true);
            }
        });

        bt_aplicarFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                //Obtener autor de las mediciones
                int autorMediciones;
                if (radioGroup.getCheckedRadioButtonId()==R.id.rb_misMediciones){
                    autorMediciones=1;
                }else{
                    autorMediciones=0;
                }
                intent.putExtra("autorMediciones", autorMediciones);
                
                //Obtener fechas mediciones
                long fechaInicio = 0;
                if(fechaInicioSeleccionada) {
                    fechaInicio = cv_inicio.getDate();
                }
                long fechaFin = 0;
                if(fechaFinSeleccionada) {
                    fechaFin = cv_fin.getDate();
                }

                intent.putExtra("fechaInicio", fechaInicio);
                intent.putExtra("fechaFin", fechaFin);

                //Obtener tipo de mediciones
                int tipoMedicion;
                if(rb_iaq.isChecked()){
                    tipoMedicion=0;
                } else if (rb_so2.isChecked()){
                    tipoMedicion=1;
                } else if (rb_o3.isChecked()){
                    tipoMedicion=2;
                } else if (rb_no2.isChecked()){
                    tipoMedicion=3;
                } else {
                    tipoMedicion=4;
                }
                intent.putExtra("tipoMedicion", tipoMedicion);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void guardarFiltro(View view){

    }
}