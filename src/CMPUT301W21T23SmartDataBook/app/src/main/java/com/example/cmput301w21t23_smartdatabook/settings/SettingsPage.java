package com.example.cmput301w21t23_smartdatabook.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * THe User Contact page where users can change and edit their information freely
 * @author Krutik, Natnail, Jayden Cho
 */

// TODO: change name to User contact instead of settings
public class SettingsPage extends Fragment {
	public EditText usernameTextField;
	public EditText contactTextField;

	public Button saveButtonView;

	private User user;
	private MainActivity activity;

	public Database database = new Database();

	public SettingsPage() {
	}

	public static SettingsPage newInstance(String user) {
		SettingsPage fragment = new SettingsPage();
		Bundle args = new Bundle();
		args.putString("", user);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
    public void onResume() {
        super.onResume();
        activity.getSupportActionBar().setTitle("Settings");
        activity.onAttachFragment(this);
        activity.setBottomNavigationItem(R.id.settings_nav);
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			user = User.getUser();
		}
		activity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.settings, container, false);
		usernameTextField = (EditText) view.findViewById(R.id.usernameTextField);
		contactTextField = (EditText) view.findViewById(R.id.emailTextField);
		saveButtonView = (Button) view.findViewById(R.id.saveButtonView);

		usernameTextField.setHint(user.getUserName());

		database.editUser(usernameTextField, contactTextField, saveButtonView, getContext(), user.getUserUniqueID(), user);

		return view;
	}
}
