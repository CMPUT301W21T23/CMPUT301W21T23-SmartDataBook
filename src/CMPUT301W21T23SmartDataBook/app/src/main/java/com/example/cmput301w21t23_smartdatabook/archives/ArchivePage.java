package com.example.cmput301w21t23_smartdatabook.archives;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.experimentDetails.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.home.CardList;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Archives activity page
 * shows a listview of the experiments that have been "ended"
 * @author Alex Mak
 */

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
                                if (experiment.getExpName().contains(currentQuery) ||
                                        UserName.get(experiment.getOwnerUserID()).getUserName().contains(currentQuery) ||
                                        experiment.getDate().toString().contains(currentQuery) ||
                                        experiment.getDescription().contains(currentQuery)) {

                                    searchDataList.add(experiment);

                                }
                            }
                            archiveExperimentAdapter.clear();
                            archiveExperimentAdapter.addAll(searchDataList);

                        }else{
                            archiveExperimentAdapter.addAll(archiveExperimentDataList);
                        }

                        archiveExperimentAdapter.notifyDataSetChanged();

                        archiveExperimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Experiment exp = archiveExperimentDataList.get(position); // get the experiment from list
                                Intent intent = new Intent(getActivity(), ExperimentDetails.class);
                                intent.putExtra("experiment", (Parcelable) exp); // pass experiment object
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

}
