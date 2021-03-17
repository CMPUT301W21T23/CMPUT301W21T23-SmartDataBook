package com.example.cmput301w21t23_smartdatabook;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Database {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //Temporarily sign in user as an anonymous user on first sign in
    //Source: firebase guides, https://firebase.google.com
    //License: Creative Commons Attribution 4.0 License, Apache 2.0 License
    //Code: https://firebase.google.com/docs/auth/android/anonymous-auth?authuser=0
    /**
     * Authenticates a new app user anonymously and generates a "User document" for the user
     * containing their respective "username and contact".
     */
    public void authenticateAnon() {
        mAuth.signInAnonymously()
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Authentication Success", "signInAnonymously:success: " + mAuth.getUid());
                            FirebaseUser currentUser = mAuth.getCurrentUser();

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        assert document != null;
                                        if (!document.exists()) {
                                            data.put("UserName", "");
                                            data.put("Email", "");
                                            data.put("UUID", currentUser.getUid());

                                            allUsersCollection
                                                    .document(currentUser.getUid())
                                                    .set(data)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("UsernameSaveSuccessful", "Username saved successfully: " + mAuth.getUid());
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

                        }//if

                    }
                });

    }//authenticationAnon

}//Database
