package com.example.cmput301w21t23_smartdatabook;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TrialList extends ArrayAdapter<Trial> {
    private final ArrayList<Trial> trials;
    private final Context context;

    public TrialList(ArrayList<Trial> trials, Context context) {
        super(context,0, trials);
        this.trials = trials;
        this.context = context;
    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;


        return view;
    }
}
