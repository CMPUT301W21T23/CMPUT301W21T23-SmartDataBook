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
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
//import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

                stats.bubbleSort(statsDataList); // sort list

                float j = 0;
                List<Entry> entries = new ArrayList<Entry>();
                for (int i = 0; i<statsDataList.size(); i++){
//                    Date date = dateClass.getDate((String) statsDataList.get(i).get(1));
//                    entries.add(new Entry(date.getTime(),
//                            Float.parseFloat((String) statsDataList.get(i).get(0))));
                    Date date = dateClass.getDate((String) statsDataList.get(i).get(1));
                    String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Timestamp(date.getTime()));
                    float testFloat = testMinTimeStamp(timeStamp);
                    entries.add(new Entry(j,
                            Float.parseFloat(statsDataList.get(i).get(0).toString())) );
                    j += 1;
                }

                LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
                dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                dataSet.setLineWidth(2.0f);
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

    public float testMinTimeStamp(String timeStamp) {
        String[] timeStampList = timeStamp.split(":");
        Log.d("Timestamp", ""+ timeStampList[0] + " " + timeStampList[1]);
        float min = Float.parseFloat(timeStampList[0]);
        float sec = Float.parseFloat(timeStampList[1]);
    }



}


