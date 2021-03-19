package com.example.cmput301w21t23_smartdatabook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Class: UploadTrial activity
 * displays the upload trials page
 * @author Afaq Nabi, Krutik Soni
 * @see TrialList
 */
public class UploadTrial extends AppCompatActivity {
    ListView trialsList;
    ArrayAdapter<Trial> trialArrayAdapter;
    ArrayList<Trial> trialDataList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    /**
     * This function create the uploadTrial view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_trial);

        Database db = new Database();

        setSupportActionBar(findViewById(R.id.app_toolbar));
        ActionBar toolbar = getSupportActionBar();

        assert toolbar != null;
        toolbar.setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("Upload Trials");

        // get intetn and experiment
        Intent intent = getIntent();
        Experiment experiment = (Experiment) intent.getSerializableExtra("experiment"); // get the experiment object

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();

        TextView name = findViewById(R.id.actual_experiment_name);
        name.setText(experiment.getExpName());

        TextView userName = findViewById(R.id.actual_user_name);
        userName.setText(experiment.getOwnerUserID());

        // Set Headers
        TextView nameHeader = findViewById(R.id.experiment_name);
        nameHeader.setText("Experiment Name: ");

        TextView userNameHeader = findViewById(R.id.username);
        userNameHeader.setText("UserName");

        TextView trialsHeader = findViewById(R.id.user_trial);
        trialsHeader.setText("YOUR TRIALS");

        Button addTrials = findViewById(R.id.add_trial_button);
        addTrials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Test", "Add Trial Button");
            }
        });

        trialsList = findViewById(R.id.uploaded_trials);
        trialDataList = new ArrayList<>();

        trialDataList.add(new Trial("123","1234"));

        trialArrayAdapter = new TrialList( trialDataList, getBaseContext());
        trialsList.setAdapter(trialArrayAdapter);

        trialsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadTrial.this);
                builder.setTitle("Delete Trial?");
                builder.setNegativeButton("cancel",  null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("Test","test");
                            }
                        }).create().show();
            }
        });

    }
}
