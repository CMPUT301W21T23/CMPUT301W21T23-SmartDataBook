
package com.example.cmput301w21t23_smartdatabook;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cmput301w21t23_smartdatabook.Experiment;
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
import com.google.firebase.firestore.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Database {

    private CallBack callBack;
    private ArrayList<Experiment> experimentDataList = new ArrayList<>();

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    int expID = 0;

    public Database(CallBack callBack) throws InterruptedException {
        this.callBack = callBack;
    }

    public Database (){};

    //Task will be executed here. Done in the background. Called Asynchronous task.

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
     * (Since ArrayAdapter<Experiment> experimentAdapter
     * @author Bosco Chan
     * @param callBack is the callback instance from a synchronous.
     */
    public void fillDataList(CallBack callBack, ArrayAdapter<Experiment> experimentArrayAdapter, CollectionReference collection) {
        db = FirebaseFirestore.getInstance();
        collection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        experimentDataList.clear();

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
                                        document.getData().get("Date").toString(),
                                        document.getData().get("ExpID").toString() ));

                            }

                            //Get callback to grab the populated dataList
                            callBack.getExpDataList(experimentDataList);
                            experimentArrayAdapter.notifyDataSetChanged();

                        } else {
                            Log.d("Failure", "Error getting documents: ", task.getException());
                            callBack.getExpDataList(new ArrayList<>());
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
        HashMap<String, String> data = new HashMap<>();
        // If there’s some data in the EditText field, then we create a new key-value pair.
        data.put("Name", newExperiment.getExpName());
        data.put("Description", newExperiment.getDescription());
        data.put("Trial Type", newExperiment.getTrialType());
        data.put("LocationStatus", giveString(newExperiment.getRegionOn()));
        data.put("PublicStatus", giveString(newExperiment.isPublic()));
        data.put("UUID", newExperiment.getOwnerUserID());
        data.put("Minimum Trials", "" + newExperiment.getMinTrials());
        data.put("Maximum Trials", "" + newExperiment.getMaxTrials());
        data.put("Date", newExperiment.getDate());
        data.put("ExpID", newExperiment.getExpID());

        collection
                .document(newExperiment.getExpID())
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

    }//addExperimentToDB

    /**
     * Edit user profile by querying the database.
     * @author Bosco Chan
     * @param usernameTextField contains the editText field to write username to
     * @param emailTextField contains the editText field to write email to
     * @param saveButtonView contains the save button view
     * @param context contains the given context from which this function is called from
     */
    public void editUser(EditText usernameTextField, EditText emailTextField, View saveButtonView, Context context) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("DocSnapShot", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> data = document.getData();

                        if (data.get("UserName").toString() == ""){
                            usernameTextField.setHint("Username");
                        }else{
                            usernameTextField.setHint(data.get("UserName").toString());
                        }

                        if (data.get("Email").toString().equals("")){
                            emailTextField.setHint("Email");
                        }else{
                            emailTextField.setHint(data.get("Email").toString());
                        }

                        saveButtonView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String username = usernameTextField.getText().toString();
                                String email = emailTextField.getText().toString();

                                docRef
                                        .update("UserName", username)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                docRef
                                        .update("Email", email)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                usernameTextField.getText().clear();
                                emailTextField.getText().clear();
                            }
                        });

                    } else {
                        Log.d("Empty", "No such document");
                    }
                } else {
                    Log.d("Failed", "get failed with ", task.getException());
                }
            }
        });

    }

    //Temporarily sign in user as an anonymous user on first sign in
    //Source: firebase guides, https://firebase.google.com
    //License: Creative Commons Attribution 4.0 License, Apache 2.0 License
    //Code: https://firebase.google.com/docs/auth/android/anonymous-auth?authuser=0
    /**
     * Authenticates a new app user anonymously and generates a "User document" for the user
     * containing their respective "username and contact".
     */
    public void authenticateAnon() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
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
