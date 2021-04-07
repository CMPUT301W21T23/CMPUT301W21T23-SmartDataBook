package com.example.cmput301w21t23_smartdatabook.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmput301w21t23_smartdatabook.experiment.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.experiment.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Class: homePage
 * This is a class that composes the home-page of the app
 * The home page initialize and displays the list of experiments
 * It inflate the layout for the experiment's fragment
 * It allows user to add a new experiment by clicking a floating button
 *
 * @author Afaq Nabi, Bosco Chan, Jayden Cho
 * @see Fragment, Firebase
 */

public class HomePage extends Fragment {

	private ListView experimentList;
	private ArrayList<Experiment> experimentDataList;
	private static ArrayAdapter<Experiment> experimentAdapter;
	private MainActivity activity;

	private User user;
	private Database database;

	private String currentQuery;
	private ArrayList<Experiment> searchDataList;

	FirebaseFirestore db = FirebaseFirestore.getInstance();

	public HomePage() {
	}

	public static HomePage newInstance(String user) {
		HomePage fragment = new HomePage();
		Bundle args = new Bundle();
		args.putString("", user);
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * This function updates the Home page, through fragment transaction
	 *
	 * @param query
	 * @param currentFragment
	 */
	public void doUpdate(String query) {

		currentQuery = query.toLowerCase();

		database.fillUserName(new GeneralDataCallBack() {
			@Override
			public void onDataReturn(Object returnedObject) {
				Hashtable<String, User> UserName = (Hashtable<String, User>) returnedObject;
				ArrayList<Experiment> temp = new ArrayList<>();

				for (int i = 0; i < experimentDataList.size(); i++) {
					Experiment experiment = experimentDataList.get(i);
					if (experiment.getExpName().toLowerCase().contains(currentQuery) ||
						UserName.get(experiment.getOwnerUserID()).getUserName().toLowerCase().contains(currentQuery) ||
						experiment.getDate().toLowerCase().contains(currentQuery) ||
						experiment.getDescription().toLowerCase().contains(currentQuery) ||
						experiment.getTrialType().toLowerCase().contains(currentQuery)) temp.add(experiment);
				}

				experimentAdapter = new CardList(getContext(), temp, UserName, 1);
				experimentList.setAdapter(experimentAdapter);

			}
		});

	}

	/**
	 * The onResume function of the Home Page
	 */
	@Override
	public void onResume() {
		super.onResume();
		experimentAdapter.notifyDataSetChanged();
		activity.getSupportActionBar().setTitle("Home");
		activity.onAttachFragment(this);
		activity.setBottomNavigationItem(R.id.home_nav);
	}

	/**
	 * The onCreate function of the Home page
	 * In this functuon, if there are arguments, it gets the user's information
	 *
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			user = User.getUser();
		}
		activity = (MainActivity) getActivity();
		database = new Database();
	}

	/**
	 * The onCreateView function of Home page, the part that most elements on the home page are setted here
	 *
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.home_page, container, false);

		experimentList = view.findViewById(R.id.experiment_list);

		experimentDataList = new ArrayList<>();
		searchDataList = new ArrayList<>();
		experimentAdapter = new CardList(getContext(), experimentDataList, new Hashtable<String, User>(), 1);

		//Source: ColdFire; https://stackoverflow.com/users/886001/coldfire
		//Code: https://stackoverflow.com/questions/7093483/why-listview-items-becomes-not-clickable-after-scroll/7104933
		experimentList.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
					experimentList.invalidateViews();
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
			}
		});

		//Source: Erwin Kurniawan A; https://stackoverflow.com/users/7693494/erwin-kurniawan-a
		//Code: https://stackoverflow.com/questions/61930061/how-to-return-a-value-from-oncompletelistener-while-creating-user-with-email-and
		database.fillUserName(new GeneralDataCallBack() {
			@Override
			public void onDataReturn(Object returnedObject) {
				Hashtable<String, User> UserName = (Hashtable<String, User>) returnedObject;
				database.fillDataList(new GeneralDataCallBack() {
					@Override
					public void onDataReturn(Object returnedObject) {
						ArrayList<Experiment> DataList = (ArrayList<Experiment>) returnedObject;

						experimentAdapter = new CardList(getContext(), experimentDataList, UserName, 1);

						experimentList.setAdapter(experimentAdapter);

						//Reset the experiment adapter for every onCreateView call
						experimentAdapter.clear();
						searchDataList.clear();

						//experimentDataList with added items ONLY exist inside the scope of this getExpDataList function
						experimentDataList = DataList;

						//Create a new searchDataList depending on the query
						if (currentQuery != null) {
							for (Experiment experiment : experimentDataList) {
								if (experiment.getExpName().contains(currentQuery) ||
										UserName.get(experiment.getOwnerUserID()).getUserName().contains(currentQuery) ||
										experiment.getDate().toString().contains(currentQuery) ||
										experiment.getDescription().contains(currentQuery)) {

									searchDataList.add(experiment);

								}
							}
							experimentAdapter.clear();
							experimentAdapter.addAll(searchDataList);
						} else {
							experimentAdapter.addAll(experimentDataList);
						}
						experimentAdapter.notifyDataSetChanged();
						experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								Experiment exp = experimentDataList.get(position); // get the experiment from list
								Intent intent = new Intent(getActivity(), ExperimentDetails.class);
								intent.putExtra("experiment", (Parcelable) exp); // pass experiment object
								startActivity(intent);
							}
						});
					}//getExpDataList
				}, experimentAdapter, db.collection("Experiments"), user.getUserUniqueID(), UserName);//fillDataList
			}
		});

		// The floating action button is used to add new experiments in the home page
		final FloatingActionButton addExperimentButton = view.findViewById(R.id.add_experiment_button);
		addExperimentButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Bundle args = new Bundle();
				args.putString("UUID", user.getUserUniqueID());

				//Source: Shweta Chauhan; https://stackoverflow.com/users/6021469/shweta-chauhan
				//Code: https://stackoverflow.com/questions/40085608/how-to-pass-data-from-one-fragment-to-previous-fragment
				addExpFragment addExpFrag = new addExpFragment();

				addExpFrag.setArguments(args);

				addExpFrag.setTargetFragment(HomePage.this, 0);
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.container, addExpFrag, "addExpFragment");
				ft.addToBackStack("addExpFragment");
				ft.commit();
			}
		});

		return view;

	}//onCreateView

	//Source: Shweta Chauhan; https://stackoverflow.com/users/6021469/shweta-chauhan
	//Code: https://stackoverflow.com/questions/40085608/how-to-pass-data-from-one-fragment-to-previous-fragment

	/**
	 * Custom on activity result function that gets an experiment object from the second fragment
	 * that had been started from this fragment (homePage.java).
	 *
	 * @param requestCode Determines which object is wanted from a fragment
	 * @param resultCode  Determines what the result is when taken
	 * @param data        The intent that holds the serialized object
	 * @author Bosco Chan
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("onBack", "working?");

		int addExpFragmentResultCode = 1;
		int addExpFragmentRequestCode = 0;

		if (resultCode == addExpFragmentResultCode) {
			if (requestCode == addExpFragmentRequestCode) {
				Experiment newExperiment = (Experiment) data.getSerializableExtra("newExp");
				experimentAdapter.add(newExperiment);
				CollectionReference experimentsCollection = db.collection("Experiments");
				database.addExperimentToDB(newExperiment, experimentsCollection, user.getUserUniqueID());
				experimentAdapter.notifyDataSetChanged();
			}
		}

	}//onActivityResult


}//homePage
