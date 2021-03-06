package com.example.cmput301w21t23_smartdatabook.experiment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.comments.CommentActivity;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.geolocation.MapsActivity;
import com.example.cmput301w21t23_smartdatabook.stats.StatsView;
import com.example.cmput301w21t23_smartdatabook.stats.StringDate;
import com.example.cmput301w21t23_smartdatabook.trials.UploadTrial;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Hashtable;

/**
 * Class:ExperimentDetails
 * This class shows the details of the experiment,it displays:
 * An experiment's minimum and maximum number of trials
 * An experiment's name and description
 * Radio button to choose which type of the trial the experiment has (binomial/ measurement/count/ non-negtaive count trials)
 * switch button that turns on/ off an experiment's trial location.
 *
 * @author Afaq Nabi, Bosco Chan, Jayden Cho
 * @version 1
 * @see Experiment
 */
public class ExperimentDetails extends AppCompatActivity {
    User user = User.getUser();
    Database database = Database.getDataBase();
    StringDate date = new StringDate();

    /**
     * onCreate method that sets up the experiment details page
     *
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

        toolbar.setTitle("Experiment: " + experiment.getExpName());

        View userInfoView = LayoutInflater.from(ExperimentDetails.this).inflate(R.layout.view_profile, null);

        AppCompatImageButton scan = findViewById(R.id.scannerimg);
        TextView scanTV = findViewById(R.id.scanner_tv);
        if (experiment.getIsEnd()) {
            scan.setVisibility(View.INVISIBLE);
            scanTV.setVisibility(View.INVISIBLE);
        } else {
            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ExperimentDetails.this, ScannerActivity.class);
                    intent.putExtra("experiment", (Parcelable) experiment);
                    intent.putExtra("Flag", "Scan");
                    startActivity(intent);
                }
            });
        }

        database.fillUserName(new GeneralDataCallBack() {
            @Override
            public void onDataReturn(Object returnedObject) {
                Hashtable<String, User> UserName = (Hashtable<String, User>) returnedObject;

                // Setting up visual representation(TextView, Button, checkbox) of experiment details page
                TextView username = userInfoView.findViewById(R.id.expOwner);
                username.setText("Username: " + UserName.get(experiment.getOwnerUserID()).getUserName());

                TextView email = userInfoView.findViewById(R.id.expContact);
                email.setText("Email: " + UserName.get(experiment.getOwnerUserID()).getUserContact());

                TextView Owner = findViewById(R.id.owner);
                Owner.setText(UserName.get(experiment.getOwnerUserID()).getUserName());
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
        expDate.setText("" + date.getDate(experiment.getDate()));

        TextView description = findViewById(R.id.ClickedExpDesc);
        description.setText("Description: " + experiment.getDescription());

        TextView minTrials = findViewById(R.id.MinTrials);
        minTrials.setText("Min Trials: " + experiment.getMinTrials());

        TextView maxTrials = findViewById(R.id.MaxTrials);
        maxTrials.setText("Max Trials: " + experiment.getMaxTrials());

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
                // Source:  krupal-shah; https://stackoverflow.com/users/3830694/krupal-shah
                // https://stackoverflow.com/questions/30965292/cannot-resolve-constructor-android-intent
                Intent trialIntent;
                trialIntent = new Intent(ExperimentDetails.this, UploadTrial.class);
                trialIntent.putExtra("experiment", (Parcelable) experiment); // pass experiment object
                startActivity(trialIntent);

            }
        });

        TextView showMap = findViewById(R.id.ShowMap);
        AppCompatImageButton map = findViewById(R.id.mapButton);
        if (!experiment.getRequireLocation()) {
            showMap.setVisibility(View.INVISIBLE);
            map.setVisibility(View.INVISIBLE);
        } else {
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
        }


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
        AppCompatImageButton arch = findViewById(R.id.endExpImageView);
        String title;

        if (user.getUserUniqueID().equals(experiment.getOwnerUserID())) {
            if (!experiment.getIsEnd()) {
                title = "Archive";
            } else {
                title = "Un-Archive";
            }
            upload.setVisibility(View.VISIBLE);
            arch.setVisibility(View.VISIBLE);
            endExp.setText(title);

            endExp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ExperimentDetails.this);
                    builder.setTitle(title);
                    builder.setMessage("Do you wish to " + title + " this experiment ?");
                    builder.setNegativeButton("Cancel", null)
                            .setPositiveButton(title + " Experiment", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!experiment.getIsEnd()) {
                                        experiment.setEnd(true);
                                        database.publicOrEnd(db.collection("Experiments"), true, experiment, "isEnd");
                                        database.publicOrEnd((db.collection("Users")
                                                .document(user.getUserUniqueID())
                                                .collection("Favorites")), true, experiment, "isEnd");
                                        database.addExperimentToDB(experiment, db.collection("Archived"), experiment.getOwnerUserID());
                                        database.deleteFromDB(db.collection("Experiments").document(experiment.getExpID()));

                                    } else {
                                        experiment.setEnd(false);
                                        database.publicOrEnd(db.collection("Experiments"), false, experiment, "isEnd");
                                        database.publicOrEnd((db.collection("Users")
                                                .document(user.getUserUniqueID())
                                                .collection("Favorites")), false, experiment, "isEnd");
                                        database.addExperimentToDB(experiment, db.collection("Experiments"), experiment.getOwnerUserID());
                                        database.deleteFromDB(db.collection("Archived").document(experiment.getExpID()));
                                    }

                                    onBackPressed();

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
            if (!experiment.getIsEnd()) {
                publish.setVisibility(View.VISIBLE);
                publish_text.setVisibility(View.VISIBLE);
            }
        }

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean onOff;
                onOff = publish.isChecked();

                database.publicOrEnd(db.collection("Experiments"), onOff, experiment, "PublicStatus");
                database.publicOrEnd((db.collection("Users")
                        .document(user.getUserUniqueID())
                        .collection("Favorites")), onOff, experiment, "PublicStatus");
            }
        });
    }

}
