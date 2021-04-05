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
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * THe User Contact page where users can change and edit their information freely
 * @author Krutik, Natnail
 */

// TODO: change name to User contact instead of settings
public class SettingsPage extends Fragment {
	public EditText usernameTextField;
	public EditText emailTextField;

	public Button saveButtonView;

	private User user;

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			user = User.getUser();
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
		database.editUser(usernameTextField, emailTextField, saveButtonView, getContext(), user.getUserUniqueID(), user);

		return view;
	}
}
