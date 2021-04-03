package com.example.cmput301w21t23_smartdatabook.stats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Array;
import java.sql.Time;
import java.sql.Timestamp;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.StringDate;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
//import com.google.firebase.Timestamp;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class StatsView extends AppCompatActivity {

    private ArrayList<ArrayList> statsDataList;
    private User user;
    private Database database;
    private FirebaseFirestore db;
    StringDate dateClass = new StringDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.graph_layout);

        database = new Database();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        Experiment experiment = (Experiment) intent.getSerializableExtra("experiment");

        double mean;

        statsDataList = new ArrayList<>();

        statsDataList.clear();

        LineChart lineChart = (LineChart) findViewById(R.id.plotChart);
        BarChart barChart = (BarChart) findViewById(R.id.barChart);

        StatisticsModel stats = new StatisticsModel();

        TextView meanView = findViewById(R.id.meanTextView);
        TextView medianView = findViewById(R.id.medianTextView);
        TextView SDView = findViewById(R.id.stdDeviationTextView);

        // https://weeklycoding.com/mpandroidchart-documentation/getting-started/
        //Source: Erwin Kurniawan A; https://stackoverflow.com/users/7693494/erwin-kurniawan-a
        //Code: https://stackoverflow.com/questions/61930061/how-to-return-a-value-from-oncompletelistener-while-creating-user-with-email-and
        database.fillStatsList(new GeneralDataCallBack() {
            @Override
            public void onDataReturn(Object returnedObject) {

                ArrayList DataList = (ArrayList) returnedObject;

                statsDataList = DataList;

                stats.bubbleSortByDate(statsDataList); //sort list by date

                List<Entry> entries = new ArrayList<Entry>();
                for (int i = 0; i<statsDataList.size(); i++){
//                    Date date = dateClass.getDate((String) statsDataList.get(i).get(1));
//                    entries.add(new Entry(date.getTime(),
//                            Float.parseFloat((String) statsDataList.get(i).get(0))));

                    Log.d("Entire", ""+statsDataList.get(i).get(0).toString()+ " " + statsDataList.get(i).get(1).toString());
                    entries.add(new Entry(i,
                            Float.parseFloat(statsDataList.get(i).get(0).toString())) );
                }

                //https://github.com/PhilJay/MPAndroidChart/issues/3705
                ValueFormatter formatter = new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        stats.bubbleSortByDate(statsDataList); //sort list by date
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                        Log.d("Date", ""+ (int) value + "|" + sdf.format( dateClass.getDate( statsDataList.get( (int)value ).get(1).toString() )));
                        return sdf.format( dateClass.getDate( statsDataList.get( (int)value ).get(1).toString() ) );
                    }
                };

                LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
                dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                dataSet.setLineWidth(2.0f);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(formatter);

                LineData lineData = new LineData(dataSet);
                lineChart.setData(lineData);
                lineChart.invalidate(); // refresh

                //Printing Forloop
//                for (int i = 0; i< statsDataList.size(); i++){
//                    Date result1 = dateClass.getDate((String) statsDataList.get(i).get(1));
//                    Log.d("Time: ", ""+ result1);
//                    assert result1 != null;
//                    Log.d("int", ""+ (int)result1.getTime());
//                }







                stats.bubbleSortByValue(statsDataList); // sort list by value

                ArrayList<Double> sortedArray = new ArrayList<>();
                for (int i = 0; i < statsDataList.size(); i++) {
                    sortedArray.add((Double) statsDataList.get(i).get(0));
                }
                double[] quartiles = stats.quartiles(sortedArray);
                double SD = stats.calculateSD(sortedArray);
                double mean = (double) stats.calcMean(statsDataList);
                double median = (double) stats.calcMedian(statsDataList);

                meanView.setText("Mean: "+ Double.parseDouble(String.valueOf(mean)));
                medianView.setText("Median: "+ String.valueOf(median));
                SDView.setText("Std: "+String.valueOf(SD));



            }
        } , statsDataList, db.collection("Experiments").document(experiment.getExpID()).collection("Trials"));

    }//onCreate


}


