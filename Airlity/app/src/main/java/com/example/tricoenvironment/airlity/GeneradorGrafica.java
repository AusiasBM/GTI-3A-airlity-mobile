/**
 * GeneradorGrafica.java
 * @fecha: 22/11/2021
 * @autor: Aitor Benítez Estruch
 *
 * @Descripcion:
 * Clase para generar un gráfico de barras de las mediciones de un periodo de tiempo determinado
 *
 */


package com.example.tricoenvironment.airlity;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Clase GeneradorGrafica
 */
public class GeneradorGrafica {

    private ArrayList<String> fechas = new ArrayList<>();
    private ArrayList<Double> mediciones = new ArrayList<>();
    private BarChart barChart;
    private String gas;
    private float limite;


    /**
     * Constructor
     * @param mediciones Lista de mediciones
     * @param fechas Lista de fechas
     * @param barChart Objeto de tipo Barchart (vista) donde se representará el gráfico
     */
    public GeneradorGrafica(ArrayList<Double> mediciones, ArrayList<Long> fechas, BarChart barChart, String gas) {
        this.barChart = barChart;
        this.mediciones = mediciones;
        this.gas = gas;
        umbralMaximoDiario();
        sacarHoraFormatoTexto(fechas);


    }


    /**
     * umbralMaximoDiario()
     * Descripción:
     * Método para establecer el umbral de ppm máximo diario según el gas
     *
     *
     */
    private void umbralMaximoDiario(){
        switch (gas){
            case "CO":
                limite = 20f;
                break;
            case "NO2":
                limite = 0.5f;
                break;
            case "SO2":
                limite = 0.5f;
                break;
            case "O3":
                limite = 0.2f;
                break;
            case "IAQ":
                limite = 20;
                break;
            default:
                limite = 0;

        }
    }

    /**
     * sacarHoraFormatoTexto()
     * Descripción:
     * Método para convertir las horas almacenadas en un array en milisegundos a formato hora : minutos
     *
     * @param fechas Lista de números Long con las horas
     * @return Lista de Texto con las horas formateadas
     *
     */
    private void sacarHoraFormatoTexto(ArrayList<Long> fechas){
        for(int i = 0; i < fechas.size(); i++){
            Date date = new Date();
            date. setTime(fechas.get(i));
            this.fechas.add(new SimpleDateFormat("HH:mm").format(date));
        }
    }


    /**
     * getSameChart()
     * Descripcion:
     * Metodo para personalizar el gráfico (añadir una descripción, elegir tamaño de letra, color de fondo, animación al generar el gráfico... )
     *
     * @param chart
     * @param description
     * @param textColor
     * @param backgroudColor
     * @param animationY
     * @return chart
     */
    private Chart getSameChart(Chart chart, String description, int textColor, int backgroudColor, int animationY){
        chart.getDescription().setText(description);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(backgroudColor);
        chart.animateY(animationY);
        chart.setExtraOffsets(5f,5f,5f,30f);
        legend(chart);
        return chart;
    }


    /**
     * legend()
     * Descripción:
     * método para personalizar la leyenda del gráfico
     *
     * @param chart
     */
    private void legend(Chart chart){
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setDrawInside(false);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);


        ArrayList<LegendEntry> entries = new ArrayList<>();
        LegendEntry entry = new LegendEntry();
        entry.label = gas;
        entry.formColor = Color.CYAN;
        entries.add(entry);
        legend.setCustom(entries);
    }


    /**
     * getBarEntries()
     * Descripción:
     * Método para obtener los valores del eje Y (AKA concentración de gas en ppm o ppb) dependiente del eje X (AKA tiempo comprendido)
     *
     * @return Lista<BarEntry> Lista de valores del eje Y (concentración de gas)
     */
    private ArrayList<BarEntry> getBarEntries() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        //Log.d("MEDIDAS OBTENIDAS",  " " + mediciones.size());
        for(int i = 0; i < mediciones.size(); i++){
            //Log.d("MEDIDAS OBTENIDAS",  " " + mediciones.get(i));
            barEntries.add(new BarEntry(i, mediciones.get(i).floatValue()));
        }

        return barEntries;
    }

    /**
     * axisX()
     * Descripción:
     * Método para personalizar el eje X del gráfico
     *
     *
     * @param axis Objeto de tipo Xaxis que permite configurar el eje X
     */
    private void axisX(XAxis axis){
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(fechas));
        axis.setLabelRotationAngle(90);
        //axis.setAdjustXLabels(true);
        axis.setGranularity(1f);
        axis.setGranularityEnabled(true);

    }

    /**
     * axisLeft()
     * Descripción:
     * Método para personalizar el eje Y izquierdo del gráfico
     *
     * @param axis Objeto de tipo Yaxis que permite configurar el eje y
     */
    private void axisLeft(YAxis axis){
        axis.setAxisMinimum(0);
        axis.setSpaceTop(30);
        LimitLine linea = new LimitLine(limite);
        axis.addLimitLine(linea);

    }

    /**
     * axisRight()
     * Descripción:
     * Método desactivar el eje Y del gráfico
     *
     * @param axis Objeto de tipo Yaxis que permite configurar el eje y
     */
    private void axisRight(YAxis axis){
        axis.setEnabled(false);
    }


    /**
     * createChart()
     * Descripción:
     * Método para generar el gráfico a partir de la configuración hecha en los métodos de personalización del
     * gráfico.
     *
     */
    public void createChart(){
        barChart = (BarChart) getSameChart(barChart, "",Color.BLACK, Color.WHITE, 500 );
        barChart.setDrawBarShadow(true);
        barChart.setDrawGridBackground(true);
        barChart.setData(getBarData());
        barChart.invalidate();
        barChart.getLegend().setEnabled(true);

        axisX(barChart.getXAxis());
        axisLeft(barChart.getAxisLeft());
        axisRight(barChart.getAxisRight());

    }

    private DataSet getData(DataSet dataSet){
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(0);
        return dataSet;
    }

    private BarData getBarData(){
        BarDataSet barDataSet = (BarDataSet)getData(new BarDataSet(getBarEntries(), ""));
        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData = new BarData(barDataSet);
        return barData;
    }



}
