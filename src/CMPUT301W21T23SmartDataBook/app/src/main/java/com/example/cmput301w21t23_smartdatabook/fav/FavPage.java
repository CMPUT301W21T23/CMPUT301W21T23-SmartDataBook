package com.example.cmput301w21t23_smartdatabook.fav;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.cmput301w21t23_smartdatabook.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.fav.CardList;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;

import java.util.ArrayList;

public class FavPage extends Fragment {
    private static final String AP1 = "AP1";
    private static final String AP2 = "AP2";
    ListView experimentList;
    ArrayAdapter<Experiment> experimentAdapter;
    ArrayList<Experiment> experimentDataList;

    public FavPage(){
    }

    public static FavPage newInstance(String p1, String p2){
        FavPage fragment = new FavPage();
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

        View view = inflater.inflate(R.layout.followed_experiments, container, false);
        // TODO: add code here

        experimentList = view.findViewById(R.id.followedExpListView);
        experimentDataList = new ArrayList<>();

        experimentDataList.add(new Experiment("first", "123", "Binomial", "testtrial", false, 30, 60, true, "03/05/2021"));
        experimentDataList.add(new Experiment("second", "123", "Binomial", "testtrial", false, 30, 60, true, "03/05/2021"));

//        fillDataList(experimentDataList);

        experimentAdapter = new CardList(getContext(), experimentDataList);
        experimentList.setAdapter(experimentAdapter);

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

        return view;
    }
}
