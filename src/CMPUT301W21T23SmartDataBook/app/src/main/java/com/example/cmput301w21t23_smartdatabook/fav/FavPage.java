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

import com.example.cmput301w21t23_smartdatabook.database.FillDataCallBack;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.example.cmput301w21t23_smartdatabook.experimentDetails.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.home.CardList;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

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

    private User user;
    private Database database;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


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

    public void doUpdate(String query) {
        Log.d("From_" + this.getClass().getSimpleName(), query);
    }

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

        favAdapter = new CardList(getContext(), favDataList, 2);
        favList.setAdapter(favAdapter);

        Toast.makeText(getContext(), "" + user.getUserUniqueID(), Toast.LENGTH_SHORT).show();

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
                        intent.putExtra("currentID", user.getUserUniqueID()); // pass position to ExperimentDetails class
                        intent.putExtra("experiment", exp); // pass experiment object
                        startActivity(intent);
                    }
                });

            }//getExpDataList
        }, favAdapter, db.collection("Users").document(user.getUserUniqueID()).collection("Favorites"), user.getUserUniqueID());//fillDataList

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

    @Override
    public void onResume(){
        super.onResume();
        favAdapter.notifyDataSetChanged();
    }

}
