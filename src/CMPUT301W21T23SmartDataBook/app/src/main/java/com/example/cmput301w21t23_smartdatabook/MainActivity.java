package com.example.cmput301w21t23_smartdatabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
<<<<<<< Updated upstream
import android.view.View;
import android.widget.AdapterView;
=======
import android.view.MenuItem;
>>>>>>> Stashed changes
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

<<<<<<< Updated upstream
=======
import com.example.cmput301w21t23_smartdatabook.fav.favPage;
import com.example.cmput301w21t23_smartdatabook.settings.settingsPage;
>>>>>>> Stashed changes
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
<<<<<<< Updated upstream
=======
import com.google.android.material.bottomnavigation.BottomNavigationView;
>>>>>>> Stashed changes
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
<<<<<<< Updated upstream
=======
import java.util.ArrayList;
import java.util.HashMap;
>>>>>>> Stashed changes

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView experimentList;
    ArrayAdapter<Experiment> experimentAdapter;
    ArrayList<Experiment> experimentDataList;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //anonymous authentication testing
        mAuth = FirebaseAuth.getInstance();

        //experimentList testing
        experimentList = findViewById(R.id.experimentList);
        experimentDataList = new ArrayList<>();

<<<<<<< Updated upstream
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
=======
        openFragment(homePage.newInstance("",""));

        //anonymous authentication testing
        mAuth = FirebaseAuth.getInstance();
        //Toast.makeText(MainActivity.this, "Passed", Toast.LENGTH_SHORT).show();
        if (mAuth.getCurrentUser() == null){
            signInNewAnon();
        }
>>>>>>> Stashed changes

    }//onCreate

<<<<<<< Updated upstream
    //Temporarily sign in user as an anonymous user on first sign in
    //Source: firebase guides, https://firebase.google.com
    //License: Creative Commons Attribution 4.0 License, Apache 2.0 License
    //Code: https://firebase.google.com/docs/auth/android/anonymous-auth?authuser=0
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
=======
    /**
     * Sign in new anonymous user depending iff the user doesn't exist in the database
     */
    public void signInNewAnon() {
        //Temporarily sign in user as an anonymous user on first sign in
        //Source: firebase guides, https://firebase.google.com
        //License: Creative Commons Attribution 4.0 License, Apache 2.0 License
        //Code: https://firebase.google.com/docs/auth/android/anonymous-auth?authuser=0
>>>>>>> Stashed changes

        //Anonymously generated UUID is only persistent with each app instance, after app is uninstalled.
        //A new UUID is generated to the user on first launch of the app
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("UserAuthSuccessful", "Login Successful.");
                            Toast.makeText(MainActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();

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
                                            data.put("Contact", "");
                                            allUsersCollection
                                                    .document(currentUser.getUid())
                                                    .set(data)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("UsernameSaveSuccessful", "Username saved successfully.");
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

                        }else{
                            Log.w("UserAuthFailed", "Login Failed.");
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

<<<<<<< Updated upstream
    }//onStart
=======
    }//signInNewAnon

>>>>>>> Stashed changes
}