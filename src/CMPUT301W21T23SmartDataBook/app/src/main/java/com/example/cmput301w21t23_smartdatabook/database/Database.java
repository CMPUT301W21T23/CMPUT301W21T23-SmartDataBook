
package com.example.cmput301w21t23_smartdatabook.database;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cmput301w21t23_smartdatabook.Experiment;
//import com.example.cmput301w21t23_smartdatabook.trials.BinomialTrial;
//import com.example.cmput301w21t23_smartdatabook.trials.CountTrial;
//import com.example.cmput301w21t23_smartdatabook.trials.MeasurementTrial;
import com.example.cmput301w21t23_smartdatabook.trials.Trial;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.example.cmput301w21t23_smartdatabook.comments.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * class: Database
 * This class consists database, it has the attributes call back which handles synchronous and asychronous functions, and an arraylist of experiment
 * It passes/ takes information to firestore
 * @author Bosco Chan Afaq Nabi
 */
public class Database {

    private FillDataCallBack fillDataCallBack;
    private SignInCallBack signInCallBack;
    private ArrayList<Experiment> experimentDataList = new ArrayList<>();
    private static final String TAG1 = "Your";
    private static final String TAG2 = "Warning";
    private static final String TAG3 = "Exception";

    private User user = User.getUser();
    private static Database database;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    /**
     * Constructor of the database class
     * @param fillDataCallBack
     * @throws InterruptedException
     * @author Bosco Chan
     */
    public Database(FillDataCallBack fillDataCallBack) throws InterruptedException {
        this.fillDataCallBack = fillDataCallBack;
    }

    public Database(SignInCallBack signInCallBack) throws InterruptedException {
        this.signInCallBack = signInCallBack;
    }

    /**
     * Main function does most of database's tasks
     * @author Bosco Chan
     */
    public Database (){};

    //Singleton implementation
    public static Database getDataBase(){
        if (database == null){
            database = new Database();
        }
        return database;
    }


