package com.example.tricoenvironment.airlity.dialogos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tricoenvironment.airlity.MapaActivity;
import com.example.tricoenvironment.airlity.Medicion;
import com.example.tricoenvironment.airlity.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogMarkerFragment extends DialogFragment {

    Activity activity;
    //IComunicaFragments iComunicaFragments;

    ImageButton ib_salir;
    ImageView iv_foto_estacion;
    TextView tv_nombre_estacion, tv_codigo, tv_comoLlegar;
    String nombreEstacion, fotoEstacion, codigoEstacion;
    Float latitud, longitud;

    public DialogMarkerFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        return crearDialogMarker();
    }

    private Dialog crearDialogMarker() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        SharedPreferences preferences= getContext().getSharedPreferences("infoEstacion", Context.MODE_PRIVATE);
        nombreEstacion = preferences.getString("nombreEstacion", null);
        fotoEstacion = preferences.getString("fotoEstacion", null);
        codigoEstacion = preferences.getString("codigoEstacion", null);
        latitud = preferences.getFloat("latitudEstacion", 0);
        longitud = preferences.getFloat("longitudEstacion", 0);


        LayoutInflater inflater=getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_dialog_marker, null);
        builder.setView(v);

        ib_salir=v.findViewById(R.id.ib_salir);
        iv_foto_estacion=v.findViewById(R.id.iv_foto_estacion);
        tv_nombre_estacion=v.findViewById(R.id.tv_nombre_estacion);
        tv_codigo = v.findViewById(R.id.tv_codigo);
        tv_comoLlegar = v.findViewById(R.id.tv_comoLLegar);

        tv_codigo.setText("Código: "+codigoEstacion);
        tv_nombre_estacion.setText(nombreEstacion+"");
        Picasso.with(getContext()).load(fotoEstacion).into(iv_foto_estacion);

        SpannableString mitextoU = new SpannableString("¿Cómo llegar?");
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        tv_comoLlegar.setText(mitextoU);

        ib_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_comoLlegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitud +"," + longitud);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.activity=(Activity) context;
        } else{
            throw new RuntimeException(context.toString());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_marker, container, false);
    }
}