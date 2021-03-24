package com.example.cmput301w21t23_smartdatabook.experimentDetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.comments.CommentActivity;
import com.example.cmput301w21t23_smartdatabook.Database;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Class:ExperimentDetails
 * This class shows the details of the experiment,it displays:
 * An experiment's minimum and maximum number of trials
 * An experiment's name and description
 * Radio button to choose which type of the trial the experiment has (binomial/ measurement/count/ non-negtaive count trials)
 * switch button that turns on/ off an experiment's trial location.
 * @author Afaq Nabi, Bosco Chan, Jayden
 * @version 1
 * @see Experiment ,
 */
public class ExperimentDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experiment_details);

        Database database = new Database();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        setSupportActionBar(findViewById(R.id.app_toolbar));
        ActionBar toolbar = getSupportActionBar();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        assert toolbar != null;
        toolbar.setDisplayHomeAsUpEnabled(true);

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

        TextView expDate = findViewById(R.id.ClickedExpdate);
        expDate.setText(experiment.getDate());

        TextView description = findViewById(R.id.ClickedExpDesc);
        description.setText(experiment.getDescription());

        TextView region = findViewById(R.id.ClickedExpRegion);
        region.setText("some location???");

        TextView minTrials = findViewById(R.id.MinTrials);
        minTrials.setText("Min Trials: "+ Integer.toString(experiment.getMinTrials()));

        TextView maxTrials = findViewById(R.id.MaxTrials);
        maxTrials.setText("Max Trials: "+Integer.toString(experiment.getMaxTrials()));

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
                // create new intent with this
                // https://stackoverflow.com/questions/30965292/cannot-resolve-constructor-android-intent
                Intent trialIntent;
                trialIntent = new Intent(ExperimentDetails.this, UploadTrial.class);
                trialIntent.putExtra("experiment", experiment); // pass experiment object
                startActivity(trialIntent);

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
                Intent intent = new Intent(getBaseContext(), CommentActivity.class);
                startActivity(intent);
            }
        });

        Button endExp = findViewById(R.id.endExp);
        endExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.publicOrEnd(db.collection("Experiments"), "On", experiment,"isEnd");
                database.publicOrEnd((db.collection("Users")
                        .document(currentUser.getUid())
                        .collection("Favorites")), "On", experiment,"IsEnd");
                endExp.setVisibility(View.INVISIBLE);
                Log.d("Tests", "value1: "+database.giveBoolean(database.giveString(experiment.getIsEnd())));
            }
        });

        CheckBox publish = findViewById(R.id.Publish);
        TextView publish_text = findViewById(R.id.Publish_text);
        publish.setChecked(experiment.isPublic());
        if (currentUser.getUid().equals(experiment.getOwnerUserID()) ){
//            endExp.setVisibility(View.VISIBLE);
            publish.setVisibility(View.VISIBLE);
            publish_text.setVisibility(View.VISIBLE);
            // doesn't work
            if (database.giveBoolean(database.giveString(experiment.getIsEnd()))){
                Log.d("Tests", "value2: "+database.giveBoolean(database.giveString(experiment.getIsEnd())));
                endExp.setVisibility(View.INVISIBLE);
            }
        }

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String onOff;
                if (publish.isChecked()){
                    onOff = "On";
                }
                else{
                    onOff = "Off";
                }
                Log.d("ONOFF", onOff);

                database.publicOrEnd(db.collection("Experiments"), onOff, experiment, "PublicStatus");
                database.publicOrEnd((db.collection("Users")
                        .document(currentUser.getUid())
                        .collection("Favorites")),onOff, experiment,"PublicStatus");
            }
        });
    }

}