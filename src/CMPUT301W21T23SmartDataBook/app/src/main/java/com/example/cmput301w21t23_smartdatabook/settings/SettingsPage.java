package com.example.cmput301w21t23_smartdatabook.settings;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cmput301w21t23_smartdatabook.Database;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.userIDString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class SettingsPage extends Fragment {
    private static final String AP1 = "AP1";
    private static final String AP2 = "AP2";
    private static final String TAG1 = "Your";
    private static final String TAG2 = "Warning";
    private static final String TAG3 = "Exception";

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public EditText usernameTextField;
    public EditText emailTextField;

    public Button saveButtonView;

    public FirebaseUser currentUser;
    public Database database = new Database();

    public SettingsPage(){

    }

    public static SettingsPage newInstance(String p1, String p2){
        SettingsPage fragment = new SettingsPage();
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
        usernameTextField = (EditText) view.findViewById(R.id.usernameTextField);
        emailTextField = (EditText) view.findViewById(R.id.emailTextField);
        saveButtonView = (Button) view.findViewById(R.id.saveButtonView);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mAuth.signInAnonymously();
        database.editUser(usernameTextField, emailTextField, saveButtonView, getContext());

//        userIDString uid = null;
//        String uuid = uid.getCurrentUser();

        Log.d("MESSAGE", "UID: "+ currentUser+" Stirng: "+currentUser.getUid());

        return view;
    }
}
