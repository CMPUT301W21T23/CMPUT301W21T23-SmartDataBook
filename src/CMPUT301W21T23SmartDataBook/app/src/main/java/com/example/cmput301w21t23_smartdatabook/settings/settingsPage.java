package com.example.cmput301w21t23_smartdatabook.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cmput301w21t23_smartdatabook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import org.w3c.dom.Text;

import java.util.Map;

public class settingsPage extends Fragment {
    private static final String AP1 = "AP1";
    private static final String AP2 = "AP2";
    private static final String TAG1 = "Your";
    private static final String TAG2 = "Warning";
    private static final String TAG3 = "Exception";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public TextView usernameTxtView;
    public TextView emailTxtView;
    public Button editButtonSettings;
    public FirebaseUser currentUser;

    public settingsPage(){

    }

    public static settingsPage newInstance(String p1, String p2){
        settingsPage fragment = new settingsPage();
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
        View view = inflater.inflate(R.layout.settings, container, false);
        usernameTxtView = (TextView) view.findViewById(R.id.usernameTxtView);
        emailTxtView = (TextView) view.findViewById(R.id.emailTxtView);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        DocumentReference docRef = db.collection("Users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG1, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> data = document.getData();

                        if (data.get("UserName").toString() == ""){
                            usernameTxtView.setText("You have no Username!");
                        }else{
                            usernameTxtView.setText(data.get("UserName").toString());
                        }

                        if (data.get("Email").toString().equals("")){
                            emailTxtView.setText("You have no Email!");
                        }else{
                            emailTxtView.setText(data.get("Email").toString());
                        }


                    } else {
                        Log.d(TAG2, "No such document");
                    }
                } else {
                    Log.d(TAG3, "get failed with ", task.getException());
                }
            }
        });
        return view;
    }
}
