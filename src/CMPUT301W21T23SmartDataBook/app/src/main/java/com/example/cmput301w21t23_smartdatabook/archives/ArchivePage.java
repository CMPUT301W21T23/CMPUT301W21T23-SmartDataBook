package com.example.cmput301w21t23_smartdatabook.archives;

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

import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.experimentDetails.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.home.CardList;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Hashtable;

public class ArchivePage extends Fragment {
    private static final String AP1 = "AP1";
    private static final String AP2 = "AP2";
    private User user = User.getUser();

    //things I added from homePage
    private Database database;
    private String currentQuery;

    private ListView archiveExperimentList;
    private ArrayList<Experiment> archiveExperimentDataList;
    private ArrayList<Experiment> searchDataList;
    private MainActivity mainActivity;
    private static ArrayAdapter<Experiment> archiveExperimentAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public ArchivePage() {
    }

    public static ArchivePage newInstance(String user) {
        ArchivePage fragment = new ArchivePage();
        Bundle args = new Bundle();
        args.putSerializable("", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            User user = (User) getArguments().getSerializable("user");
        }

        // added stuff
        mainActivity = (MainActivity) getActivity();
        database = new Database();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.archive_page, container, false);

        // added stuff
        archiveExperimentList=view.findViewById(R.id.archive_experiment_list);
        archiveExperimentDataList = new ArrayList<>();
        searchDataList = new ArrayList<>();

        archiveExperimentAdapter = new CardList(getContext(), archiveExperimentDataList, new Hashtable<String, User>(), 1);

        database.fillUserName(new GeneralDataCallBack() {
            @Override
            public void onDataReturn(Object returnedObject) {
                Hashtable<String, User> UserName = (Hashtable<String, User>) returnedObject;
                database.fillDataList(new GeneralDataCallBack() {
                    @Override
                    public void onDataReturn(Object returnedObject) {
                        ArrayList<Experiment> DataList = (ArrayList<Experiment>) returnedObject;

                        archiveExperimentAdapter = new CardList(getContext(), archiveExperimentDataList, UserName, 1);

                        archiveExperimentList.setAdapter(archiveExperimentAdapter);

//                        Toast.makeText(getContext(), "" + user.getUserName(), Toast.LENGTH_LONG).show();

                        //Reset the experiment adapter for every onCreateView call
                        archiveExperimentAdapter.clear();
                        searchDataList.clear();

                        //experimentDataList with added items ONLY exist inside the scope of this getExpDataList function
                        archiveExperimentDataList = DataList;

                        //Create a new searchDataList depending on the query
                        if (currentQuery != null) {
                            for (Experiment experiment : archiveExperimentDataList) {
                                if (experiment.getExpName().equals(currentQuery) ||
                                        experiment.getOwnerUserName().equals(currentQuery) ||
                                        experiment.getDate().equals(currentQuery) ||
                                        experiment.getDescription().contains(currentQuery)) {

                                    Log.d("experiment", ""+ experiment.getExpName());
                                    searchDataList.add(experiment);

                                }
                            }

                            archiveExperimentAdapter.clear();
                            archiveExperimentAdapter.addAll(searchDataList);

                            Log.d("search", ""+searchDataList.size());

                        }else{
                            archiveExperimentAdapter.addAll(archiveExperimentDataList);
                        }

                        Log.d("ExperimentList", ""+ archiveExperimentDataList.size());

                        archiveExperimentAdapter.notifyDataSetChanged();

                        archiveExperimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Experiment exp = archiveExperimentDataList.get(position); // get the experiment from list
                                Intent intent = new Intent(getActivity(), ExperimentDetails.class);
                                intent.putExtra("experiment", exp); // pass experiment object
                                startActivity(intent);
                            }
                        });


                    }//getExpDataList
                }, archiveExperimentAdapter, db.collection("Archived"), user.getUserUniqueID(), UserName);//fillDataList
            }
        });


        return view;



        // same as home page, even same adapter, copy the whole code, change the array adapter and the list
    }

    // copied from homePage
    @Override
    public void onResume() {
        super.onResume();
        archiveExperimentAdapter.notifyDataSetChanged();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        int addExpFragmentResultCode = 1;
//        int addExpFragmentRequestCode = 0;
//
//        if (resultCode == addExpFragmentResultCode) {
//            if (requestCode == addExpFragmentRequestCode) {
//                Experiment newExperiment = (Experiment) data.getSerializableExtra("newExp");
//                Toast.makeText(getActivity(), newExperiment.getExpName() + " " + newExperiment.getDescription(), Toast.LENGTH_SHORT).show();
//                experimentAdapter.add(newExperiment);
//                CollectionReference experimentsCollection = db.collection("Experiments");
//                database.addExperimentToDB(newExperiment, experimentsCollection, user.getUserUniqueID() );
//                experimentAdapter.notifyDataSetChanged();
//            }
//        }
//
//    }

}
