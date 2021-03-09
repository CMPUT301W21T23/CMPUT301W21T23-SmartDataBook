package com.example.cmput301w21t23_smartdatabook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView experimentList;
    ArrayAdapter<Experiment> experimentAdapter;
    ArrayList<Experiment> experimentDataList;

    private FusedLocationProviderClient fusedLocationClient;
    // https://stackoverflow.com/questions/44992014/how-to-get-current-location-in-googlemap-using-fusedlocationproviderclient
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location lastLocation = locationResult.getLastLocation();
            System.out.println(lastLocation);
        }
    };

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


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        final Button location_button = findViewById(R.id.location_test);

        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 78);
                } else {
                    fusedLocationClient.requestLocationUpdates(LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY), mLocationCallback, Looper.myLooper());
                    // Below method executes when location is successfully obtained, deprecated due to above mLocationCallback
//                    fusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            // Got last known location. In some rare situations this can be null.
//                            if (location != null) {
//                                System.out.println(location.toString());
//                            } else {
//                                System.out.println("Location do not exist yet. Maybe something wrong?");
//                            }
//                        }
//                    });
                }
            }
        });


    }

}