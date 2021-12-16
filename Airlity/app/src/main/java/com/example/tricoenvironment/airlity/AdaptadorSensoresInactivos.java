package com.example.tricoenvironment.airlity;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdaptadorSensoresInactivos extends RecyclerView.Adapter<AdaptadorSensoresInactivos.ViewHolder> {
    private LayoutInflater inflador;
    private ArrayList<Sensor> lista;



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView macSensor, correoUsusario, tipoMedicion, fechaRegistro;

        /**
         * Constructor de la clase ViewHolder. Asigna a cada TextView que tiene como campo privado
         * el elemento de la Vista que se le pasa como parámetro.
         *
         * itemView : View -> Constructor() ->
         *
         * @param itemView Se pasa un objeto de tipo View.
         *
         */
        public ViewHolder(View itemView) {
            super(itemView);
            macSensor = itemView.findViewById(R.id.tv_mac_sensores_inactivos);
            correoUsusario = itemView.findViewById(R.id.tv_correo_sensores_inactivos);
            tipoMedicion = itemView.findViewById(R.id.tv_tipo_sensores_inactivos);
            fechaRegistro = itemView.findViewById(R.id.textFechaRV);

        }

        public void asignarDatos(Sensor sensor) {

            //Poner en cada textView que tiene el layout elementos_recyclerview.xml
            // el dato que corresponda.
            macSensor.setText(sensor.getMac());
            correoUsusario.setText(sensor.getCorreoUsuario());
            tipoMedicion.setText(sensor.getTipoMedicion());

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm");
            Date resultado = new Date(sensor.getFecha());
            fechaRegistro.setText(resultado.toString());


        }
    }

    /**
     * El método onCreateViewHolder crea cada elemento del recycler. Se debe poner el layout donde
     * está la vista de cada elemento de la lista.
     *
     * parent : ViewGroup,
     * viewType : Z -> onCreateViewHolder() <-
     * ViewHolder <-

     * @return ViewHolder Crea el viewholder con los elementos (textViews, etc.)
     *
     */
    public AdaptadorSensoresInactivos(ArrayList<Sensor>lista) {
        this.lista = lista;
        //inflador = (LayoutInflater)
        //      context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("Nums", ""+this.lista);
    }

    @NonNull
    @Override
    public AdaptadorSensoresInactivos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elementos_recycler_inactivos, parent, false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull AdaptadorSensoresInactivos.ViewHolder holder, int position) {
        holder.asignarDatos(lista.get(position));
    }

    /**
     * El método getItemCount devuelve la longitud de la lista de objetos de tipo Medicion que tiene
     * esta clase
     *
     * N <- getItemCount() <-
     *
     * @return N Devuelve la longitud de la lista de objetos Medicion
     */
    @Override
    public int getItemCount() {
        return lista.size();
    }
}
