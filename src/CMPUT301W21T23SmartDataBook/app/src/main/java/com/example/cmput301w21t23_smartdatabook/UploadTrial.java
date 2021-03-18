package com.example.cmput301w21t23_smartdatabook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class UploadTrial extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_trial);

        setSupportActionBar(findViewById(R.id.app_toolbar));
        ActionBar toolbar = getSupportActionBar();

        assert toolbar != null;
        toolbar.setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("Upload Trials");

        Intent intent = getIntent();
        Experiment experiment = (Experiment) intent.getSerializableExtra("experiment"); // get the experiment object

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();

        TextView name = findViewById(R.id.actual_experiment_name);
        TextView userName = findViewById(R.id.actual_user_name);

        // Set Headers
        TextView nameHeader = findViewById(R.id.experiment_name);
        nameHeader.setText("Experiment Name: ");

        TextView userNameHEader = findViewById(R.id.username);
        userNameHEader.setText("UserName");

        TextView trialsHeader = findViewById(R.id.user_trial);
        trialsHeader.setText("YOUR TRIALS");

        Button addTrials = findViewById(R.id.add_trial_button);
        addTrials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Test", "Add Trial Button");
            }
        });

        ListView trials = findViewById(R.id.uploaded_trials);




    }
}
