package com.example.tricoenvironment.airlity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class PopUpRegistro extends AppCompatActivity {

    TextView tv_escanear_popup;
    String macUsuarioDato;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_registro);
        tv_escanear_popup = findViewById(R.id.tvEscanear);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int ancho= displayMetrics.widthPixels;
        int alto = displayMetrics.heightPixels;

        getWindow().setLayout((int) (ancho * 0.6), (int)(alto * 0.15));

        SpannableString mitextoU = new SpannableString("Escanear sensor");
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        tv_escanear_popup.setText(mitextoU);

        tv_escanear_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Escanee el QR de un beacon", Toast.LENGTH_LONG).show();
                new IntentIntegrator(PopUpRegistro.this).initiateScan();
            }
        });
    }

    /**
     * Método llamado al escanear un QR
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        macUsuarioDato = intentResult.getContents();

        try {
            Log.d("ESCANEEEEEER", macUsuarioDato);
            Gson gson = new Gson();

            DatosScanner dc = gson.fromJson(macUsuarioDato, DatosScanner.class);

            Log.d("ESCANEEEEEER", dc.toString());

            if(!dc.getMacSensor().equals(null) && !dc.getTipoMedicion().equals(null)){
                //Toast.makeText(this, "Sensor encontrado", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, SignUpActivity.class);
                i.putExtra("macUsuario", macUsuarioDato);
                startActivity(i);
            }else{
                //Toast.makeText(getApplicationContext(), "Error al escanear", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, SignInActivity.class);
                i.putExtra("error", "error");
                startActivity(i);
            }

        }catch(Exception e){
            //Toast.makeText(getApplicationContext(), "Error al escanear", Toast.LENGTH_LONG).show();
            Log.d("ESCANEEEEEER", "Error de escaneo");
        }



    }
}