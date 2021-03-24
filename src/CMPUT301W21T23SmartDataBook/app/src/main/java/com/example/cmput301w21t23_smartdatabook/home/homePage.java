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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmput301w21t23_smartdatabook.FillDataCallBack;
import com.example.cmput301w21t23_smartdatabook.Database;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.experimentDetails.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Class: homePage
 * This is a class that composes the home-page of the app
 * The home page initialize and displays the list of experiments
 * It inflate the layout for the experiment's fragment
 * It allows user to add a new experiment by clicking a floating button
 *
 * @author Afaq Nabi, Bosco Chan, Jayden
 * @see Fragment, Firebase
 */

public class homePage extends Fragment implements FillDataCallBack {

    private static final String AP1 = "AP1";
    private static final String AP2 = "AP2";

    private ListView experimentList;
    private ArrayList<Experiment> experimentDataList;
    private static ArrayAdapter<Experiment> experimentAdapter;

    private String currentID;

    Database database;

    //Implement interrupted exception throw on database object instantiation
    {
        try {
            database = new Database(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Assigns the experimentDataList with the callback-acquired DataList containing Experiment objects
     * @param DataList is the Experiment-populated array list found in Database.fillDataList()
     */
    @Override
    public void getExpDataList(ArrayList<Experiment> DataList) {
        experimentDataList = DataList;
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public homePage() {
    }

    public static homePage newInstance(String userID) {
        homePage fragment = new homePage();
        Bundle args = new Bundle();
        args.putString("UUID", userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        experimentAdapter.notifyDataSetChanged();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentID = getArguments().getString("UUID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_page, container, false);

        experimentList = view.findViewById(R.id.experiment_list);
        experimentDataList = new ArrayList<>();

        experimentAdapter = new CardList(getContext(), experimentDataList,1, currentID);

        experimentList.setAdapter(experimentAdapter);

        //Source: Erwin Kurniawan A; https://stackoverflow.com/users/7693494/erwin-kurniawan-a
        //Code: https://stackoverflow.com/questions/61930061/how-to-return-a-value-from-oncompletelistener-while-creating-user-with-email-and
        database.fillDataList(new FillDataCallBack() {
            @Override
            public void getExpDataList(ArrayList<Experiment> DataList) {

                //experimentDataList with added items ONLY exist inside the scope of this getExpDataList function
                experimentDataList = DataList;
                experimentAdapter.addAll(experimentDataList);

                experimentAdapter.notifyDataSetChanged();

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

            }//getExpDataList
        }, experimentAdapter, db.collection("Experiments"), currentID);//fillDataList

        final FloatingActionButton addExperimentButton = view.findViewById(R.id.add_experiment_button);
        addExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle args = new Bundle();
                args.putString("UUID", currentID);

                //Source: Shweta Chauhan; https://stackoverflow.com/users/6021469/shweta-chauhan
                //Code: https://stackoverflow.com/questions/40085608/how-to-pass-data-from-one-fragment-to-previous-fragment
                addExpFragment addExpFrag = new addExpFragment();

                addExpFrag.setArguments(args);

                addExpFrag.setTargetFragment(homePage.this, 0);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, addExpFrag, "addExpFragment");
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
                CollectionReference experimentsCollection = db.collection("Experiments");
                database.addExperimentToDB(newExperiment, experimentsCollection, currentID );
                experimentAdapter.notifyDataSetChanged();
            }
        }

    }//onActivityResult


}//homePage
