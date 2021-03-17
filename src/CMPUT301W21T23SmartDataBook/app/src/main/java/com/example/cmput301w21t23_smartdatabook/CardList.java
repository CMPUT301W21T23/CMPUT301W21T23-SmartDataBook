package com.example.cmput301w21t23_smartdatabook;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * This classs is simply to display the information from the experiment list adapters onto the screen
 * @Author Afaq
 * @return view the view of the card which contains a couple buttons and brief information about the experiment
 */

public class CardList extends ArrayAdapter<Experiment> {

    private final ArrayList<Experiment> experiments;
    private final Context context;

    /**
     * Public Constructor for the CardList class
     * @param context
     * @param experiments
     */
    public CardList(Context context, ArrayList<Experiment> experiments) {
        super(context,0, experiments);
        this.experiments = experiments;
        this.context = context;
    }


    /**
     * Set attributes of the name, date, ownerName, experimentDescription and region in the list
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.card, parent,false);
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
        region.setText(null);

        Button comment = view.findViewById(R.id.comment_btn);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", experiment.getExpName());
            }
        });

        //CheckBox follow = view.findViewById(R.id.fav);


        return view;
    }

//    public View getView1(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View view = convertView;
//
//        if (view == null) {
//            view = LayoutInflater.from(context).inflate(R.layout.followed_experiments_items, parent, false);
//        }
//
//        Experiment experiment = experiments.get(position);
//
//        return view;
//    }
}

