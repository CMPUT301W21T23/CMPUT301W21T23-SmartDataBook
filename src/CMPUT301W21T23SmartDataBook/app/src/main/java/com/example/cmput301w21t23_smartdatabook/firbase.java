package com.example.cmput301w21t23_smartdatabook;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class firbase {
    //Temporarily sign in user as an anonymous user on first sign in
    //Source: firebase guides, https://firebase.google.com
    //License: Creative Commons Attribution 4.0 License, Apache 2.0 License
    //Code: https://firebase.google.com/docs/auth/android/anonymous-auth?authuser=0
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        //Anonymously generated UUID is only persistent with each app instance, after app is uninstalled.
//        //A new UUID is generated to the user on first launch of the app
//        mAuth.signInAnonymously()
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("UserAuthSuccessful", "Login Successful.");
//                            Toast.makeText(MainActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
//
//                            FirebaseUser currentUser = mAuth.getCurrentUser();
//
//                            db = FirebaseFirestore.getInstance();
//                            // Get a top level reference to the collection
//                            final CollectionReference allUsersCollection = db.collection("Users");
//                            HashMap<String, String> data = new HashMap<>();
//
//                            //Source: Firebase, firebase.google.com
//                            //License: Creative Commons Attribution 4.0 License, Apache 2.0 License
//                            //Code: https://firebase.google.com/docs/firestore/query-data/get-data#java_
//                            assert currentUser != null;
//                            DocumentReference userDoc = allUsersCollection.document(currentUser.getUid());
//                            userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                    if(task.isSuccessful()){
//                                        DocumentSnapshot document = task.getResult();
//                                        assert document != null;
//                                        if(!document.exists()){
//                                            data.put("UserName", "");
//                                            data.put("Contact", "");
//                                            allUsersCollection
//                                                    .document(currentUser.getUid())
//                                                    .set(data)
//                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                        @Override
//                                                        public void onSuccess(Void aVoid) {
//                                                            Log.d("UsernameSaveSuccessful", "Username saved successfully.");
//                                                        }
//                                                    })
//                                                    .addOnFailureListener(new OnFailureListener() {
//                                                        @Override
//                                                        public void onFailure(@NonNull Exception e) {
//                                                            Log.d("UsernameSaveFailed", "Username saving failed.");
//                                                        }
//                                                    });
//                                        }
//                                    }
//                                }
//                            });
//
//                        }else{
//                            Log.w("UserAuthFailed", "Login Failed.");
//                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//
//    }//onStart
}
