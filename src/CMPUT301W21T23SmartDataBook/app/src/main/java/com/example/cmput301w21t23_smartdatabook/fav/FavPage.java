package com.example.cmput301w21t23_smartdatabook.fav;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.cmput301w21t23_smartdatabook.FillDataCallBack;
import com.example.cmput301w21t23_smartdatabook.Database;
import com.example.cmput301w21t23_smartdatabook.experimentDetails.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.home.CardList;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.home.homePage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

/**
 * class: FavPage
 * This class consists the page of the user's favourite experiments
 * @author Afaq Nabi, Bosco Chan
 */
public class FavPage extends Fragment implements FillDataCallBack {
    private static final String AP1 = "AP1";
    private static final String AP2 = "AP2";

    private ListView favList;
    private static ArrayAdapter<Experiment> favAdapter;
    private static ArrayList<Experiment> favDataList;

    private String currentID;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Database database;

    //Implement interrupted exception throw on database object instantiation
    {
        try {
            database = new Database(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public FavPage(){
    }

    public static FavPage newInstance(String userID) {
        FavPage fragment = new FavPage();
        Bundle args = new Bundle();
        args.putString("UUID", userID);
        fragment.setArguments(args);
        return fragment;
    }

//    /**
//     * new instance method of FavPage
//     * @param p1
//     * @param p2
//     * @return fragment
//     */
//    public static FavPage newInstance(String p1, String p2){
//        FavPage fragment = new FavPage();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentID = getArguments().getString("UUID");
        }
    }

//    /**
//     * oncreate method of FavPage
//     * @param savedInstanceState
//     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            String mParam1 = getArguments().getString(AP1);
//            String mParam2 = getArguments().getString(AP2);
//        }
//    }

    /**
     * this emthod create the view of the user's favouritte experiments page
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

        favAdapter = new CardList(getContext(), favDataList, 2, currentID);
        favList.setAdapter(favAdapter);

        Toast.makeText(getContext(), "" + currentID, Toast.LENGTH_LONG).show();

        database.fillDataList(new FillDataCallBack() {
            @Override
            public void getExpDataList(ArrayList<Experiment> DataList) {

                //experimentDataList with added items ONLY exist inside the scope of this getExpDataList function
                favDataList = DataList;
                favAdapter.addAll(DataList);

//                Log.d("List", "" + favDataList.get(0).getExpName());

                favAdapter.notifyDataSetChanged();

                favList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Experiment exp = favDataList.get(position); // get the experiment from list
                        Intent intent = new Intent(getActivity(), ExperimentDetails.class);
                        intent.putExtra("position", position); // pass position to ExperimentDetails class
                        intent.putExtra("experiment", exp); // pass experiment object
                        startActivity(intent);
                    }
                });

            }//getExpDataList
        }, favAdapter, db.collection("Users").document(currentID).collection("Favorites"), currentID);//fillDataList

        favList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Experiment exp = favDataList.get(position); // get the experiment from list
                Intent intent = new Intent(getActivity(), ExperimentDetails.class);
                intent.putExtra("position", position); // pass position to ExperimentDetails class
                intent.putExtra("experiment", exp); // pass experiment object
                startActivity(intent);
            }
        });

        return view;
    }//onCreateView

    /**
     * Assigns the experimentDataList with the callback-acquired DataList containing Experiment objects
     * @param DataList is the Experiment-populated array list found in Database.fillDataList()
     */
    @Override
    public void getExpDataList(ArrayList<Experiment> DataList) {
        favDataList = DataList;
    }

}
