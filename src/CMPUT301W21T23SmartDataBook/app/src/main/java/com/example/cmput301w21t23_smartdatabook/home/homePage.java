package com.example.cmput301w21t23_smartdatabook.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.example.cmput301w21t23_smartdatabook.experimentDetails.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Hashtable;

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

public class homePage extends Fragment {

    private ListView experimentList;
    private ArrayList<Experiment> experimentDataList;
    private static ArrayAdapter<Experiment> experimentAdapter;
    private MainActivity mainActivity;

    private User user;
    private Database database;

    private String currentQuery;
    private ArrayList<Experiment> searchDataList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public homePage() {
    }

    public static homePage newInstance(String user) {
        homePage fragment = new homePage();
        Bundle args = new Bundle();
        args.putString("", user);
        fragment.setArguments(args);
        return fragment;
    }

    public void doUpdate(String query, Fragment currentFragment) {

        currentQuery = query;

        if (currentQuery != null){
            //Source: Michele Lacorte; https://stackoverflow.com/users/4529790/michele-lacorte
            //Code: https://stackoverflow.com/questions/32359727/method-to-refresh-fragment-content-when-data-changed-like-recall-oncreateview
            //Refresh the current fragment after assigning a the currentQuery
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();
        }

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
            user = User.getUser();
        }
        mainActivity = (MainActivity) getActivity();
        database = new Database();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_page, container, false);

        experimentList = view.findViewById(R.id.experiment_list);

        experimentDataList = new ArrayList<>();
        searchDataList = new ArrayList<>();

        experimentAdapter = new CardList(getContext(), experimentDataList, new Hashtable<String, User>(), 1);

        //Source: Erwin Kurniawan A; https://stackoverflow.com/users/7693494/erwin-kurniawan-a
        //Code: https://stackoverflow.com/questions/61930061/how-to-return-a-value-from-oncompletelistener-while-creating-user-with-email-and
        database.fillUserName(new GeneralDataCallBack() {
            @Override
            public void onDataReturn(Object returnedObject) {
                Hashtable<String, User> UserName = (Hashtable<String, User>) returnedObject;
                database.fillDataList(new GeneralDataCallBack() {
                    @Override
                    public void onDataReturn(Object returnedObject) {
                        ArrayList<Experiment> DataList = (ArrayList<Experiment>) returnedObject;

                        experimentAdapter = new CardList(getContext(), experimentDataList, UserName, 1);

                        experimentList.setAdapter(experimentAdapter);

                        //Reset the experiment adapter for every onCreateView call
                        experimentAdapter.clear();
                        searchDataList.clear();

                        //experimentDataList with added items ONLY exist inside the scope of this getExpDataList function
                        experimentDataList = DataList;

                        //Create a new searchDataList depending on the query
                        if (currentQuery != null) {
                            for (Experiment experiment : experimentDataList) {
                                if (experiment.getExpName().contains(currentQuery) ||
                                        UserName.get(experiment.getOwnerUserID()).getUserName().contains(currentQuery) ||
                                        experiment.getDate().toString().contains(currentQuery) ||
                                        experiment.getDescription().contains(currentQuery)) {

                                    searchDataList.add(experiment);

                                }
                            }

                            experimentAdapter.clear();
                            experimentAdapter.addAll(searchDataList);

                        }else{
                            experimentAdapter.addAll(experimentDataList);
                        }

                        experimentAdapter.notifyDataSetChanged();

                        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Experiment exp = experimentDataList.get(position); // get the experiment from list
                                Intent intent = new Intent(getActivity(), ExperimentDetails.class);
                                intent.putExtra("experiment", (Parcelable) exp); // pass experiment object
                                startActivity(intent);
                            }
                        });

                    }//getExpDataList
                }, experimentAdapter, db.collection("Experiments"), user.getUserUniqueID(), UserName);//fillDataList
            }
        });


        final FloatingActionButton addExperimentButton = view.findViewById(R.id.add_experiment_button);
        addExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle args = new Bundle();
                args.putString("UUID", user.getUserUniqueID());

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
                database.addExperimentToDB(newExperiment, experimentsCollection, user.getUserUniqueID() );
                experimentAdapter.notifyDataSetChanged();
            }
        }

    }//onActivityResult


}//homePage
