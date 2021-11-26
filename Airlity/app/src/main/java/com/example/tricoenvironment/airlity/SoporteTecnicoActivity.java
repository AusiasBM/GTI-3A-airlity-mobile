package com.example.tricoenvironment.airlity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SoporteTecnicoActivity extends AppCompatActivity {

    Bundle datos;
    boolean usuarioRegistrado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soporte_tecnico);
        datos = getIntent().getExtras();
        usuarioRegistrado = datos.getBoolean("sesionIniciada");
    }
}