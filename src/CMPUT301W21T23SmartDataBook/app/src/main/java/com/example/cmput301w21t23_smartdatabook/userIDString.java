package com.example.cmput301w21t23_smartdatabook;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class userIDString {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public FirebaseUser currentUser = mAuth.getCurrentUser();

//    mAuth.signInAnonymously();

    public String getCurrentUser() {
        return currentUser.getUid();
    }
}
