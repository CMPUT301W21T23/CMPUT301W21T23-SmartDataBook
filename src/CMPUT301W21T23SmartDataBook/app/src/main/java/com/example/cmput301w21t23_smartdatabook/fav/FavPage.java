package com.example.cmput301w21t23_smartdatabook.fav;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.experiment.CardList;
import com.example.cmput301w21t23_smartdatabook.experiment.Experiment;
import com.example.cmput301w21t23_smartdatabook.experiment.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * class: FavPage
 * This class consists the page of the user's favourite experiments
 *
 * @author Afaq Nabi, Bosco Chan, Jayden Cho
 */
public class FavPage extends Fragment {

    private static ArrayAdapter<Experiment> favAdapter;
    private static ArrayList<Experiment> favDataList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // initialize elements
    private ListView favList;
    private User user;
    private Database database;
    private MainActivity activity;
    private String currentQuery;

    public FavPage() {
    }

    public static FavPage newInstance(String user) {
        FavPage fragment = new FavPage();
        Bundle args = new Bundle();
        args.putString("", user);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * onCreate method of Favourite page
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = User.getUser();
        }
        activity = (MainActivity) getActivity();
    }

    /**
     * The method updates the favourite page if there are any queries
     *
     * @param query
     */
    public void doUpdate(String query) {

        currentQuery = query.toLowerCase();

        database.fillUserName(new GeneralDataCallBack() {
            @Override
            public void onDataReturn(Object returnedObject) {
                Hashtable<String, User> UserName = (Hashtable<String, User>) returnedObject;
                ArrayList<Experiment> temp = new ArrayList<>();

                for (int i = 0; i < favDataList.size(); i++) {
                    Experiment experiment = favDataList.get(i);
                    if (experiment.getExpName().toLowerCase().contains(currentQuery) ||
                            UserName.get(experiment.getOwnerUserID()).getUserName().toLowerCase().contains(currentQuery) ||
                            experiment.getDate().toLowerCase().contains(currentQuery) ||
                            experiment.getDescription().toLowerCase().contains(currentQuery) ||
                            experiment.getTrialType().toLowerCase().contains(currentQuery))
                        temp.add(experiment);
                }

                favAdapter = new CardList(getContext(), temp, 1);
                favList.setAdapter(favAdapter);

            }
        });
    }

    /**
     * this method create the view of the user's favourite experiments page
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View, the view of the page
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.followed_experiments, container, false);

        favList = view.findViewById(R.id.followedExpListView);
        favDataList = new ArrayList<>();
        database = new Database();

        favAdapter = new CardList(getContext(), favDataList, 1);

        database.fillUserName(new GeneralDataCallBack() {
            @Override
            public void onDataReturn(Object returnedData) {
                Hashtable<String, User> UserName = (Hashtable<String, User>) returnedData;
                database.fillDataList(new GeneralDataCallBack() {
                    @Override
                    public void onDataReturn(Object returnedData) {
                        ArrayList<Experiment> DataList = (ArrayList<Experiment>) returnedData;

                        favAdapter = new CardList(getContext(), favDataList, 1);

                        favList.setAdapter(favAdapter);

                        //Reset the experiment adapter for every onCreateView call
                        favAdapter.clear();

                        //experimentDataList with added items ONLY exist inside the scope of this getExpDataList function
                        favDataList = DataList;

                        favAdapter.addAll(favDataList);
                        favAdapter.notifyDataSetChanged();

                        favList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Experiment exp = (Experiment) parent.getAdapter().getItem(position); // get the experiment from list
                                Intent intent = new Intent(getActivity(), ExperimentDetails.class);
                                intent.putExtra("currentID", user.getUserUniqueID()); // pass position to ExperimentDetails class
                                intent.putExtra("experiment", (Parcelable) exp); // pass experiment object
                                startActivity(intent);
                            }
                        });

                    }//getExpDataList
                }, favAdapter, db.collection("Users").document(user.getUserUniqueID()).collection("Favorites"), user.getUserUniqueID(), UserName);//fillDataList
            }
        });
        return view;
    }//onCreateView

    /**
     * onResume method of Favourite page
     */
    @Override
    public void onResume() {
        super.onResume();
        favAdapter.notifyDataSetChanged();
        activity.getSupportActionBar().setTitle("Subscribed");
        activity.onAttachFragment(this);
        activity.setBottomNavigationItem(R.id.fav_nav);
    }


}
