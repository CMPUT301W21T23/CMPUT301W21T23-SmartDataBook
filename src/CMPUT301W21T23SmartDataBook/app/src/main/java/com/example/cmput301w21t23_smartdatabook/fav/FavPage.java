package com.example.cmput301w21t23_smartdatabook.fav;

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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.example.cmput301w21t23_smartdatabook.experimentDetails.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.home.CardList;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * class: FavPage
 * This class consists the page of the user's favourite experiments
 * @author Afaq Nabi, Bosco Chan
 */
public class FavPage extends Fragment {

    private ListView favList;
    private static ArrayAdapter<Experiment> favAdapter;
    private static ArrayList<Experiment> favDataList;
    private static ArrayList<Experiment> searchDataList;

    private User user;
    private Database database;

    private String currentQuery;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


//    //Implement interrupted exception throw on database object instantiation
//    {
//        try {
//            database = new Database(this);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public FavPage(){}

    public static FavPage newInstance(String user) {
        FavPage fragment = new FavPage();
        Bundle args = new Bundle();
        args.putString("", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = User.getUser();
        }
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

    /**
     * this emthod create the view of the user's favourite experiments page
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
        searchDataList = new ArrayList<>();
        database = new Database();

        favAdapter = new CardList(getContext(), favDataList, new Hashtable<String, User>(), 1);

        database.fillUserName(new GeneralDataCallBack() {
            @Override
            public void onDataReturn(Object returnedData) {
                Hashtable<String, User> UserName = (Hashtable<String, User>) returnedData;
                database.fillDataList(new GeneralDataCallBack() {
                    @Override
                    public void onDataReturn(Object returnedData) {
                        ArrayList<Experiment> DataList = (ArrayList<Experiment>) returnedData;

                        favAdapter = new CardList(getContext(), favDataList, UserName,1);

                        favList.setAdapter(favAdapter);

                        Toast.makeText(getContext(), "" + user.getUserUniqueID(), Toast.LENGTH_SHORT).show();

                        //Reset the experiment adapter for every onCreateView call
                        favAdapter.clear();
                        searchDataList.clear();

                        //experimentDataList with added items ONLY exist inside the scope of this getExpDataList function
                        favDataList = DataList;

                        //Create a new searchDataList depending on the query
                        if (currentQuery != null) {
                            for (Experiment experiment : favDataList) {
                                if (experiment.getExpName().contains(currentQuery) ||
                                        UserName.get(experiment.getOwnerUserID()).getUserName().contains(currentQuery) ||
                                        experiment.getDate().toString().contains(currentQuery) ||
                                        experiment.getDescription().contains(currentQuery)) {

                                    Log.d("experiment", ""+ experiment.getExpName());
                                    searchDataList.add(experiment);

                                }
                            }

                            favAdapter.clear();
                            favAdapter.addAll(searchDataList);

                            Log.d("search", ""+searchDataList.size());

                        }else{
                            favAdapter.addAll(favDataList);
                        }

                        favAdapter.notifyDataSetChanged();

                        favList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Experiment exp = favDataList.get(position); // get the experiment from list
                                Intent intent = new Intent(getActivity(), ExperimentDetails.class);
                                intent.putExtra("currentID", user.getUserUniqueID()); // pass position to ExperimentDetails class
                                intent.putExtra("experiment", exp); // pass experiment object
                                startActivity(intent);
                            }
                        });

                    }//getExpDataList
                }, favAdapter, db.collection("Users").document(user.getUserUniqueID()).collection("Favorites"), user.getUserUniqueID(), UserName);//fillDataList
            }
        });
        return view;
    }//onCreateView

//    /**
//     * Assigns the experimentDataList with the callback-acquired DataList containing Experiment objects
//     * @param DataList is the Experiment-populated array list found in Database.fillDataList()
//     */
//    @Override
//    public void getExpDataList(ArrayList<Experiment> DataList) {
//        favDataList = DataList;
//    }

    @Override
    public void onResume(){
        super.onResume();
        favAdapter.notifyDataSetChanged();
    }

}
