package com.example.cmput301w21t23_smartdatabook.fav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.cmput301w21t23_smartdatabook.R;

public class favPage extends Fragment {
    private static final String AP1 = "AP1";
    private static final String AP2 = "AP2";

    public favPage(){
    }

    public static favPage newInstance(String p1, String p2){
        favPage fragment = new favPage();
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
        return view;
    }
}
