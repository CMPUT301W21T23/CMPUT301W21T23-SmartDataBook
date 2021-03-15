package com.example.cmput301w21t23_smartdatabook.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cmput301w21t23_smartdatabook.CardList;
import com.example.cmput301w21t23_smartdatabook.CommentActivity;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.MainActivity;
import com.example.cmput301w21t23_smartdatabook.R;
<<<<<<< Updated upstream
import com.google.android.gms.tasks.OnCompleteListener;
=======
import com.example.cmput301w21t23_smartdatabook.experimentDetails;
>>>>>>> Stashed changes
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

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
    }//onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_page, container, false);

        ListView experimentList;
        ArrayAdapter<Experiment> experimentAdapter;
        ArrayList<Experiment> experimentDataList;

        experimentList = view.findViewById(R.id.experimentList);
        experimentDataList = new ArrayList<>();

        experimentDataList.add(new Experiment("first", "123",
                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));
        experimentDataList.add(new Experiment("second", "123",
                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));
        experimentDataList.add(new Experiment("third", "123",
                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));
<<<<<<< Updated upstream
=======
        experimentDataList.add(new Experiment("fourth", "123",
                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));
        experimentDataList.add(new Experiment("fifth", "123","Binomial", "testtrial", false, 30,60, true, "03/05/2021"));
>>>>>>> Stashed changes

        experimentAdapter = new CardList(getContext(), experimentDataList);

        experimentList.setAdapter(experimentAdapter);

        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                startActivity(intent);
            }
        });

<<<<<<< Updated upstream
=======
        final FloatingActionButton addExperimentButton = view.findViewById(R.id.add_experiment_button);
        addExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Source: Shweta Chauhan; https://stackoverflow.com/users/6021469/shweta-chauhan
                //Code: https://stackoverflow.com/questions/40085608/how-to-pass-data-from-one-fragment-to-previous-fragment
                addExpFragment addExpFrag = new addExpFragment();
                addExpFrag.setTargetFragment(homePage.this, 0);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, addExpFrag, "addExpFragment")
                        .addToBackStack("addExpFragment")
                        .commit();

            }
        });

        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("test","clicked");
                Experiment exp = experimentDataList.get(position); // get the experiment from list
                Intent intent = new Intent(getActivity(), experimentDetails.class);
                intent.putExtra("position", position); // pass position to experimentDetails class
                intent.putExtra("experiment", exp); // pass experiment object
                startActivity(intent);
            }
        });

>>>>>>> Stashed changes
        return view;

    }//onCreateView

<<<<<<< Updated upstream
=======
    @Override
    public void onResume() {
        super.onResume();
//        LoaderManager.getInstance(this).restartLoader(0, null, this);

    }

    //Source: Shweta Chauhan; https://stackoverflow.com/users/6021469/shweta-chauhan
    //Code: https://stackoverflow.com/questions/40085608/how-to-pass-data-from-one-fragment-to-previous-fragment
    /**
     * Custom on activity result function that gets an experiment object from the second fragment
     * that had been started from this fragment (homePage.java).
     * @param requestCode Determines which object is wanted from a fragment
     * @param resultCode Determines what the result is when taken
     * @param data The intent that holds the serialized object
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int addExpFragmentResultCode = 1;
        int addExpFragmentRequestCode = 0;

        if (resultCode == addExpFragmentResultCode) {
            if (requestCode == addExpFragmentRequestCode){
                Experiment newExperiment = (Experiment) data.getSerializableExtra("newExp");
                Toast.makeText(getActivity(), newExperiment.getExpName() + " " + newExperiment.getDescription() , Toast.LENGTH_SHORT).show();
                experimentAdapter.add(newExperiment);
                addExperimentToDB(newExperiment);
                experimentAdapter.notifyDataSetChanged();
            }
        }
    }//onActivityResult

    /**
     * Add a new experiment object to the Firebase database.
     * @param newExperiment The experiment object that is to be added to the Firebase database.
     */
    public void addExperimentToDB(Experiment newExperiment) {

        //Add into a Comments collection with a comment document containing
        //a Pies collection with a pie document
        db = FirebaseFirestore.getInstance();
        final CollectionReference allCommentsCollection = db.collection("Experiments");
        HashMap<String, String> data = new HashMap<>();

        // If there’s some data in the EditText field, then we create a new key-value pair.
        data.put("Name", newExperiment.getExpName());
        data.put("Description", newExperiment.getDescription());
        data.put("Trial Type", newExperiment.getTrialType());
        data.put("LocationStatus", ""+ newExperiment.getRegionOn());
        data.put("PublicStatus", ""+ newExperiment.isPublic());
        data.put("UUID", newExperiment.getOwnerUserID());
>>>>>>> Stashed changes

}//homePage
