package com.example.cmput301w21t23_smartdatabook.geolocation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * the map activity which will show the markers of all the expeirments and trials
 * @author Afaq Nabi
 * @ref https://www.geeksforgeeks.org/how-to-add-dynamic-markers-in-google-maps-with-firebase-firstore/
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseFirestore db;
    Experiment experiment;
    String main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // initializing our firebase firestore.
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        main = intent.getStringExtra("main");
        if (main.equals("false")){
            experiment = (Experiment) intent.getSerializableExtra("experiment");
        }

        // Obtain the SupportMapFragment and get
        // notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(main.equals("true")){
            db.collection("Experiments")
                    .whereEqualTo("requireLocation", "On")
                    .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Experiments", document.getId() + " => " + document.getData());
                            GeoPoint gp = document.getGeoPoint("Location");
                            assert gp != null;
                            LatLng location = new LatLng(gp.getLatitude(), gp.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(location).title("Marker"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                        }
                    }
                }
            });
        }
        else{
            db.collection("Experiments")
                .document(experiment.getExpID())
                .collection("Trials")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("Experiments", document.getId() + " => " + document.getData());
                                    GeoPoint gp = document.getGeoPoint("GeoPoint");
                                    assert gp != null;
                                    LatLng location = new LatLng(gp.getLatitude(), gp.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(location).title("Marker"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                                }
                            }
                        }
                    });
        }

        // creating a variable for document reference.
    }
}