package com.example.cmput301w21t23_smartdatabook;

import android.util.Log;
import android.widget.ArrayAdapter;

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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Database {

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    int expID = 0;

    public void addTrialToDB(Experiment experiment, String parentCollection){

        db = FirebaseFirestore.getInstance();
        final CollectionReference allExpCollection = db.collection(parentCollection);
        HashMap<String, String> data = new HashMap<>();

        data.put("Trial Type", experiment.getTrialType());
        allExpCollection
                .document("" + experiment.getExpName())
                .collection("Trials")
                .document("Trial#1")
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success", "Trial has been added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Failure", "Data storing failed");
                    }
                });
    }

    /**
     * Get Experiment documents from the database and add its contents to the experimentDataList
     * to populate the user's fragment page.
     * (All experiments added to the experimentDataList ONLY exist in the SCOPE of the "onComplete()").
     * @author Bosco Chan
     * @param experimentDataList the array list that holds the all the experiments for a user
     */
    public void fillDataList(ArrayList<Experiment> experimentDataList, ArrayAdapter<Experiment> experimentAdapter, CollectionReference collection) {
        db = FirebaseFirestore.getInstance();
        collection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("Success", document.getId() + " => " + document.getData());
                                experimentDataList.add( new Experiment(
                                        document.getData().get("Name").toString(),
                                        document.getData().get("UUID").toString(),
                                        document.getData().get("Trial Type").toString(),
                                        document.getData().get("Description").toString(),
                                        giveBoolean( document.getData().get("LocationStatus").toString() ),
                                        Integer.parseInt( document.getData().get("Minimum Trials").toString() ),
                                        Integer.parseInt( document.getData().get("Maximum Trials").toString() ),
                                        giveBoolean( document.getData().get("PublicStatus").toString() ),
                                        document.getData().get("Date").toString() ) );
                            }

                            experimentAdapter.notifyDataSetChanged();

                        } else {
                            Log.d("Failure", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    /**
     * Checks if the status of  "PublicStatus" and "LocationStatus" found in the database
     * is "on" or "of" and gives respective boolean.
     * @author Bosco Chan
     * @param status
     * @return a boolean that is either "true" or "false"
     */
    public boolean giveBoolean (String status) {
        if (status == "On"){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Checks if the boolean condition of "getRegionOn" and "isPublic" is "true" or "false"
     * and gives the respective "on" or "off" string
     * @author Bosco Chan
     * @param condition the boolean that determines if region or isPublic is true or false
     * @return a string that is either "on" or "off"
     */
    public String giveString (boolean condition) {
        if (condition == true) {
            return "On";
        }else{
            return "Off";
        }
    }

    /**
     * Add a new experiment object to the Firebase database by assigning a unique experiment ID.
     * An experiment is added by firstly checking if the collection contains any
     * @author Bosco Chan
     * @param newExperiment The experiment object that is to be added to the Firebase database
     */
    public void addExperimentToDB(Experiment newExperiment, CollectionReference collection) {

        db = FirebaseFirestore.getInstance();

        //Query only gets one document
        Query query = collection
                .orderBy("ID", Query.Direction.DESCENDING)
                .limit(1);

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        List documentList = task.getResult().getDocuments();

                        int expID;

                        HashMap<String, String> data = new HashMap<>();
                        // If there’s some data in the EditText field, then we create a new key-value pair.
                        data.put("Name", newExperiment.getExpName());
                        data.put("Description", newExperiment.getDescription());
                        data.put("Trial Type", newExperiment.getTrialType());
                        data.put("LocationStatus", giveString( newExperiment.getRegionOn() ) );
                        data.put("PublicStatus", giveString( newExperiment.isPublic() ) );
                        data.put("UUID", newExperiment.getOwnerUserID());
                        data.put("Minimum Trials", "" + newExperiment.getMinTrials() );
                        data.put("Maximum Trials", "" + newExperiment.getMaxTrials() );
                        data.put("Date", newExperiment.getDate() );

                        //If the query successfully returns a document, then it means a doc exists in the collection
                        if ( documentList.size() != 0 ){
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            expID = Integer.parseInt(document.getData().get("ID").toString()) + 1;
                        }else{
                            expID = 0;
                        }//if

                        data.put("ID", "" + expID);
                        collection
                                .document("" + expID )
                                .set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Success", "Experiment has been added successfully");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Failure", "Data storing failed");
                                    }
                                });

                    }
                });

//
//        HashMap<String, String> data = new HashMap<>();
//
//        // If there’s some data in the EditText field, then we create a new key-value pair.
//        data.put("Name", newExperiment.getExpName());
//        data.put("Description", newExperiment.getDescription());
//        data.put("Trial Type", newExperiment.getTrialType());
//        data.put("LocationStatus", giveString( newExperiment.getRegionOn() ) );
//        data.put("PublicStatus", giveString( newExperiment.isPublic() ) );
//        data.put("UUID", newExperiment.getOwnerUserID());
//        data.put("Minimum Trials", "" + newExperiment.getMinTrials() );
//        data.put("Maximum Trials", "" + newExperiment.getMaxTrials() );
//        data.put("Date", newExperiment.getDate() );
//        data.put("ID", "" + expID);
//
//        collection
//                .document("" + newExperiment.getExpName() )
//                .set(data)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("Success", "Experiment has been added successfully");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("Failure", "Data storing failed");
//                    }
//                });

    }//addExperimentToDB

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
