package com.example.tricoenvironment.airlity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    ImageView iv_logo, iv_nombre;
    TextView tv_lema;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        iv_logo=findViewById(R.id.iv_logo_splash);
        iv_nombre=findViewById(R.id.iv_nombre);
        tv_lema=findViewById(R.id.tv_lema);

        SharedPreferences preferences=getSharedPreferences("com.example.tricoenvironment.airlity", Context.MODE_PRIVATE);
        String tokken = preferences.getString("tokken", null);
        Log.d("tokken", preferences.getString("tokken", null) +"");
        if(tokken != null){
            Intent intent = new Intent(SplashScreen.this, MapaActivity.class);
            startActivity(intent);
            finish();
        }else{
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotacion);
            animation.setFillAfter(true);
            iv_logo.startAnimation(animation);

            AlphaAnimation fadeIn=new AlphaAnimation(0.0f, 1.0f);
            fadeIn.setDuration(1000);
            fadeIn.setStartOffset(1500);
            fadeIn.setFillAfter(true);

            iv_nombre.setAnimation(fadeIn);

            AlphaAnimation fadeIn2=new AlphaAnimation(0.0f, 1.0f);
            fadeIn2.setDuration(1000);
            fadeIn2.setStartOffset(2000);
            fadeIn2.setFillAfter(true);
            tv_lema.setAnimation(fadeIn2);

            new Handler().postDelayed(new Runnable(){
                public void run(){

                    //----------------------------
                    Intent intent = new Intent(SplashScreen.this, Mapa_interpolacion.class);
                    startActivity(intent);
                    finish();
                    //----------------------------

                }
            }, 3000);
        }


    }
}