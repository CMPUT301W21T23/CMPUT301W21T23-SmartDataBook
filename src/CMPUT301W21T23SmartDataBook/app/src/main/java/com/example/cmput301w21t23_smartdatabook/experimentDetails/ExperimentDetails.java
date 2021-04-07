package com.example.cmput301w21t23_smartdatabook.experimentDetails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.cmput301w21t23_smartdatabook.QRCode.QRCodeActivity;
import com.example.cmput301w21t23_smartdatabook.QRCode.ScannerActivity;
import com.example.cmput301w21t23_smartdatabook.StringDate;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.geolocation.MapsActivity;
import com.example.cmput301w21t23_smartdatabook.stats.StatsView;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.example.cmput301w21t23_smartdatabook.comments.CommentActivity;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.trials.UploadTrial;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

/**
 * Class:ExperimentDetails
 * This class shows the details of the experiment,it displays:
 * An experiment's minimum and maximum number of trials
 * An experiment's name and description
 * Radio button to choose which type of the trial the experiment has (binomial/ measurement/count/ non-negtaive count trials)
 * switch button that turns on/ off an experiment's trial location.
 * @author Afaq Nabi, Bosco Chan, Jayden
 * @version 1
 * @see Experiment
 */
public class ExperimentDetails extends AppCompatActivity {
    User user = User.getUser();
    Database database = Database.getDataBase();
    StringDate date = new StringDate();

    /**
     * onCreate method that sets up the experiment details page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize elements in the experiment details page
        setContentView(R.layout.experiment_details);

        setSupportActionBar(findViewById(R.id.app_toolbar));
        ActionBar toolbar = getSupportActionBar();
        assert toolbar != null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        Experiment experiment = (Experiment) intent.getSerializableExtra("experiment"); // get the experiment object

        toolbar.setTitle(experiment.getExpName());

        View userInfoView = LayoutInflater.from(ExperimentDetails.this).inflate(R.layout.view_profile, null);

        AppCompatImageButton scan = findViewById(R.id.scanner);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExperimentDetails.this, ScannerActivity.class);
                intent.putExtra("experiment", (Parcelable) experiment);
                intent.putExtra("Flag", "Scan");
                startActivity(intent);
            }
        });

        database.fillUserName(new GeneralDataCallBack() {
            @Override
            public void onDataReturn(Object returnedObject) {
                Hashtable<String, User> UserName = (Hashtable<String, User>) returnedObject;

                // Setting up visual representation(TextView, Button, checkbox) of experiment details page
                TextView username = userInfoView.findViewById(R.id.expOwner);
                username.setText("Username: " + UserName.get(experiment.getOwnerUserID()).getUserName());

                TextView email = userInfoView.findViewById(R.id.expContact);;
                email.setText("Email: " + UserName.get(experiment.getOwnerUserID()).getUserContact());

                TextView Owner = findViewById(R.id.owner);
                Owner.setText( UserName.get(experiment.getOwnerUserID()).getUserName() );
                Owner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Source: Johnny Five; https://stackoverflow.com/users/6325722/johnny-five
                        //Code: https://stackoverflow.com/questions/28071349/the-specified-child-already-has-a-parent-you-must-call-removeview-on-the-chil
                        if (userInfoView.getParent() != null) {
                            ((ViewGroup) userInfoView.getParent()).removeView(userInfoView);
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(ExperimentDetails.this);
                        builder.setView(userInfoView)
                                .setNegativeButton("Close", null).create().show();
                    }
                });
            }
        });


        TextView expDate = findViewById(R.id.ClickedExpdate);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//        try {
//            date = format.parse(experiment.getDate());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        expDate.setText(""+ date.getDate(experiment.getDate()));

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
                Intent intent = new Intent(getBaseContext(), StatsView.class);
                intent.putExtra("experiment", (Parcelable) experiment);
                startActivity(intent);
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
                trialIntent.putExtra("experiment", (Parcelable) experiment); // pass experiment object
                startActivity(trialIntent);

            }
        });

        TextView showMap = findViewById(R.id.ShowMap);
        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: new show map activity
                Intent intent = new Intent(ExperimentDetails.this, MapsActivity.class);
                intent.putExtra("experiment", (Parcelable) experiment);
                intent.putExtra("main", "false");
                startActivity(intent);
            }
        });

        TextView QRCode = findViewById(R.id.generateCodeTextView);
        QRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: new QRCode activity
                Intent intent = new Intent(ExperimentDetails.this, QRCodeActivity.class);
                intent.putExtra("experiment", (Parcelable) experiment);
                startActivity(intent);
            }
        });

        TextView askQns = findViewById(R.id.askQuestionsTextView);
        askQns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CommentActivity.class);
                intent.putExtra("Experiment", (Parcelable) experiment);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        TextView endExp = findViewById(R.id.endExp);
        String title;
        if (!experiment.getIsEnd()) {
            title = "Archive";
            upload.setVisibility(View.VISIBLE);
        } else {
            title = "Un-Archive";
            upload.setVisibility(View.INVISIBLE);
        }
        endExp.setText(title);
        if (user.getUserUniqueID().equals(experiment.getOwnerUserID())){
            endExp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(ExperimentDetails.this);
                    builder.setTitle(title);
                    builder.setMessage("Do you wish to " + title + " this experiment ?");
                    builder.setNegativeButton("Cancel", null)
                            .setPositiveButton(title + " Experiment", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!experiment.getIsEnd()) {
                                        experiment.setEnd(true);
                                        database.publicOrEnd(db.collection("Experiments"), "On", experiment, "isEnd");
                                        database.publicOrEnd((db.collection("Users")
                                                .document(user.getUserUniqueID())
                                                .collection("Favorites")), "On", experiment, "isEnd");
                                        database.addExperimentToDB(experiment, db.collection("Archived"), experiment.getOwnerUserID());
                                        database.deleteFromDB(db.collection("Experiments").document(experiment.getExpID()));

                                    } else {
                                        experiment.setEnd(false);
                                        database.publicOrEnd(db.collection("Experiments"), "Off", experiment, "isEnd");
                                        database.publicOrEnd((db.collection("Users")
                                                .document(user.getUserUniqueID())
                                                .collection("Favorites")), "Off", experiment, "isEnd");
                                        database.addExperimentToDB(experiment, db.collection("Experiments"), experiment.getOwnerUserID());
                                        database.deleteFromDB(db.collection("Archived").document(experiment.getExpID()));
                                    }

                                    Toast.makeText(getBaseContext(), "Experiment has been " + title, Toast.LENGTH_SHORT).show();

                                }//onClick
                            })
                            .create().show();
                }
            });
        }

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

                database.publicOrEnd(db.collection("Experiments"), onOff, experiment, "PublicStatus");
                database.publicOrEnd((db.collection("Users")
                        .document(user.getUserUniqueID())
                        .collection("Favorites")), onOff, experiment, "PublicStatus");
            }
        });
    }

}
