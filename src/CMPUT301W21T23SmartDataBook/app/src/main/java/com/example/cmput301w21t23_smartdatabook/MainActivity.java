package com.example.cmput301w21t23_smartdatabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cmput301w21t23_smartdatabook.fav.favPage;
import com.example.cmput301w21t23_smartdatabook.settings.settingsPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.cmput301w21t23_smartdatabook.home.homePage;

//https://androidwave.com/bottom-navigation-bar-android-example/ -> bottom tab navigation
public class MainActivity extends AppCompatActivity {

    ListView experimentList;
    ArrayAdapter<Experiment> experimentAdapter;
    ArrayList<Experiment> experimentDataList;

    BottomNavigationView bottomNavigation;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Home");
        openFragment(homePage.newInstance("",""));

        //anonymous authentication testing
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Authentication Success", "signInAnonymously:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();

                            Toast.makeText(MainActivity.this, "User Authenticated.", Toast.LENGTH_SHORT).show();

                            db = FirebaseFirestore.getInstance();
                            // Get a top level reference to the collection
                            final CollectionReference allUsersCollection = db.collection("Users");
                            HashMap<String, String> data = new HashMap<>();

                            //Source: Firebase, firebase.google.com
                            //License: Creative Commons Attribution 4.0 License, Apache 2.0 License
                            //Code: https://firebase.google.com/docs/firestore/query-data/get-data#java_
                            assert currentUser != null;
                            DocumentReference userDoc = allUsersCollection.document(currentUser.getUid());
                            userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        assert document != null;
                                        if(!document.exists()){
                                            data.put("UserName", "");
                                            data.put("Contact", "");
                                            allUsersCollection
                                                    .document(currentUser.getUid())
                                                    .set(data)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("UsernameSaveSuccessful", "Username saved successfully: "+ mAuth.getUid());
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d("UsernameSaveFailed", "Username saving failed.");
                                                        }
                                                    });
                                        }
                                    }
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Authentication Failed", "signInAnonymously:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }//onCreate
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_nav:
                    toolbar.setTitle("Home");
                    openFragment(homePage.newInstance("",""));
                    return true;

                case R.id.fav_nav:
                    toolbar.setTitle("Favorites");
                    openFragment(favPage.newInstance("",""));
                    return true;

                case R.id.settings_nav:
                    toolbar.setTitle("Settings");
                    openFragment(settingsPage.newInstance("",""));
                    return true;
            }
            return false;
        }
    };


}