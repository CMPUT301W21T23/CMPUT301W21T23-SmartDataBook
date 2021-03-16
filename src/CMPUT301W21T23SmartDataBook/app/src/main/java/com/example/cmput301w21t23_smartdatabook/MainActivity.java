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
=======
import android.view.MenuItem;

import com.example.cmput301w21t23_smartdatabook.fav.FavPage;
import com.example.cmput301w21t23_smartdatabook.home.HomePage;
import com.example.cmput301w21t23_smartdatabook.settings.SettingsPage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * the Purpose of this class is to register the user in the database and initialize the bottom tab navigation
 * functionality of the app
 * the bottom tab should get covered if a new acitivity is opened
 * @Author Afaq
 * @Refrences https://androidwave.com/bottom-navigation-bar-android-example/
 */
public class MainActivity extends AppCompatActivity {

    ListView experimentList;
    ArrayAdapter<Experiment> experimentAdapter;
    ArrayList<Experiment> experimentDataList;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    BottomNavigationView bottomNavigation;

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //anonymous authentication testing
        mAuth = FirebaseAuth.getInstance();

        //experimentList testing
        experimentList = findViewById(R.id.experimentList);
        experimentDataList = new ArrayList<>();

        experimentDataList.add(new Experiment("first", "123",
                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));
        experimentDataList.add(new Experiment("second", "123",
                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));
        experimentDataList.add(new Experiment("third", "123",
                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));

        experimentAdapter = new CardList(this, experimentDataList);

        experimentList.setAdapter(experimentAdapter);

        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, CommentActivity.class);
                    startActivity(intent);
                }
            });
        openFragment(homePage.newInstance("",""));

        //anonymous authentication testing
        mAuth = FirebaseAuth.getInstance();
        //Toast.makeText(MainActivity.this, "Passed", Toast.LENGTH_SHORT).show();
        if (mAuth.getCurrentUser() == null){
            signInNewAnon();
        }

    }//onCreate

    /**
     * Sign in new anonymous user depending iff the user doesn't exist in the database
     */
    public void signInNewAnon() {
        //Temporarily sign in user as an anonymous user on first sign in
        //Source: firebase guides, https://firebase.google.com
        //License: Creative Commons Attribution 4.0 License, Apache 2.0 License
        //Code: https://firebase.google.com/docs/auth/android/anonymous-auth?authuser=0

        //Anonymously generated UUID is only persistent with each app instance, after app is uninstalled.
        //A new UUID is generated to the user on first launch of the app
        toolbar = getSupportActionBar();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Home");
        openFragment(HomePage.newInstance("",""));

//        mAuth = FirebaseAuth.getInstance();
//        authenticateAnon();
        Database dataBase = new Database();
        dataBase.authenticateAnon();

    } //onCreate

    public void openFragment(Fragment fragment) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
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
                    openFragment(HomePage.newInstance("",""));
                    return true;

                case R.id.fav_nav:
                    toolbar.setTitle("Favorites");
                    openFragment(FavPage.newInstance("",""));
                    return true;

                case R.id.settings_nav:
                    toolbar.setTitle("Settings");
                    openFragment(SettingsPage.newInstance("",""));
                    return true;
            }
            return false;
        }
    };

    /**
     * Authenticates a new app user anonymously and generates a "User document" for the user
     * containing their respective "username and contact".
     */
    /*
    public void authenticateAnon() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Authentication Success", "signInAnonymously:success: " + mAuth.getUid());
                            FirebaseUser currentUser = mAuth.getCurrentUser();

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
                                            data.put("Email", "");
                                            data.put("UUID", currentUser.getUid());

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

    }//onStart

    }//signInNewAnon
}
                    }
                });
    }//authenticationAnon
    */
}//mainActivity

