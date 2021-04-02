package com.example.cmput301w21t23_smartdatabook.stats;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.Experiment;

public class StatsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Experiment experiment = (Experiment) intent.getSerializableExtra("experiment");





    }
}
