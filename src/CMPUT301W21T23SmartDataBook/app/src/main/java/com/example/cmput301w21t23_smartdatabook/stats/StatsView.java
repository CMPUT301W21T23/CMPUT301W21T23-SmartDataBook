package com.example.cmput301w21t23_smartdatabook.stats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.home.CardList;
import com.example.cmput301w21t23_smartdatabook.home.addExpFragment;
import com.example.cmput301w21t23_smartdatabook.home.homePage;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;



public class StatsView extends AppCompatActivity {

    private ArrayList<ArrayList> statsDataList;
    private User user;
    private Database database;
    private FirebaseFirestore db;

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

//        LineChart lineChart = findViewById(R.id.plotChart);
//        BarChart barChart = findViewById(R.id.barChart);

        //Source: Erwin Kurniawan A; https://stackoverflow.com/users/7693494/erwin-kurniawan-a
        //Code: https://stackoverflow.com/questions/61930061/how-to-return-a-value-from-oncompletelistener-while-creating-user-with-email-and
        database.fillStatsList(new GeneralDataCallBack() {
            @Override
            public void onDataReturn(Object returnedObject) {

                ArrayList DataList = (ArrayList) returnedObject;

                statsDataList = DataList;

                Number mean = calcMean(statsDataList);

//                [ [Number, String], [Number, String], ... ]

                for (int i = 0; i < statsDataList.size(); i++) {
                    Log.d("Item", ""+statsDataList.get(i));
                }

                for (int i = 0; i < statsDataList.size(); i++) {
                    for (int j = 0; j < statsDataList.size()-i-1; j++){
                        Log.d("First", "" + statsDataList.get(j).get(0));
                        Log.d("Second", "" + statsDataList.get(j+1).get(0));

                        float num1 = (float) statsDataList.get(i).get(0);
                        float num2 = (float) statsDataList.get(i+1).get(0);

                        if (num1 > num2){

                            ArrayList temp = statsDataList.get(j);
                            statsDataList.set(j, statsDataList.get(j+1));
                            statsDataList.set(j+1,temp);
                        }
                    }
                }

//                for(int i = 0; i < rowCount; i++){
//                    for(int j = 0; j < colCount; j++){
//                        for(int k = 0; k < colCount; k++){
//                            if(differencearray[i][k]>differencearray[i][k+1]){
//
//                                int temp = differencearray[i][k];
//                                differencearray[i][k] = differencearray[i][k+1];
//                                differencearray[i][k+1] = temp;
//                            }
//                        }
//                    }
//                }

                for (int i = 0; i < statsDataList.size(); i++) {
                    Log.d("Item", ""+statsDataList.get(i));
                }

//                Number median = calcMedian(statsDataList);

            }
        } , statsDataList, db.collection("Experiments").document(experiment.getExpID()).collection("Trials"));

    }//onCreate

    public Number calcMean(ArrayList<ArrayList> statsDataList){
        int sum = 0;
        for (int i = 0; i < statsDataList.size(); i++){
            sum += statsDataList.get(i).indexOf(0);
        }
        if (statsDataList.size() == 0) {
            return 0;
        }else{
            return sum/statsDataList.size();
        }

    }

    public Number calcMedian(ArrayList<ArrayList> statsDataList) {
        int middle = statsDataList.size()/2;
        if (statsDataList.size()%2 == 1) {
            return statsDataList.get(middle).indexOf(0);
        } else {
            return (statsDataList.get(middle-1).indexOf(0) + statsDataList.get(middle).indexOf(0) / 2.0);
        }
    }



}


