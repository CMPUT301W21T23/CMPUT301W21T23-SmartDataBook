package com.example.cmput301w21t23_smartdatabook.trials;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cmput301w21t23_smartdatabook.stats.StringDate;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.experiment.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.geolocation.LocationWithPermission;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

/**
 * Class: UploadTrial activity
 * displays the upload trials page
 * @author Afaq Nabi, Krutik Soni, Jayden Cho, Bosco Chan
 * @see TrialList, Trial
 */
public class UploadTrial extends AppCompatActivity {
    ListView trialsList;
    ArrayAdapter<Trial> trialArrayAdapter;
    ArrayList<Trial> trialDataList;

    String expType;

    User user = User.getUser();

    Database database = Database.getDataBase();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    StringDate stringDate = new StringDate();

    /**
     * This function sets up the uploadTrial view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_trial);

        setSupportActionBar(findViewById(R.id.app_toolbar));
        ActionBar toolbar = getSupportActionBar();

        assert toolbar != null;

        toolbar.setTitle("Upload Trials");

        // get intent and experiment
        Intent intent = getIntent();
        Experiment experiment = (Experiment) intent.getSerializableExtra("experiment"); // get the experiment object

        TextView name = findViewById(R.id.actual_experiment_name);
        name.setText(experiment.getExpName());

        database.fillUserName(new GeneralDataCallBack() {
            @Override
            public void onDataReturn(Object returnedObject) {
                Hashtable<String, User> UserName = (Hashtable<String, User>) returnedObject;
                TextView userName = findViewById(R.id.actual_user_name);
                userName.setText( UserName.get(experiment.getOwnerUserID()).getUserName() );
            }
        });

        // Set Headers
        TextView nameHeader = findViewById(R.id.experiment_name);
        nameHeader.setText("Experiment Name: ");

        TextView userNameHeader = findViewById(R.id.username);
        userNameHeader.setText("UserName");

        TextView trialsHeader = findViewById(R.id.user_trial);
        trialsHeader.setText("YOUR TRIALS");

        Button addTrials = findViewById(R.id.add_trial_button);

        // add conditional to make sure archived experiments can't upload trials

        /**
         * Methods to handle upload trials based on different types of trials in experiment by using dialog
         * @author Alex Mak
         */
        addTrials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!experiment.getIsEnd()) {
                    expType = experiment.getTrialType();
                    db
                            .collection("Experiments")
                            .document(experiment.getExpID())
                            .collection("Trials")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        int count = 0;
                                        boolean found = false;
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            count += 1;
                                            if (count == experiment.getMaxTrials()) {
                                                found = true;
                                                Toast.makeText(UploadTrial.this, "This Experiment has reached the max number of trials", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        if (!found) {
                                            addTrialDialogs(expType, experiment);
                                        }
                                    }
                                }
                            });
                }
            }
        });
        trialsList = findViewById(R.id.uploaded_trials);
        trialDataList = new ArrayList<>();

        trialArrayAdapter = new TrialList(trialDataList, getBaseContext());
        trialsList.setAdapter(trialArrayAdapter);
        database.fillTrialList(db
                        .collection("Experiments")
                        .document(experiment.getExpID())
                        .collection("Trials")
                , trialDataList, trialArrayAdapter);

        // TODO: Users can delete the trials that they entered but owner can delete any trials
        trialsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (experiment.getOwnerUserID().equals(user.getUserUniqueID())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UploadTrial.this);
                    builder.setTitle("Delete Trial?");
                    builder.setNegativeButton("cancel", null)
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Trial trial = trialDataList.get(position);
                                    database.deleteFromDB(db
                                            .collection("Experiments")
                                            .document(experiment.getExpID())
                                            .collection("Trials")
                                            .document(trial.getTrialID()));
                                    recreate();
                                }
                            }).create().show();
                } else {
                    Toast.makeText(UploadTrial.this, "You don't have the privilege to delete trials", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addTrialDialogs(String expType, Experiment experiment) {
        new LocationWithPermission(UploadTrial.this).getLatLng(new GeneralDataCallBack() {
            @Override
            public void onDataReturn(Object returnedObject) {
                Location location = (Location) returnedObject;
                if (location == null) {
                    Toast.makeText(UploadTrial.this, "Please open up the google map and obtain your location at least once.", Toast.LENGTH_LONG).show();
                    return;
                }

                LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

                // 1. 4 different cases for dialog
                // 2. 4 XML diagram associate with the trial type
                // Go from upload trial to each of trial types
                // 3. 3 fragments, shows binomial fragment or input fragment, get trial

                // 1st case: if the experiment's trial type is binomial
                // I learned about string matching on java from tutorialspoint
                // URL: https://www.tutorialspoint.com/java/java_string_matches.htm
                // Q1: how to take input?
                if (expType.matches("Binomial")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UploadTrial.this);
                    builder.setTitle("Add Binomial Trials?");
                    final EditText numBinomial = new EditText(UploadTrial.this);
                    // set input to be integer and positives only
                    numBinomial.setInputType(InputType.TYPE_CLASS_NUMBER);
                    numBinomial.setHint("Enter positive number of passes/failures");
                    builder.setView(numBinomial);
                    builder
                            .setNeutralButton("Add passes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (numBinomial.getText().toString().length() == 0){
                                        Toast.makeText(UploadTrial.this, "Trial value field must not be empty", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }else if (Integer.parseInt(numBinomial.getText().toString()) > experiment.getMaxTrials()){
                                        Toast.makeText(UploadTrial.this, "You cannot add more trials than the maximum trials for this experiment at once", Toast.LENGTH_SHORT).show();
                                    } else {
                                        db
                                                .collection("Experiments")
                                                .document(experiment.getExpID())
                                                .collection("Trials")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                       if (task.isSuccessful()) {
                                                           int count = 0;
                                                           for (QueryDocumentSnapshot document : task.getResult()) {
                                                               count+=1;
                                                           }

                                                           if (count+Integer.parseInt(numBinomial.getText().toString()) > experiment.getMaxTrials()){
                                                               Toast.makeText(UploadTrial.this, "You cannot add more trials than the maximum trials for this experiment at once", Toast.LENGTH_SHORT).show();
                                                           } else{
                                                               for (int i = 0; i < Integer.parseInt(numBinomial.getText().toString()); i++) {
                                                                   Trial trial = new Trial(experiment.getRequireLocation(),
                                                                           experiment.getTrialType(),
                                                                           true,
                                                                           experiment.getOwnerUserID(),
                                                                           UUID.randomUUID().toString(),
                                                                           stringDate.getShortDate(),
                                                                           experiment.getRequireLocation() ? latlng : null);
                                                                   collectionRefToDB(trial, experiment);
                                                               }
                                                           }
                                                       }
                                                       recreate();
                                                   }
                                                });
                                    }
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Add failure", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (numBinomial.getText().toString().length() == 0) {
                                        Toast.makeText(UploadTrial.this, "Trial value field must not be empty", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();

                                    } else if (Integer.parseInt(numBinomial.getText().toString()) > experiment.getMaxTrials()){
                                        Toast.makeText(UploadTrial.this, "You cannot add more trials than the maximum trials for this experiment at once", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    else{
                                        db
                                                .collection("Experiments")
                                                .document(experiment.getExpID())
                                                .collection("Trials")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            int count = 0;
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                count+=1;
                                                            }

                                                            if ( count+Integer.parseInt(numBinomial.getText().toString()) > experiment.getMaxTrials() ){
                                                                Toast.makeText(UploadTrial.this, "You cannot add more trials than the maximum trials for this experiment at once", Toast.LENGTH_SHORT).show();
                                                            }
                                                            else{
                                                                for (int i = 0; i < Integer.parseInt(numBinomial.getText().toString()); i++) {
                                                                    Trial trial = new Trial(experiment.getRequireLocation(),
                                                                            experiment.getTrialType(),
                                                                            false,
                                                                            experiment.getOwnerUserID(),
                                                                            UUID.randomUUID().toString(),
                                                                            stringDate.getShortDate(),
                                                                            experiment.getRequireLocation() ? latlng : null);
                                                                    collectionRefToDB(trial, experiment);
                                                                }
                                                            }
//                                                            recreate();
                                                        }
                                                        recreate();
                                                    }
                                                });
                                    }

//                                    recreate();
                                    dialog.cancel();
                                }
                            })
                            .setPositiveButton("Cancel", null).create().show();

                }
                // 2nd case: if the experiment's trial type is count
                // Q1: how to take input?
                if (expType.matches("Count")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder((UploadTrial.this));
                    builder.setTitle("Add Count Trials?");
                    final EditText numCount = new EditText(UploadTrial.this);
                    // set input to be any integer
                    numCount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    numCount.setHint("Enter positive/negative number of counts");
                    builder.setView(numCount);

                    builder.setNegativeButton("Cancel", null)
                            .setPositiveButton("Add Trials", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (numCount.getText().toString().length() == 0) {
                                        Toast.makeText(UploadTrial.this, "Trial value field must not be empty", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    } else {
                                        Trial trial = new Trial(experiment.getRequireLocation(),
                                                experiment.getTrialType(),
                                                Integer.parseInt(numCount.getText().toString()),
                                                experiment.getOwnerUserID(),
                                                UUID.randomUUID().toString(),
                                                stringDate.getShortDate(),
                                                experiment.getRequireLocation() ? latlng : null);
                                        collectionRefToDB(trial, experiment);
                                        recreate();
                                    }
                                }
                            }).create().show();
                }
                // 3rd case: if the experiment's trial type is non-negative count
                // Q1: how to take input?
                if (expType.matches("Non-Negative Count")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder((UploadTrial.this));
                    builder.setTitle("Add Non-Negative Count Trials?");
                    final EditText numNonNegCount = new EditText(UploadTrial.this);
                    // set input to be integer and positives only
                    numNonNegCount.setInputType(InputType.TYPE_CLASS_NUMBER);
                    numNonNegCount.setHint("Enter a positive number");
                    builder.setView(numNonNegCount);

                    builder.setNegativeButton("Cancel", null)
                            .setPositiveButton("Add trials", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // include 0 as well
                                    if (Integer.parseInt(numNonNegCount.getText().toString()) >= 0) {
                                        // save the data
                                        Trial trial = new Trial(experiment.getRequireLocation(),
                                                experiment.getTrialType(),
                                                Integer.parseInt(numNonNegCount.getText().toString()),
                                                experiment.getOwnerUserID(),
                                                UUID.randomUUID().toString(),
                                                stringDate.getShortDate(),
                                                experiment.getRequireLocation() ? latlng : null);
                                        collectionRefToDB(trial, experiment);
                                    } else {
                                        Toast.makeText(UploadTrial.this, "Trial value field must not be empty", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                    recreate();
                                }
                            }).create().show();
                }
                // 4th case: if the experiment's trial type is measurement
                if (expType.matches("Measurement")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder((UploadTrial.this));
                    builder.setTitle("Add Measurement Trials?");
                    builder.setMessage("Enter measurement below:");
                    final EditText measurementInput = new EditText(UploadTrial.this);
                    measurementInput.setHint("In Integers or Floats");
                    // set input to be exclusively integer and decimal
                    measurementInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    builder.setView(measurementInput);
                    builder.setNegativeButton("Cancel", null)
                            .setPositiveButton("Add Trials", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // once click add trials, add the trial's outcome, location
                                    // Q1: check editText value
                                    // Q2: how to save information
                                    // check input for

                                    if (measurementInput.getText().toString().length() == 0) {
                                        Toast.makeText(UploadTrial.this, "Trial value field must not be empty", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    } else {
                                        // source: https://docs.oracle.com/javase/8/docs/api/java/math/BigDecimal.html
                                        // I have use the idea of BigDecimal object to handle float returning not exactly the same valye
                                        // by Alex Mak
                                        BigDecimal roundedVal = new BigDecimal(measurementInput.getText().toString());
                                        double finalVal = roundedVal.doubleValue();
                                        Trial trial = new Trial(experiment.getRequireLocation(),
                                                experiment.getTrialType(),
                                                finalVal,
                                                experiment.getOwnerUserID(),
                                                UUID.randomUUID().toString(),
                                                stringDate.getShortDate(),
                                                experiment.getRequireLocation() ? latlng : null);
                                        collectionRefToDB(trial, experiment);
                                        recreate();
                                    }
                                }
                            }).create().show();
            } }
        });
    }

    void collectionRefToDB(Trial trial, Experiment experiment){
        database.addTrialToDB(db
                .collection("Experiments")
                .document(experiment.getExpID())
                .collection("Trials")
                .document(trial.getTrialID()), trial);
    }

}





