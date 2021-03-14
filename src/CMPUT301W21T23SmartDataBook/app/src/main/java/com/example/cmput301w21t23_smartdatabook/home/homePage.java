package com.example.cmput301w21t23_smartdatabook.home;

import android.app.Activity;
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
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.experimentDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * Class: homePage
 * This is a class that composes the home-page of the app
 * The home page initialize and displays the list of experiments
 * It inflate the layout for the experiment's fragment
 * It allows user to add a new experiment by clicking a floating button
 * 
 * @author Afaq Nabi, Bosco Chan 
 * @see Fragment, Firebase
 * @version
 */

public class homePage extends Fragment {

    private static final String AP1 = "AP1";
    private static final String AP2 = "AP2";

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    ListView experimentList;
    ArrayAdapter<Experiment> experimentAdapter;
    ArrayList<Experiment> experimentDataList;

    public homePage(){
    }

    public static homePage newInstance(String p1, String p2){
        homePage fragment = new homePage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

        FloatingActionButton addExperimentButton = view.findViewById(R.id.add_experiment_button);

        experimentList = view.findViewById(R.id.experimentList);
        experimentDataList = new ArrayList<>();

        experimentDataList.add(new Experiment("first", "123",
                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));
        experimentDataList.add(new Experiment("second", "123",
                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));
        experimentDataList.add(new Experiment("third", "123",
                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));
        experimentDataList.add(new Experiment("6", "123",
                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));

        experimentAdapter = new CardList(getContext(), experimentDataList);

        experimentList.setAdapter(experimentAdapter);

        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Experiment exp = experimentDataList.get(position); // get the experiment from list
                Intent intent = new Intent(getActivity(), experimentDetails.class);
                intent.putExtra("position", position); // pass position to experimentDetails class
                intent.putExtra("experiment", exp); // pass experiment object
                startActivity(intent);
            }
        });

        addExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Source: Shweta Chauhan; https://stackoverflow.com/users/6021469/shweta-chauhan
                //Code: https://stackoverflow.com/questions/40085608/how-to-pass-data-from-one-fragment-to-previous-fragment
                addExpFragment addExpFrag = new addExpFragment();
                addExpFrag.setTargetFragment(homePage.this, 0);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, addExpFrag, "addExpFragment")
                        .addToBackStack("addExpFragment")
                        .commit();


            }
        });

        return view;
    }

    //Source: Shweta Chauhan; https://stackoverflow.com/users/6021469/shweta-chauhan
    //Code: https://stackoverflow.com/questions/40085608/how-to-pass-data-from-one-fragment-to-previous-fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            if (requestCode == 0){
                Experiment newExperiment = (Experiment) data.getSerializableExtra("newExp");
                Toast.makeText(getActivity(), newExperiment.getExpName() + " " + newExperiment.getDescription() , Toast.LENGTH_SHORT).show();
                experimentDataList.add(newExperiment);
                addExperimentToDB(newExperiment);
                experimentAdapter.notifyDataSetChanged();

            }
        }
    }


    /**
     * Add an experiment to the database
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

        allCommentsCollection
                .document("" + newExperiment.getExpName())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success", "Data has been added successfully");
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
                        Log.d("Success", "Data has been added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Failure", "Data storing failed");
                    }
                });


    }

}
