package com.example.cmput301w21t23_smartdatabook.fav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.cmput301w21t23_smartdatabook.CardList;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;

import java.util.ArrayList;

public class favList extends Fragment {
    ListView experimentList;
    ArrayAdapter<Experiment> experimentAdapter;
    ArrayList<Experiment> experimentDataList;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.followed_experiments, container, false);

        experimentList = view.findViewById(R.id.followedExpListView);
        experimentDataList = new ArrayList<>();

        experimentDataList.add(new Experiment("first", "123", "Binomial", "testtrial", false, 30, 60, true, "03/05/2021"));
        experimentDataList.add(new Experiment("second", "123", "Binomial", "testtrial", false, 30, 60, true, "03/05/2021"));

//        fillDataList(experimentDataList);

        experimentAdapter = new CardList(getContext(), experimentDataList);
        experimentList.setAdapter(experimentAdapter);

        return view;
    }

}
