package com.example.cmput301w21t23_smartdatabook.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.cmput301w21t23_smartdatabook.CardList;
import com.example.cmput301w21t23_smartdatabook.CommentActivity;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.experimentDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Class: homePage
 * This is a class that composes the home-page of the app
 * The home page initialize and displays the list of experiments
 * It inflate the layout for the experiment's fragment
 * It allows user to add a new experiment by clicking a floating button
 * 
 * @author Afaq Nabi, Bosco Chan 
 * @see Fragment, Firebase
 * @version
 */

public class homePage extends Fragment {

    private static final String AP1 = "AP1";
    private static final String AP2 = "AP2";

    public homePage(){
    }

    public static homePage newInstance(String p1, String p2){
        homePage fragment = new homePage();
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
        View view = inflater.inflate(R.layout.home_page, container, false);

        ListView experimentList;
        ArrayAdapter<Experiment> experimentAdapter;
        ArrayList<Experiment> experimentDataList;

        FloatingActionButton addExperimentButton = view.findViewById(R.id.add_experiment_button);

        experimentList = view.findViewById(R.id.experimentList);
        experimentDataList = new ArrayList<>();

        experimentDataList.add(new Experiment("first", "123",
                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));
        experimentDataList.add(new Experiment("second", "123",
                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));
        experimentDataList.add(new Experiment("third", "123",
                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));

        experimentAdapter = new CardList(getContext(), experimentDataList);

        experimentList.setAdapter(experimentAdapter);

        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Experiment exp = experimentDataList.get(position); // get the experiment from list
                Intent intent = new Intent(getActivity(), experimentDetails.class);
                intent.putExtra("position", position); // pass position to experimentDetails class
                intent.putExtra("experiment", exp); // pass experiment object
                startActivity(intent);
            }
        });

        addExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), addExpActivity.class);
                startActivity(intent);

                // new ExperimentFragment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
            }
        });

        return view;
    }


    /**
     * Add an experiment by clicking the floating button
     * if there is something on the edittext field, a key value pair is added to the FireStore.
     * @param view
     */
    public void addExperiment(View view) {

        //addCommentBtn testing - add comments via btn to database
        final FloatingActionButton addCommentBtn = findViewById(R.id.addCommentBtn);

        EditText commentText = findViewById(R.id.editCommentText);
        EditText userNameText = findViewById(R.id.editUserNameText);

        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(commentText.getText().toString().length() != 0 || userNameText.getText().toString().length() !=0 ) {
                    Comment newComment = new Comment(commentText.getText().toString(), userNameText.getText().toString(), commentDataList.size() + 1);
                    commentDataList.add(newComment);
                    commentAdapter.notifyDataSetChanged();

                    //Add into a Comments collection with a comment document containing
                    //a Pies collection with a pie document
                    db = FirebaseFirestore.getInstance();
                    final CollectionReference allCommentsCollection = db.collection("Comments");
                    HashMap<String, String> data = new HashMap<>();

                    if(commentText.length()>0 && userNameText.length()>0){
                        // If there’s some data in the EditText field, then we create a new key-value pair.
                        data.put("Username", userNameText.getText().toString());
                        data.put("Comment", commentText.getText().toString());

                        allCommentsCollection
                                .document("" + newComment.getCommentID())
                                .set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Success", "Data has been added successfully");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Failure", "Data storing failed");
                                    }
                                });

                        data.clear();
                        data.put("Pie Type", "Chocolate");
                        allCommentsCollection
                                .document("" + newComment.getCommentID())
                                .collection("Pies")
                                .document("ChocoPie")
                                .set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Success", "Data has been added successfully");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Failure", "Data storing failed");
                                    }
                                });

                        // Setting the fields to null so that user can add a new city
                        commentText.setText("");
                        userNameText.setText("");
                    }else{
                        Toast.makeText(MainActivity.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
                    }//if-else


                }//if

            }//onClick

        });

    }//addExperiment

}
