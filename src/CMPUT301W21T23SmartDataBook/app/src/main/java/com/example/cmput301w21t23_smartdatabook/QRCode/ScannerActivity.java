package com.example.cmput301w21t23_smartdatabook.QRCode;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.experiment.Experiment;
import com.example.cmput301w21t23_smartdatabook.geolocation.LocationWithPermission;
import com.example.cmput301w21t23_smartdatabook.stats.StringDate;
import com.example.cmput301w21t23_smartdatabook.trials.Trial;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;
import java.util.UUID;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * this class will use the emulators camera to scan the barcode using the Zxing librabry
 * once scanned the rawResult will be added to the database
 * references: https://www.youtube.com/watch?v=AiNi9K94W5c&ab_channel=MdJamal
 *
 * @author Afaq
 */
public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView scannerView;
    Database database = Database.getDataBase();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StringDate stringDate = new StringDate();
    Experiment experiment;
    User user = User.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        //Source: Opeyemi, https://stackoverflow.com/users/8226150/opeyemi
                        //Code: https://stackoverflow.com/questions/50639292/detecting-wether-a-permission-can-be-requested-or-is-permanently-denied
                        if (permissionDeniedResponse.isPermanentlyDenied()) {
                            //permission is permanently denied navigate to user setting
                            new AlertDialog.Builder(ScannerActivity.this)
                                    .setTitle("Camera permission was denied permanently.")
                                    .setMessage("Allow Camera access through your settings.")
                                    .setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                            intent.setData(uri);
                                            startActivityForResult(intent, 101);
                                            dialog.cancel();
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                    finish();
                                                }
                                            }
                                    ).show();

                        } else {
                            finish();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.e("format", rawResult.getBarcodeFormat().toString());
        Intent intent = getIntent();
        experiment = (Experiment) intent.getSerializableExtra("experiment");
        String type = intent.getStringExtra("Flag");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(location -> {
                if (location == null) {
                    new LocationWithPermission(this).requestLocationUpdate();
                    Toast.makeText(this, "Preparing location.. Please wait up to 10 seconds and try again.", Toast.LENGTH_LONG).show();
                    return;
                }
                LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                if (type.equals("Scan")) {
                    if (!experiment.getIsEnd()) {
                        if (rawResult.getBarcodeFormat().toString().contains("QR_CODE")) {
                            QRCodeScanned(rawResult.toString(), latlng);

                        } else {
                            BarcodeScanned(rawResult.toString(), experiment, latlng);
                        }
                    } else {
                        onBackPressed();
                        Toast.makeText(getBaseContext(), "Trial has been Archived new trials cannot be added", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    registerBarcode(rawResult.toString(), experiment);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    // if a barcode or anything other than a QR code is scanned for th epurpose of
    // registering it to a trial for an experiment
    private void registerBarcode(String rawResult, Experiment experiment) {
        View trialView = LayoutInflater.from(ScannerActivity.this).inflate(R.layout.barcode, null);
        EditText value = trialView.findViewById(R.id.value);
        TextView trueFalse = trialView.findViewById(R.id.true_false);
        Switch switchTF = trialView.findViewById(R.id.switchTF);

        int inputType = InputType.TYPE_CLASS_NUMBER;
        if (experiment.getTrialType().equals("Count")) {
            inputType += InputType.TYPE_NUMBER_FLAG_SIGNED;
        } else if (experiment.getTrialType().equals("Measurement")) {
            inputType += InputType.TYPE_NUMBER_FLAG_DECIMAL;
        } else if (experiment.getTrialType().equals("Binomial")) {
            trueFalse.setVisibility(View.VISIBLE);
            switchTF.setVisibility(View.VISIBLE);
        }
        value.setInputType(inputType);

        switchTF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchTF.isChecked()) {
                    trueFalse.setText("True");
                } else {
                    trueFalse.setText("False");
                }
            }
        });

        db.collection("Barcode")
                .document(experiment.getExpID())
                .collection(user.getUserUniqueID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (rawResult.equals(document.get("RawResult"))) {
                                    onBackPressed();
                                    Toast.makeText(getBaseContext(), "Barcode already exists for this experiment", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(ScannerActivity.this);
                            builder.setTitle("Enter value for trial")
                                    .setView(trialView)
                                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            onBackPressed();
                                        }
                                    })
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (value.getText().toString().equals("")) {
                                                onBackPressed();
                                                Toast.makeText(getBaseContext(), "You must enter a value, Try Again", Toast.LENGTH_SHORT).show();
                                            } else {
                                                HashMap<String, Object> data = new HashMap<>();
                                                data.put("RawResult", rawResult);
                                                data.put("UUID", user.getUserUniqueID());
                                                data.put("ExpID", experiment.getExpID());
                                                if (experiment.getTrialType().equals("Binomial")) {
                                                    data.put("Bool", switchTF.isChecked());
                                                    data.put("Value", Integer.parseInt(value.getText().toString()));
                                                    if (Integer.parseInt(value.getText().toString()) > experiment.getMaxTrials()) {
                                                        onBackPressed();
                                                        Toast.makeText(getBaseContext(), "Cannot use value greater than max #'s of trial", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                } else if (experiment.getTrialType().equals(("Measurement"))) {
                                                    data.put("Value", Float.parseFloat(value.getText().toString()));
                                                } else {
                                                    data.put("Value", Integer.parseInt(value.getText().toString()));
                                                }

                                                db.collection("Barcode")
                                                        .document(experiment.getExpID())
                                                        .collection(user.getUserUniqueID())
                                                        .document(UUID.randomUUID().toString())
                                                        .set(data);
                                                dialog.dismiss();
                                                onBackPressed();
                                            }
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
                });
    }

    // if a QR code is scanned this function is called
    private void QRCodeScanned(String rawResult, LatLng latlng) {
        String[] values = rawResult.split(",");
        if (experiment.getExpID().equals(values[0])) {
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
                                    count += 1;
                                }
                                if (count >= experiment.getMaxTrials()) {
                                    onBackPressed();
                                    Toast.makeText(getBaseContext(), "You cannot add more trials than the maximum trials for this experiment at once", Toast.LENGTH_LONG).show();
                                } else {
                                    if (values[3].equals("Binomial")) {
                                        //Need to add in given number of binomial trials
                                        if (count + Integer.parseInt(String.valueOf((values[2]))) > experiment.getMaxTrials()) {
                                            onBackPressed();
                                            Toast.makeText(getBaseContext(), "You cannot add more trials than the maximum trials for this experiment at once", Toast.LENGTH_LONG).show();
                                        } else {
                                            for (int i = 1; i <= Integer.parseInt(values[2]); i++) {
                                                Trial trial = new Trial(Boolean.parseBoolean(values[4]),
                                                        values[3],
                                                        Boolean.parseBoolean(values[5]),
                                                        values[1],
                                                        UUID.randomUUID().toString(),
                                                        stringDate.getCurrentDate(),
                                                        experiment.getRequireLocation() ? latlng : null);
                                                database.addTrialToDB(db.collection("Experiments")
                                                        .document(values[0])
                                                        .collection("Trials")
                                                        .document(trial.getTrialID()), trial);
                                            }
                                        }
                                    } else {
                                        Trial trial;
                                        if (values[3].equals("Measurement")) {
                                            trial = new Trial(Boolean.parseBoolean(values[4]),
                                                    values[3],
                                                    Float.parseFloat(values[2]),
                                                    values[1],
                                                    UUID.randomUUID().toString(),
                                                    stringDate.getCurrentDate(),
                                                    experiment.getRequireLocation() ? latlng : null);
                                        } else {
                                            trial = new Trial(Boolean.parseBoolean(values[4]),
                                                    values[3],
                                                    Integer.parseInt(values[2]),
                                                    values[1],
                                                    UUID.randomUUID().toString(),
                                                    stringDate.getCurrentDate(),
                                                    experiment.getRequireLocation() ? latlng : null);
                                        }

                                        database.addTrialToDB(db.collection("Experiments")
                                                .document(values[0])
                                                .collection("Trials")
                                                .document(trial.getTrialID()), trial);
                                    }
                                }
                                onBackPressed();
                            }
                        }
                    });
        } else {
            onBackPressed();
            Toast.makeText(getBaseContext(), "Invalid QR Code", Toast.LENGTH_SHORT).show();
        }

    }

    // if barcode is scanned for the purpose of adding a trial to the experiment
    private void BarcodeScanned(String rawResult, Experiment experiment, LatLng latlng) {
        db.collection("Barcode")
                .document(experiment.getExpID())
                .collection(user.getUserUniqueID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean found = false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (rawResult.equals(document.get("RawResult"))) {
                                    found = true;
                                    db
                                            .collection("Experiments")
                                            .document(experiment.getExpID())
                                            .collection("Trials")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        // count the current number of trials in the db
                                                        int count = 0;
                                                        for (QueryDocumentSnapshot document1 : task.getResult()) {
                                                            count += 1;
                                                        }

                                                        // if the # of trials has reached its limit don't do anything
                                                        if (count >= experiment.getMaxTrials()) {
                                                            onBackPressed();
                                                            Toast.makeText(getBaseContext(), "Reached Max Trials", Toast.LENGTH_SHORT).show();
                                                        }
                                                        // else proceed to add the trial to the db
                                                        else {
                                                            if (experiment.getTrialType().equals("Binomial")) {
                                                                if ((count + Integer.parseInt(String.valueOf(document.get("Value")))) > experiment.getMaxTrials()) {
                                                                    onBackPressed();
                                                                } else {
                                                                    for (int i = 1; i <= Integer.parseInt(document.get("Value").toString()); i++) {
                                                                        Trial trial = new Trial(experiment.getRequireLocation(),
                                                                                experiment.getTrialType(),
                                                                                document.get("Bool"),
                                                                                user.getUserUniqueID(),
                                                                                UUID.randomUUID().toString(),
                                                                                stringDate.getCurrentDate(),
                                                                                experiment.getRequireLocation() ? latlng : null);

                                                                        database.addTrialToDB(db
                                                                                .collection("Experiments")
                                                                                .document(experiment.getExpID())
                                                                                .collection("Trials")
                                                                                .document(trial.getTrialID()), trial);
                                                                    }
                                                                }
                                                            } else {
                                                                Trial trial = new Trial(experiment.getRequireLocation(),
                                                                        experiment.getTrialType(),
                                                                        document.get("Value"),
                                                                        user.getUserUniqueID(),
                                                                        UUID.randomUUID().toString(),
                                                                        stringDate.getCurrentDate());

                                                                database.addTrialToDB(db
                                                                        .collection("Experiments")
                                                                        .document(experiment.getExpID())
                                                                        .collection("Trials")
                                                                        .document(trial.getTrialID()), trial);
                                                            }
                                                        }
                                                        onBackPressed();
                                                    }
                                                }
                                            });
                                }
                            }
                            if (!found) {
                                onBackPressed();
                                Toast.makeText(getBaseContext(), "Invalid Barcode", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            onBackPressed();
                            Toast.makeText(getBaseContext(), "Invalid Barcode", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}


