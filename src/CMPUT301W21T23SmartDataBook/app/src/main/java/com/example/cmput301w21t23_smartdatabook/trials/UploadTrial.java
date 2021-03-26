package com.example.cmput301w21t23_smartdatabook.trials;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.user.User;
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
    String expType;
    User user = User.getUser();

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

                expType=experiment.getTrialType();
//                Toast toast= Toast.makeText(getApplicationContext(), "text is", Toast.LENGTH_LONG);
//                toast.show();
                // 1. 4 different cases for dialog
                // 2. 4 XML diagram associate with the trial type
                // Go from upload trial to each of trial types
                // 3. 3 fragments, shows binomial fragment or input fragment, get trial
//                if statement to deal with experiment.gettype
//                Log.d("Test", "Add Trial Button");

                // 1st case: if the experiment's trial type is binomial, incomplete
                // I learned about string matching on java from tutorialspoint
                // URL: https://www.tutorialspoint.com/java/java_string_matches.htm
                // Q1: how to take input?
                if (expType.matches("Binomial")){
                    AlertDialog.Builder builder= new AlertDialog.Builder((UploadTrial.this));
                    builder.setTitle("Add Binomial Trials?");
                    final EditText numBinomial= new EditText(UploadTrial.this);
                    // set input to be integer and positives only
                    numBinomial.setInputType(InputType.TYPE_CLASS_NUMBER);
                    numBinomial.setHint("Enter positive number of passes/failures");
                    builder.setView(numBinomial);

                    builder
                            .setNeutralButton("Add passes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("Test", "test");
                        }
                    })
                            .setNegativeButton("Add failure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("Test", "test");
                            }
                            })
                            .setPositiveButton("Cancel", null).create().show();
                }
                // 2nd case: if the experiment's trial type is count, incomplete
                // Q1: how to take input?
                if (expType.matches("Count")){
                    AlertDialog.Builder builder= new AlertDialog.Builder((UploadTrial.this));
                    builder.setTitle("Add Count Trials?");
                    final EditText numCount= new EditText(UploadTrial.this);
                    // set input to be any integer
                    numCount.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_SIGNED);
                    numCount.setHint("Enter positive/negative number of counts");
                    builder.setView(numCount);

                    builder.setNegativeButton("Cancel", null)
                            .setPositiveButton("Add Trials", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // include 0 as well

                                    // save the data
                                    Log.d("Test", "test");

                                }
                            }).create().show();
                }
                // 3rd case: if the experiment's trial type is non-negative count, incomplete
                // Q1: how to take input?
                if (expType.matches("Non-Negative Count")){
                    AlertDialog.Builder builder= new AlertDialog.Builder((UploadTrial.this));
                    builder.setTitle("Add Non-Negative Count Trials?");
                    final EditText numNonNegCount= new EditText(UploadTrial.this);
                    // set input to be integer and positives only
                    numNonNegCount.setInputType(InputType.TYPE_CLASS_NUMBER);
                    numNonNegCount.setHint("Enter a positive number");
                    builder.setView(numNonNegCount);


                    builder.setNegativeButton("Cancel", null)
                            .setPositiveButton("Add trials", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // include 0 as well
                                    if (Integer.parseInt(numNonNegCount.getText().toString())>=0){
                                        // save the data
                                        Log.d("Test", "test");
                                    }
                                }
                            }).create().show();
                }
                // 4th case: if the experiment's trial type is measurement, partial complete,
                if (expType.matches("Measurement")){
                    AlertDialog.Builder builder= new AlertDialog.Builder((UploadTrial.this));
                    builder.setTitle("Add Measurement Trials?");
                    builder.setMessage("Enter measurement below:");
                    final EditText measurementInput= new EditText(UploadTrial.this);
                    measurementInput.setHint("In Integers or Floats");
                    // set input to be exclusively integer and decimal
                    measurementInput.setInputType(InputType.TYPE_CLASS_NUMBER  | InputType.TYPE_NUMBER_FLAG_DECIMAL| InputType.TYPE_NUMBER_FLAG_SIGNED);
                    builder.setView(measurementInput);
                    builder.setNegativeButton("Cancel", null)
                            .setPositiveButton("Add Trials", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // once click add trials, add the trial's outcome, location
                                    // Q1: check editText value
                                    // Q2: how to save information
                                    // check input for

                                    // include 0 as well

                                    // save the data
                                    Log.d("Test", "test");

                                }
                            }).create().show();
                }


            }
        });
        // gg

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