package com.example.cmput301w21t23_smartdatabook.experiment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301w21t23_smartdatabook.stats.StringDate;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.example.cmput301w21t23_smartdatabook.comments.CommentActivity;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.experiment.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

/**
 * This classs is simply to display the information from the experiment list adapters onto the screen
 * @Author Afaq, Bosco, Krutik
 * @return view the view of the card which contains a couple buttons and brief information about the experiment
 */

public class CardList extends ArrayAdapter<Experiment> {

    private ArrayList<Experiment> experiments;
    private Context context;
    private int index;
    private User user = User.getUser();

    private Hashtable<String, User> UserName;

    StringDate date = new StringDate();

    public ArrayList<Experiment> getExperiments() {
        return experiments;
    }

    Database database;
    FirebaseFirestore db;
    Map<Integer, View> views = new HashMap<Integer, View>();

    /**
     * Public Constructor for the CardList class
     * @param context
     * @param experiments
     * @param index
     */
    public CardList(Context context, ArrayList<Experiment> experiments, Hashtable<String, User> UserName, int index) {
        super(context,0, experiments);
        this.experiments = experiments;
        this.context = context;
        this.index = index;
        this.UserName = UserName;
    }

    /**
     * This function:
     * Initialize elements, like inflates the layout, sets attributes of the name, date, ownerName, experimentDescription and region in the list
     * Display the visual representation of each experiment card(textview, Button, checkbox etc)
     * Setting up onclick for clicking the experiment card, as well as buttons and checkbox on the experiment card
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */

    // we have learned how to handle the issue of listview item's positions changed on scroll, through stack overflow
    //Source: Tautvydas; https://stackoverflow.com/users/951894/tautvydas
    //Code: https://stackoverflow.com/questions/22919417/listview-items-change-position-on-scroll/22919488
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        View view1 = null;

        LayoutInflater inflater;
        database = new Database();
        db = FirebaseFirestore.getInstance();

        Experiment experiment = experiments.get(position);

        if (index == 1) {

            if (views.containsKey(position) ){
                return views.get(position);
            }

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.card, null);

            v.setFocusable(false);

            TextView experimentName = v.findViewById(R.id.experimentName);
            TextView dateView = v.findViewById(R.id.dateCreated);
            TextView ownerName = v.findViewById(R.id.Owner);
            TextView experimentDescription = v.findViewById(R.id.Experiment_descr);
            TextView region = v.findViewById(R.id.Region);

            if (!experiment.getRequireLocation()){
                ((ViewGroup)region.getParent()).removeView(region);
            }

            experimentName.setText(experiment.getExpName());
            dateView.setText(""+ date.getDate(experiment.getDate()));
            ownerName.setText(experiment.getOwnerUserName());
            experimentDescription.setText(experiment.getDescription());

            // when the user click the comment button
            Button comment = v.findViewById(R.id.comment_btn);
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getContext(), CommentActivity.class);
                    intent.putExtra("Experiment", (Parcelable) experiment);
                    intent.putExtra("CurrentID", user.getUserUniqueID());
                    intent.putExtra("user", user);
                    context.startActivity(intent);
                }
            });
            // We have learned the idea of CheckBox from developer.android.com
            // URL: https://developer.android.com/reference/android/widget/CheckBox
            CheckBox follow = v.findViewById(R.id.fav);
            db
                    .collection("Users")
                    .document(user.getUserUniqueID())
                    .collection("Favorites")
                    .document(experiment.getExpID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            follow.setChecked(true);
                        }
                    }
                }
            });

            // gets the favourite experiments, by using an if-else statement
            follow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                  @Override
                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                      if(isChecked){
                          final CollectionReference favExpCollection = db.collection("Users")
                                  .document(user.getUserUniqueID())
                                  .collection("Favorites");
                          database.addExperimentToDB(experiment, favExpCollection, user.getUserUniqueID());

                      } else {

                              final DocumentReference ref = db.collection("Users")
                                      .document(user.getUserUniqueID())
                                      .collection("Favorites")
                                      .document(experiment.getExpID());

                              database.followStatus( ref, experiment, getContext(), follow, user.getUserUniqueID() );
                          }
                      }
                  }
            );


            View userInfoView = LayoutInflater.from(getContext()).inflate(R.layout.view_profile, null);

            TextView username = userInfoView.findViewById(R.id.expOwner);
            username.setText("Username: " + experiment.getOwnerUserName());

            TextView email = userInfoView.findViewById(R.id.expContact);;
            email.setText("Email: " + UserName.get(experiment.getOwnerUserID()).getUserContact() );

            // when the user clicks the experiment's owner name
            ownerName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Source: Johnny Five; https://stackoverflow.com/users/6325722/johnny-five
                    //Code: https://stackoverflow.com/questions/28071349/the-specified-child-already-has-a-parent-you-must-call-removeview-on-the-chil
                    if (userInfoView.getParent() != null) {
                        ((ViewGroup) userInfoView.getParent()).removeView(userInfoView);
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(userInfoView)
                            .setNegativeButton("Close", null).create().show();
                }
            });

            views.put(position, v);

            return v;

        }

        return view;
    }

}

