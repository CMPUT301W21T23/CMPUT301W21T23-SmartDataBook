package com.example.cmput301w21t23_smartdatabook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

/**
 * Class:ExperimentDetails
 * This class shows the details of the experiment,it displays:
 * An experiment's minimum and maximum number of trials
 * An experiment's name and description
 * Radio button to choose which type of the trial the experiment has (binomial/ measurement/count/ non-negtaive count trials)
 * switch button that turns on/ off an experiment's trial location.
 * @author Afaq Nabi, Bosco Chan
 * @version 1
 * @see Experiment, 
 */
public class ExperimentDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experiment_details);

        ActionBar toolbar  = getSupportActionBar();

        Intent intent = getIntent();
        Experiment experiment = (Experiment) intent.getSerializableExtra("experiment"); // get the experiment object

        toolbar.setTitle(experiment.getExpName());

        TextView Owner = findViewById(R.id.owner);
        Owner.setText(experiment.getOwnerUserID());
        Owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: new user details activity
            }
        });

        TextView expName = findViewById(R.id.ClickedExpName);
        expName.setText(experiment.getExpName());

        TextView region = findViewById(R.id.ClickedExpRegion);
        region.setText("some location???");

        TextView minTrials = findViewById(R.id.MinTrials);
        minTrials.setText(experiment.getMinTrials());

        TextView maxTrials = findViewById(R.id.MaxTrials);
        maxTrials.setText(experiment.getMaxTrials());

        TextView type = findViewById(R.id.ExpType);
        type.setText(experiment.getTrialType());

        Button viewStats = findViewById(R.id.ViewStatsBTN);
        viewStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: new stats activity
            }
        });

        Button upload = findViewById(R.id.UploadTrialsBTN);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: new upload activity
            }
        });

        TextView showMap = findViewById(R.id.ShowMap);
        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: new show map activity
            }
        });

        TextView QRCode = findViewById(R.id.generateCodeTextView);
        QRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: new QRCode activity
            }
        });

        TextView askQns = findViewById(R.id.askQuestionsTextView);
        askQns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: new comments activity
            }
        });




    }

}
