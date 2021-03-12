package com.example.cmput301w21t23_smartdatabook;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class experimentDetails extends AppCompatActivity {
    public int position;

    public experimentDetails(int pos) {
        this.position = pos;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experiment_details);
    }

}
