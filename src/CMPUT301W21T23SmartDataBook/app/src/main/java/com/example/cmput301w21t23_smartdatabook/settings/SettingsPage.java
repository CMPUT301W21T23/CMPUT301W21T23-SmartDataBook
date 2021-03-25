package com.example.cmput301w21t23_smartdatabook.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.cmput301w21t23_smartdatabook.Database;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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
	private String currentID;

	private User user;

	public Database database = new Database();

	public SettingsPage() {
	}

	public static SettingsPage newInstance(User user) {
		SettingsPage fragment = new SettingsPage();
		Bundle args = new Bundle();
		args.putSerializable("user", user);
		fragment.setArguments(args);
		return fragment;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			user = (User) getArguments().getSerializable("user");
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.settings, container, false);
		usernameTextField = (EditText) view.findViewById(R.id.usernameTextField);
		emailTextField = (EditText) view.findViewById(R.id.emailTextField);
		saveButtonView = (Button) view.findViewById(R.id.saveButtonView);

		usernameTextField.setHint(user.getUserName());


		Toast.makeText(getContext(), "" + user.getUserUniqueID(), Toast.LENGTH_LONG).show();
		database.editUser(usernameTextField, emailTextField, saveButtonView, getContext(), user.getUserUniqueID());

		return view;
	}
}
