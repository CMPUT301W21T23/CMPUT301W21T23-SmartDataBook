package com.example.cmput301w21t23_smartdatabook.experimentDetails;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.BuildConfig;
import com.example.cmput301w21t23_smartdatabook.QRCode.QRCodeActivity;
import com.example.cmput301w21t23_smartdatabook.maps.MapsActivity;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.example.cmput301w21t23_smartdatabook.comments.CommentActivity;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.trials.UploadTrial;
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
    User user = User.getUser();
    Database database = Database.getDataBase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experiment_details);

        setSupportActionBar(findViewById(R.id.app_toolbar));
        ActionBar toolbar = getSupportActionBar();
        assert toolbar != null;
        toolbar.setDisplayHomeAsUpEnabled(true);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        Experiment experiment = (Experiment) intent.getSerializableExtra("experiment"); // get the experiment object

        toolbar.setTitle(experiment.getExpName());

        View userInfoView = LayoutInflater.from(ExperimentDetails.this).inflate(R.layout.view_profile, null);

        TextView username = userInfoView.findViewById(R.id.expOwner);
        username.setText("Username: " + user.getUserName());

        TextView email = userInfoView.findViewById(R.id.expContact);;
        email.setText("Email: " + user.getUserContact());

        TextView Owner = findViewById(R.id.owner);
        Owner.setText(experiment.getOwnerUserName());
        Owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: new user details activity
                AlertDialog.Builder builder = new AlertDialog.Builder(ExperimentDetails.this);
                builder.setView(userInfoView)
                        .setNegativeButton("Close", null).create().show();
            }
        });

        TextView expDate = findViewById(R.id.ClickedExpdate);
        expDate.setText(experiment.getDate());

        TextView description = findViewById(R.id.ClickedExpDesc);
        description.setText(experiment.getDescription());

        TextView region = findViewById(R.id.ClickedExpRegion);
        region.setText("some location???");

        TextView minTrials = findViewById(R.id.MinTrials);
        minTrials.setText("Min Trials: " + Integer.toString(experiment.getMinTrials()));

        TextView maxTrials = findViewById(R.id.MaxTrials);
        maxTrials.setText("Max Trials: " + Integer.toString(experiment.getMaxTrials()));

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
                Intent intent = new Intent(ExperimentDetails.this, MapsActivity.class);
                intent.putExtra("experiemnt", experiment);
                startActivity(intent);
            }
        });

        TextView QRCode = findViewById(R.id.generateCodeTextView);
        QRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: new QRCode activity
                Intent intent = new Intent(ExperimentDetails.this, QRCodeActivity.class);
                intent.putExtra("experiment", experiment);
                startActivity(intent);
            }
        });

        TextView askQns = findViewById(R.id.askQuestionsTextView);
        askQns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CommentActivity.class);
                intent.putExtra("Experiment", experiment);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        Button endExp = findViewById(R.id.endExp);
        endExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!experiment.getIsEnd()) {
                    experiment.setEnd(true);
                    database.publicOrEnd(db.collection("Experiments"), "On", experiment, "isEnd");
                    database.publicOrEnd((db.collection("Users")
                            .document(user.getUserUniqueID())
                            .collection("Favorites")), "On", experiment, "isEnd");
                    Toast.makeText(getBaseContext(), "Experiment has been ended this action cannot be undone", Toast.LENGTH_SHORT).show();
                    Log.d("Tests", "value1: " + experiment.getIsEnd());
                } else {
                    Toast.makeText(getBaseContext(), "Experiment is ended already!!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        CheckBox publish = findViewById(R.id.Publish);
        TextView publish_text = findViewById(R.id.Publish_text);
        publish.setChecked(experiment.isPublic());
        if (user.getUserUniqueID().equals(experiment.getOwnerUserID())) {
            endExp.setVisibility(View.VISIBLE);
            publish.setVisibility(View.VISIBLE);
            publish_text.setVisibility(View.VISIBLE);

        }

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String onOff;
                if (publish.isChecked()) {
                    onOff = "On";
                } else {
                    onOff = "Off";
                }
                Log.d("ONOFF", onOff);

                database.publicOrEnd(db.collection("Experiments"), onOff, experiment, "PublicStatus");
                database.publicOrEnd((db.collection("Users")
                        .document(user.getUserUniqueID())
                        .collection("Favorites")), onOff, experiment, "PublicStatus");
            }
        });
    }

}