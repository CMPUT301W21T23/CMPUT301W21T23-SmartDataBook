package com.example.cmput301w21t23_smartdatabook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView experimentList;
    ArrayAdapter<Experiment> experimentAdapter;
    ArrayList<Experiment> experimentDataList;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        experimentList = findViewById(R.id.experimentList);
        experimentDataList = new ArrayList<>();

        experimentDataList.add(new Experiment("first", "123",
                "Binomial", "testtrial", false, 30, 60, true, "03/05/2021"));
        experimentDataList.add(new Experiment("second", "123",
                "Binomial", "testtrial", false, 30, 60, true, "03/05/2021"));
        experimentDataList.add(new Experiment("third", "123",
                "Binomial", "testtrial", false, 30, 60, true, "03/05/2021"));

        experimentAdapter = new CardList(this, experimentDataList);

        experimentList.setAdapter(experimentAdapter);

        FusedLocationProviderClient client =
                LocationServices.getFusedLocationProviderClient(this);

        // Get the last known location. In some rare situations, this can be null.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 78);
        }
        client.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        System.out.println(location.toString());
                    } else {
                        System.out.println("Location do not exist yet. Maybe something wrong?");
                    }
                });
    }
}