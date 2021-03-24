package com.example.cmput301w21t23_smartdatabook.home;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301w21t23_smartdatabook.comments.CommentActivity;
import com.example.cmput301w21t23_smartdatabook.Database;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This classs is simply to display the information from the experiment list adapters onto the screen
 * @Author Afaq, Bosco, Krutik
 * @return view the view of the card which contains a couple buttons and brief information about the experiment
 */

public class
CardList extends ArrayAdapter<Experiment> {

    private final ArrayList<Experiment> experiments;
    private final Context context;
    private final int index;
    private final String currentID;

    public ArrayList<Experiment> getExperiments() {
        return experiments;
    }

    Database database;
    FirebaseFirestore db;

    /**
     * Public Constructor for the CardList class
     * @param context
     * @param experiments
     * @param index
     */
    public CardList(Context context, ArrayList<Experiment> experiments, int index, String currentID) {
        super(context,0, experiments);
        this.experiments = experiments;
        this.context = context;
        this.index = index;
        this.currentID = currentID;
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

        database = new Database();
        db = FirebaseFirestore.getInstance();

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
//                    Log.d("test", experiment.getExpName());
                    Intent intent = new Intent(getContext(), CommentActivity.class);
                    context.startActivity(intent);
                }
            });

            // https://developer.android.com/reference/android/widget/CheckBox
            CheckBox follow = view.findViewById(R.id.fav);

            db.collection("Users")
                    .document(experiment.getOwnerUserID())
                    .collection("Favorites")
                    .document(experiment.getExpID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            follow.setChecked(true);
                        }
                    }
                }
            });

            follow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                      @Override
                      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                          if(isChecked){
                              final CollectionReference favExpCollection = db.collection("Users")
                                      .document(experiment.getOwnerUserID())
                                      .collection("Favorites");

                              database.addExperimentToDB(experiment, favExpCollection, currentID);

                              System.out.println("Checked");

                          } else {

                              final DocumentReference ref = db.collection("Users")
                                      .document(experiment.getOwnerUserID())
                                      .collection("Favorites")
                                      .document(experiment.getExpID());

                              database.followStatus( ref, experiment, getContext(), follow, currentID );

                              System.out.println("Un-Checked");
                          }
                      }
                  }
            );

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