    public void followStatus(DocumentReference ref, Experiment experiment, Context context, CheckBox follow, String currentID) {

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        if(!currentID.equals(experiment.getOwnerUserID())){
                            ref.delete();
                        }
                        else{
                            Toast.makeText(context,"Cannot unfollow owned Experiment",Toast.LENGTH_SHORT).show();
                            follow.setChecked(true);
                        }
                    }
                }
            }
        });//ref.get()
    }


    //Task will be executed here. Done in the background. Called Asynchronous task.
    /**
     * This function delete trials from the database
     * @param experiment
     * @param parentCollection
     */
    public void deleteTrialFromDB(DocumentReference DocRef){
        DocRef.delete();

    }

    public void addTrialToDB(DocumentReference genericDocument, Trial trial){
        HashMap<String, Object> data = new HashMap<>();
        data.put("Region On", trial.isGeoLocationSettingOn());
        data.put("Trial Type", trial.getExpType());
        data.put("Trial Value", trial.getValue());
        data.put("UUID", trial.getUid());
        data.put("TrialID", trial.getTrialID());
        genericDocument.set(data);
    }

    public void fillTrialList(CollectionReference coll, ArrayList<Trial> trialDataList, ArrayAdapter<Trial> trialArrayAdapter){
        coll.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                trialDataList.add(new Trial(
                                        (Boolean) document.get("Region On"),
                                        document.get("Trial Type").toString(),
                                        document.get("Trial Value"),
                                        document.get("UUID").toString(),
                                        document.get("TrialID").toString())
                                );
                            }
                            Log.d("Trials list", ""+trialDataList.size());
                            trialArrayAdapter.notifyDataSetChanged();
                        }
                        else {
                            Log.d("Failure", "fail");
                        }
                    }
                });
    }


    public void addCommentToDB(DocumentReference DocRef, Comment comment){
        HashMap<String, String> data = new HashMap<>();
        data.put("CommentText", comment.getText());
        data.put("UserID", comment.getUserUniqueID());
        data.put("CommentID", comment.getCommentID());
        data.put("Date", comment.getDate());
        DocRef.set(data);
    }


    public void fillCommentList(CollectionReference coll, ArrayList<Comment> commentList, ArrayAdapter<Comment> commentAdapter) {
        coll
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                commentList.add(new Comment(
                                        document.get("CommentText").toString(),
                                        document.get("UserID").toString(),
                                        document.get("CommentID").toString(),
                                        document.get("Date").toString()));
                                Log.d("Success", document.getId() + " => " + document.getData());
                            }
                            commentAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }



    public void fillUserName(FillUserCallBack fillUserCallBack) {
        db = FirebaseFirestore.getInstance();

        Hashtable<String, String> userNames = new Hashtable<String, String>();

        db.collection("Users")
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                 @Override
                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                     mAuth = FirebaseAuth.getInstance();

                     String path = db.collection("Users").getPath();

                     if (task.isSuccessful()) {
                         for (QueryDocumentSnapshot document : task.getResult()) {
                             if (document.getData().get("UUID") != null && document.getData().get("UserName") != null) {
                                 userNames.put(document.getData().get("UUID").toString(), document.getData().get("UserName").toString());
                                 Log.d("Getting_user", "Success");
                             }
                         }
                         fillUserCallBack.getUserTable(userNames);
                     } else {
                         fillUserCallBack.getUserTable(new Hashtable<String, String>());
                     }
                 }
         });
    }


    /**
     * Get Experiment documents from the database and add its contents to the experimentDataList
     * to populate the user's fragment page.
     * (All experiments added to the experimentDataList ONLY exist in the SCOPE of the "onComplete()").
     * (Since ArrayAdapter<Experiment> experimentAdapter
     * @author Bosco Chan
     * @param fillDataCallBack is the callback instance from a synchronous.
     */
    public void fillDataList(FillDataCallBack fillDataCallBack, ArrayAdapter<Experiment> experimentArrayAdapter, CollectionReference collection, String currentID, Hashtable<String, String> userNames) {
        db = FirebaseFirestore.getInstance();
        Log.d("USER_SIZE", String.valueOf(userNames.size()));

        collection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        mAuth = FirebaseAuth.getInstance();

                        experimentDataList.clear();

                        String coll1 = db.collection("Experiments").getPath();

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if ( (coll1.equals(collection.getPath()) && document.getData().get("ExpID") != null && giveBoolean( document.getData().get("PublicStatus").toString())) ||
                                        (collection.getPath().equals(db.collection("Users").document(currentID).collection("Favorites").getPath())) ){

                                    experimentDataList.add( new Experiment(
                                            document.getData().get("Name").toString(),
                                            document.getData().get("UUID").toString(),
                                            userNames.get(document.getData().get("UUID").toString()),
                                            document.getData().get("Trial Type").toString(),
                                            document.getData().get("Description").toString(),
                                            giveBoolean( document.getData().get("LocationStatus").toString() ),
                                            Integer.parseInt( document.getData().get("Minimum Trials").toString() ),
                                            Integer.parseInt( document.getData().get("Maximum Trials").toString() ),
                                            giveBoolean( document.getData().get("PublicStatus").toString() ),
                                            document.getData().get("Date").toString(),
                                            document.getData().get("ExpID").toString(),
                                            giveBoolean(document.getData().get("isEnd").toString())
                                            )
                                    );
                                }

                                Log.d("Success", document.getId() + " => " + document.getData());

                            }

                            //Get callback to grab the populated dataList
                            fillDataCallBack.getExpDataList(experimentDataList);
                            experimentArrayAdapter.notifyDataSetChanged();

                        } else {
                            Log.d("Failure", "Error getting documents: ", task.getException());
                            fillDataCallBack.getExpDataList(new ArrayList<>());
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
        return status.equals("On");
    }

    /**
     * Checks if the boolean condition of "getRegionOn" and "isPublic" is "true" or "false"
     * and gives the respective "on" or "off" string
     * @author Bosco Chan
     * @param condition the boolean that determines if region or isPublic is true or false
     * @return a string that is either "on" or "off"
     */
    public String giveString (boolean condition) {
        if (condition) {
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
    public void addExperimentToDB(Experiment newExperiment, CollectionReference collection, String currentID) {
        mAuth = FirebaseAuth.getInstance();

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
        data.put("isEnd", giveString(newExperiment.getIsEnd()));

        collection
                .document(newExperiment.getExpID())
                .set(data);

        db.collection("Users")
                .document(currentID)
                .collection("Favorites")
                .document(newExperiment.getExpID())
                .set(data);

    }//addExperimentToDB

    /**
     * Edit user profile by querying the database.
     * @author Bosco Chan
     * @param usernameTextField contains the editText field to write username to
     * @param emailTextField contains the editText field to write email to
     * @param saveButtonView contains the save button view
     * @param context contains the given context from which this function is called from
     */
    public void editUser(EditText usernameTextField, EditText emailTextField, View saveButtonView, Context context, String currentID, User user) {

        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Users").document(currentID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG1, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> data = document.getData();

                        if (data.get("UserName").toString().equals("")){
                            usernameTextField.setHint("Enter Username");
                        }else{
                            usernameTextField.setText(data.get("UserName").toString());
                        }

                        if (data.get("Email").toString().equals("")){
                            emailTextField.setHint("Enter Email");
                        }else{
                            emailTextField.setText(data.get("Email").toString());
                        }

                        saveButtonView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String username = usernameTextField.getText().toString();
                                String email = emailTextField.getText().toString();
                                user.setUserName(username);
                                user.setUserContact(email);

                                docRef.update("UserName", username);

                                docRef.update("Email", email);

                            }
                        });

                    } else {
                        Log.d(TAG2, "No such document");
                    }
                } else {
                    Log.d(TAG3, "get failed with ", task.getException());
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
     * @author Bosco Chan
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

                            assert currentUser != null;
                            signInCallBack.updateHomeScreen(currentUser.getUid());

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            // Get a top level reference to the collection
                            final CollectionReference allUsersCollection = db.collection("Users");
                            HashMap<String, String> data = new HashMap<>();

                            //Source: Firebase, firebase.google.com
                            //License: Creative Commons Attribution 4.0 License, Apache 2.0 License
                            //Code: https://firebase.google.com/docs/firestore/query-data/get-data#java_
                            DocumentReference userDoc = allUsersCollection.document(currentUser.getUid());
                            userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        assert document != null;
                                        if (!document.exists()) {
                                            data.put("UserName", "User - "+currentUser.getUid().substring(0,4));
                                            data.put("Email", "");
                                            data.put("UUID", currentUser.getUid());

                                            allUsersCollection
                                                    .document(currentUser.getUid())
                                                    .set(data);
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

    public void publicOrEnd(CollectionReference coll, String onOff, Experiment experiment, String status){
        coll.document(experiment.getExpID()).update(status, onOff);
    }

}//Database
