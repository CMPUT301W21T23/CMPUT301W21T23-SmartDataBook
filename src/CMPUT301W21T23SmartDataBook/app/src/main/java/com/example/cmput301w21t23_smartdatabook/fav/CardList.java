package com.example.cmput301w21t23_smartdatabook.fav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;

import java.util.ArrayList;

public class CardList extends ArrayAdapter<Experiment> {

    private final ArrayList<Experiment> experiments;
    private final Context context;

    /**
     * Public Constructor for the CardList class
     *
     * @param context
     * @param experiments
     */
    public CardList(Context context, ArrayList<Experiment> experiments) {
        super(context, 0, experiments);
        this.experiments = experiments;
        this.context = context;
    }


    /**
     * Set attributes of the name, date, ownerName, experimentDescription and region in the list
     *
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.followed_experiments_items, parent, false);
        }

        Experiment experiment = experiments.get(position);

        TextView experimentName = view.findViewById(R.id.ExpNameTextView);
        TextView ownerName = view.findViewById(R.id.ownerTextView);

        experimentName.setText(experiment.getExpName());
        ownerName.setText(experiment.getOwnerUserID());

        return view;
    }
}