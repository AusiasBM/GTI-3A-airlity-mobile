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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FiltrosActivity extends AppCompatActivity {

    Button bt_aplicarFiltros, bt_borrarFiltros;
    RadioGroup radioGroup;
    RadioButton radioTodas;
    RadioButton radioMias;
    CalendarView cv_inicio, cv_fin;
    Boolean fechaInicioSeleccionada = false;
    Boolean fechaFinSeleccionada = false;
    ImageView iv_abrir_autor, iv_abrir_fechas, iv_abrir_tipo_mediciones, iv_abrir_posicion, iv_volver;
    TextView tv_fechaIni, tv_fechaFin;
    CheckBox cb_o3, cb_no2, cb_co, cb_so2, cb_iaq;

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
        cb_co=findViewById(R.id.cb_CO);
        cb_o3=findViewById(R.id.cb_o3);
        cb_no2=findViewById(R.id.cb_no2);
        cb_so2=findViewById(R.id.cb_SO2);
        cb_iaq=findViewById(R.id.cb_IAQ);
        iv_volver=findViewById(R.id.iv_volver);



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

        int tipoSO2 = intent.getIntExtra("tipoSO2", 0);
        if(tipoSO2 == 0){
            cb_so2.setChecked(true);
        } else {
            cb_so2.setChecked(false);
        }

        int tipoO3 = intent.getIntExtra("tipoO3", 0);
        if(tipoO3 == 0){
            cb_o3.setChecked(true);
        } else {
            cb_o3.setChecked(false);
        }

        int tipoNO2 = intent.getIntExtra("tipoNO2", 0);
        if(tipoNO2 == 0){
            cb_no2.setChecked(true);
        } else {
            cb_no2.setChecked(false);
        }

        final int tipoCO = intent.getIntExtra("tipoCO", 0);
        if(tipoCO == 0){
            cb_co.setChecked(true);
        } else {
            cb_co.setChecked(false);
        }

        int tipoIAQ = intent.getIntExtra("tipoIAQ", 0);
        if(tipoIAQ == 0){
            cb_iaq.setChecked(true);
        } else {
            cb_iaq.setChecked(false);
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
                if (cb_co.getVisibility()==View.VISIBLE){
                    cb_co.setVisibility(View.GONE);
                    cb_o3.setVisibility(View.GONE);
                    cb_no2.setVisibility(View.GONE);
                    cb_so2.setVisibility(View.GONE);
                    cb_iaq.setVisibility(View.GONE);
                    iv_abrir_tipo_mediciones.setImageResource(R.drawable.flecha_abajo);
                } else{
                    cb_co.setVisibility(View.VISIBLE);
                    cb_o3.setVisibility(View.VISIBLE);
                    cb_no2.setVisibility(View.VISIBLE);
                    cb_so2.setVisibility(View.VISIBLE);
                    cb_iaq.setVisibility(View.VISIBLE);
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
                cb_iaq.setChecked(true);
                cb_co.setChecked(true);
                cb_no2.setChecked(true);
                cb_o3.setChecked(true);
                cb_so2.setChecked(true);
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
                int tipoSO2;
                if(cb_so2.isChecked()){
                    tipoSO2=0;
                } else {
                    tipoSO2=1;
                }
                intent.putExtra("tipoSO2", tipoSO2);

                int tipoO3;
                if(cb_o3.isChecked()){
                    tipoO3=0;
                } else {
                    tipoO3=1;
                }
                intent.putExtra("tipoO3", tipoO3);

                int tipoNO2;
                if(cb_no2.isChecked()){
                    tipoNO2=0;
                } else {
                    tipoNO2=1;
                }
                intent.putExtra("tipoNO2", tipoNO2);

                int tipoCO;
                if(cb_co.isChecked()){
                    tipoCO=0;
                } else {
                    tipoCO=1;
                }
                intent.putExtra("tipoCO", tipoCO);

                int tipoIAQ;
                if(cb_iaq.isChecked()){
                    tipoIAQ=0;
                } else {
                    tipoIAQ=1;
                }
                intent.putExtra("tipoIAQ", tipoIAQ);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}