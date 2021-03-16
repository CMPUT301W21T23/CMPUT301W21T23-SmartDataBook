package com.example.cmput301w21t23_smartdatabook.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmput301w21t23_smartdatabook.CardList;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class: homePage
 * This is a class that composes the home-page of the app
 * The home page initialize and displays the list of experiments
 * It inflate the layout for the experiment's fragment
 * It allows user to add a new experiment by clicking a floating button
 *
 * @author Afaq Nabi, Bosco Chan
 * @see Fragment, Firebase
 */

public class homePage extends Fragment {

    private static final String AP1 = "AP1";
    private static final String AP2 = "AP2";

    ListView experimentList;
    ArrayAdapter<Experiment> experimentAdapter;
    ArrayList<Experiment> experimentDataList;

    FirebaseFirestore db;

    public homePage() {
    }

    public static homePage newInstance(String p1, String p2) {
        homePage fragment = new homePage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        experimentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(AP1);
            String mParam2 = getArguments().getString(AP2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_page, container, false);

        experimentList = view.findViewById(R.id.experiment_list);
        experimentDataList = new ArrayList<>();

        experimentDataList.add(new Experiment("first", "123", "Binomial", "testtrial", false, 30, 60, true, "03/05/2021"));
        experimentDataList.add(new Experiment("second", "123", "Binomial", "testtrial", false, 30, 60, true, "03/05/2021"));

        fillDataList(experimentDataList);

        experimentAdapter = new CardList(getContext(), experimentDataList);
        experimentList.setAdapter(experimentAdapter);

        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Experiment exp = experimentDataList.get(position); // get the experiment from list
                Intent intent = new Intent(getActivity(), ExperimentDetails.class);
                intent.putExtra("position", position); // pass position to ExperimentDetails class
                intent.putExtra("experiment", exp); // pass experiment object
                startActivity(intent);
            }
        });

        final FloatingActionButton addExperimentButton = view.findViewById(R.id.add_experiment_button);
        addExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Source: Shweta Chauhan; https://stackoverflow.com/users/6021469/shweta-chauhan
                //Code: https://stackoverflow.com/questions/40085608/how-to-pass-data-from-one-fragment-to-previous-fragment
                addExpFragment addExpFrag = new addExpFragment();
                addExpFrag.setTargetFragment(homePage.this, 0);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.container, addExpFrag, "addExpFragment");
                ft.addToBackStack("addExpFragment");
                ft.commit();

            }
        });

        return view;

    }//onCreateView


    //Source: Shweta Chauhan; https://stackoverflow.com/users/6021469/shweta-chauhan
    //Code: https://stackoverflow.com/questions/40085608/how-to-pass-data-from-one-fragment-to-previous-fragment
    /**
     * Custom on activity result function that gets an experiment object from the second fragment
     * that had been started from this fragment (homePage.java).
     * @author Bosco Chan
     * @param requestCode Determines which object is wanted from a fragment
     * @param resultCode  Determines what the result is when taken
     * @param data        The intent that holds the serialized object
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int addExpFragmentResultCode = 1;
        int addExpFragmentRequestCode = 0;

        if (resultCode == addExpFragmentResultCode) {
            if (requestCode == addExpFragmentRequestCode) {
                Experiment newExperiment = (Experiment) data.getSerializableExtra("newExp");
                Toast.makeText(getActivity(), newExperiment.getExpName() + " " + newExperiment.getDescription(), Toast.LENGTH_SHORT).show();
                experimentAdapter.add(newExperiment);
                addExperimentToDB(newExperiment);
                experimentAdapter.notifyDataSetChanged();
            }
        }
    }//onActivityResult

    /**
     * Get a Experiment document from the database and add its contents to the experimentDataList
     * to populate the user's homePage with ALL experiments in the app
     * @param experimentDataList the array list that holds the all the experiments for a user
     */
    public void fillDataList(ArrayList<Experiment> experimentDataList) {
        db = FirebaseFirestore.getInstance();
        db.collection("Experiments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Success", document.getId() + " => " + document.getData());
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

                                experimentAdapter.notifyDataSetChanged();
                            }
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
     * Add a new experiment object to the Firebase database
     * @author Bosco Chan
     * @param newExperiment The experiment object that is to be added to the Firebase database
     */
    public void addExperimentToDB(Experiment newExperiment) {

        //Add into a Comments collection with a comment document containing
        //a Pies collection with a pie document
        db = FirebaseFirestore.getInstance();
        final CollectionReference allCommentsCollection = db.collection("Experiments");
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

        allCommentsCollection
                .document("" + newExperiment.getExpName())
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

        data.clear();
        data.put("Trial Type", newExperiment.getTrialType());
        allCommentsCollection
                .document("" + newExperiment.getExpName())
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

    }//addExperimentToDB
}
