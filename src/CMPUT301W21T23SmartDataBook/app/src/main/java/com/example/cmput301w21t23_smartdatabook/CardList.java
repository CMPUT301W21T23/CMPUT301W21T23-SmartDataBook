package com.example.cmput301w21t23_smartdatabook;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * This classs is simply to display the information from the experiment list adapters onto the screen
 * @Author Afaq
 * @return view the view of the card which contains a couple buttons and breif information about the experiment
 */

public class CardList extends ArrayAdapter<Experiment> {

    private final ArrayList<Experiment> experiments;
    private final Context context;

    public CardList(Context context, ArrayList<Experiment> experiments) {
        super(context,0, experiments);
        this.experiments = experiments;
        this.context = context;
    }

    // set the attributes of each item in the list
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.card,parent,false);
        }

        Experiment experiment = experiments.get(position);

        TextView experimentName = view.findViewById(R.id.experimentName);
        TextView date = view.findViewById(R.id.dateCreated);
        TextView ownerName = view.findViewById(R.id.Owner);
        TextView experimentDescription = view.findViewById(R.id.Experiment_descr);
        TextView region = view.findViewById(R.id.Region);

        experimentName.setText(experiment.getExpName());
        date.setText(experiment.getDate());
        ownerName.setText(experiment.getOwnerUserID());
        experimentDescription.setText(experiment.getDescription());
        region.setText("null");

        return view;
    }
}

