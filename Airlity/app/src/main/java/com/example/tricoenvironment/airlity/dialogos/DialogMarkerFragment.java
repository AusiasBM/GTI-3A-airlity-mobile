package com.example.tricoenvironment.airlity.dialogos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    TextView tv_nombre_estacion, tv_codigo;
    String nombreEstacion, fotoEstacion, codigoEstacion;

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

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_dialog_marker, null);
        builder.setView(v);

        ib_salir=v.findViewById(R.id.ib_salir);
        iv_foto_estacion=v.findViewById(R.id.iv_foto_estacion);
        tv_nombre_estacion=v.findViewById(R.id.tv_nombre_estacion);
        tv_codigo = v.findViewById(R.id.tv_codigo);

        tv_codigo.setText("Código: "+codigoEstacion);
        tv_nombre_estacion.setText(nombreEstacion+"");
        Picasso.with(getContext()).load(fotoEstacion).into(iv_foto_estacion);

        ib_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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