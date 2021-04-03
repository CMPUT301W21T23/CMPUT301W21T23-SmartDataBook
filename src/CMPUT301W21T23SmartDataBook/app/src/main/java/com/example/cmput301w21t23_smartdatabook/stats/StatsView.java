package com.example.cmput301w21t23_smartdatabook.stats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.home.CardList;
import com.example.cmput301w21t23_smartdatabook.home.addExpFragment;
import com.example.cmput301w21t23_smartdatabook.home.homePage;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Hashtable;



public class StatsView extends AppCompatActivity {

        private ArrayList<String> statsDataList;
    private User user;
    private Database database;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);

        database = new Database();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        Experiment experiment = (Experiment) intent.getSerializableExtra("experiment");

        statsDataList = new ArrayList<>();

        //Source: Erwin Kurniawan A; https://stackoverflow.com/users/7693494/erwin-kurniawan-a
        //Code: https://stackoverflow.com/questions/61930061/how-to-return-a-value-from-oncompletelistener-while-creating-user-with-email-and

        database.fillStatsList(new GeneralDataCallBack() {
            @Override
            public void onDataReturn(Object returnedObject) {
                ArrayList<String> DataList = (ArrayList<String>) returnedObject;

                statsDataList = DataList;

                Log.d("ItemsCount", "" + statsDataList.size());

            }
        } , statsDataList, db.collection("Experiments").document(experiment.getExpID()).collection("Trials"));


//
//        database.fillUserName(new GeneralDataCallBack() {
//            @Override
//            public void onDataReturn(Object returnedObject) {
//                Hashtable<String, User> UserName = (Hashtable<String, User>) returnedObject;
//                database.fillDataList(new GeneralDataCallBack() {
//                    @Override
//                    public void onDataReturn(Object returnedObject) {
//                        ArrayList<Number> DataList = (ArrayList<Stats>) returnedObject;
//
//                        StatsAdapter = new CardList(getContext(), StatsDataList, UserName, 1);
//
//                        StatsList.setAdapter(StatsAdapter);
//
//                        //Reset the Stats adapter for every onCreateView call
//                        StatsAdapter.clear();
//                        searchDataList.clear();
//
//                        //StatsDataList with added items ONLY exist inside the scope of this getExpDataList function
//                        StatsDataList = DataList;
//
//                        //Create a new searchDataList depending on the query
//                        if (currentQuery != null) {
//                            for (Stats Stats : StatsDataList) {
//                                if (Stats.getExpName().contains(currentQuery) ||
//                                        UserName.get(Stats.getOwnerUserID()).getUserName().contains(currentQuery) ||
//                                        Stats.getDate().contains(currentQuery) ||
//                                        Stats.getDescription().contains(currentQuery)) {
//
//                                    searchDataList.add(Stats);
//
//                                }
//                            }
//
//                            StatsAdapter.clear();
//                            StatsAdapter.addAll(searchDataList);
//
//                        }else{
//                            StatsAdapter.addAll(StatsDataList);
//                        }
//
//                        StatsAdapter.notifyDataSetChanged();
//
//                        StatsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                Stats exp = StatsDataList.get(position); // get the Stats from list
//                                Intent intent = new Intent(getActivity(), StatsDetails.class);
//                                intent.putExtra("Stats", exp); // pass Stats object
//                                startActivity(intent);
//                            }
//                        });
//
////                        Log.d("QueryCheck", ""+currentQuery);
//
//                    }//getExpDataList
//                }, StatsAdapter, db.collection("Stats"), user.getUserUniqueID(), UserName);//fillDataList
//            }
//        });

    }//onCreate

}


