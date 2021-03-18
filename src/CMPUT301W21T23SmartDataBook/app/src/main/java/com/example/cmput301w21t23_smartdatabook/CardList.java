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
import java.util.HashMap;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This classs is simply to display the information from the experiment list adapters onto the screen
 * @Author Afaq, Bosco
 * @return view the view of the card which contains a couple buttons and brief information about the experiment
 */

public class CardList extends ArrayAdapter<Experiment> {

    private final ArrayList<Experiment> experiments;
    private final Context context;
    private final int index;

    public ArrayList<Experiment> getExperiments() {
        return experiments;
    }

    Database database = new Database();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * Public Constructor for the CardList class
     * @param context
     * @param experiments
     * @param index
     */
    public CardList(Context context, ArrayList<Experiment> experiments, int index) {
        super(context,0, experiments);
        this.experiments = experiments;
        this.context = context;
        this.index = index;
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
        View view1 = null;

        Experiment experiment = experiments.get(position);

        if (index == 1) {

            if (view == null){
                view = LayoutInflater.from(context).inflate(R.layout.card, parent,false);
            }

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

            // https://developer.android.com/reference/android/widget/CheckBox
            CheckBox follow = view.findViewById(R.id.fav);
            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final CollectionReference favExpCollection = db.collection("Users")
                            .document(Objects.requireNonNull(mAuth.getUid()))
                            .collection("Favorites");

                    database.addExperimentToDB(experiment, favExpCollection);

                    follow.setChecked(true);
                }
            });

            return view;

        } else if (index == 2){

            if (view1 == null){
                view1 = LayoutInflater.from(context).inflate(R.layout.followed_experiments_items, parent,false);
            }

            assert view1 != null;
            TextView experimentName = view1.findViewById(R.id.ExpNameTextView);
            TextView ownerName = view1.findViewById(R.id.ownerTextView);

            experimentName.setText(experiment.getExpName());
            ownerName.setText(experiment.getOwnerUserID());

            return view1;

        }

        return view;
    }

}

