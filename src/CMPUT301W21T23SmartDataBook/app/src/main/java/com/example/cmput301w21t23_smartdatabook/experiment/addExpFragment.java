package com.example.cmput301w21t23_smartdatabook.experiment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.stats.StringDate;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.geolocation.LocationWithPermission;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import java.util.UUID;

/**
 * @author Afaq Nabi, Bosco Chan, Jayden Cho
 * This class is the activity of which contains the add experiment, it has:
 * An experiment's minimum and maximum number of trials
 * An experiment's name and description
 * Radio button to choose which type of the trial the experiment has (binomial/ measurement/count/ non-negtaive count trials)
 * switch button that turns on/ off an experiment's trial location.
 * a back button that allows the user to go back
 * @see xml files that is associated with this addExpFragment
 */
public class addExpFragment extends Fragment {

	private static final int binomialID = 10;
	private static final int countID = 11;
	private static final int nonNegativeID = 12;
	private static final int measurementID = 13;

	private boolean checkLocationOn = true;
	private boolean checkPublicOn = true;

	private Experiment returnedExperiment;
	private AppCompatActivity activity;

	/**
	 * The onCreateView function of addExperimentFragment
	 * This functions sets up the visual representation (NumberPicker, TextInputLayout, RadioGroup, Switch Material
	 * This function also displays the errors the user potentially made when they add a new experiment.
	 *
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.new_experiment_location_on, container, false);

		activity = ((AppCompatActivity) getActivity());
		activity.getSupportActionBar().setTitle("Add new experiment");

		String currentID = getArguments().getString("UUID");

		NumberPicker maxTrials = view.findViewById(R.id.maxTrialsNumberPicker);
		NumberPicker minTrials = view.findViewById(R.id.minTrialsNumberPicker);

		TextInputLayout name = view.findViewById(R.id.experimentNameField);
		TextInputLayout description = view.findViewById(R.id.descriptionField);

		RadioGroup trialChoice = view.findViewById(R.id.typeoFTrialRadioGroup);
		RadioButton binomial = view.findViewById(R.id.binomialRadioButton);
		RadioButton count = view.findViewById(R.id.countRadioButton);
		RadioButton nonNegative = view.findViewById(R.id.nonNegativeRadioButton);
		RadioButton measurement = view.findViewById(R.id.measurmentRadioButton);

		SwitchMaterial LocationToggle = view.findViewById(R.id.newExperimentLocationToggleSwitch);
		SwitchMaterial PublicPrivateToggle = view.findViewById(R.id.newExperimentLocationPublishToggleSwitch);

		binomial.setId(binomialID);
		count.setId(countID);
		nonNegative.setId(nonNegativeID);
		measurement.setId(measurementID);

		// we have learned the idea of andorid number picker from the websites below
		//Source: Zoftino; https://www.zoftino.com/
		//Code: https://www.zoftino.com//android-number-picker-tutorial

		maxTrials.setMinValue(2);
		maxTrials.setMaxValue(999);
		minTrials.setMinValue(1);
		minTrials.setMaxValue(50);

		// Commented out for avoiding errors
//      TODO: grab the input in the text fields and add to the firebase
//      TODO: also need to pass the user id to this class

		LocationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checkLocationOn = isChecked;
			}
		});
		PublicPrivateToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checkPublicOn = isChecked;
			}
		});

		final Button back = view.findViewById(R.id.add_exp_back_button);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.getSupportFragmentManager().popBackStack();
			}
		});

		final Button addExperiment = view.findViewById(R.id.newExperimentLocationOnCreateButtonView);
		addExperiment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				//Instantiate currentStringDate object to get the current date
				StringDate currentStringDate = new StringDate();

				String expName = "" + name.getEditText().getText();
				String expDescription = "" + description.getEditText().getText();
				String trialType = findTrialType(trialChoice.getCheckedRadioButtonId());


//                    //Source: Shweta Chauhan; https://stackoverflow.com/users/6021469/shweta-chauhan
//                    //Code: https://stackoverflow.com/questions/40085608/how-to-pass-data-from-one-fragment-to-previous-fragment

				new LocationWithPermission(activity).getLatLng(new GeneralDataCallBack() {
					@Override
					public void onDataReturn(Object returnedObject) {
						Location location = (Location) returnedObject;
						if (location == null) {
						Log.d(getClass().getSimpleName(), "150: why is it not working, makes no sense");
							Toast.makeText(activity.getApplicationContext(), "Please open up the google map and obtain your location at least once.", Toast.LENGTH_LONG).show();
							return;
						}
						LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

						if (expName == "" || expDescription == "") {
							Toast.makeText(getContext(), "The name or description can't be empty.", Toast.LENGTH_SHORT).show();
						} else if (minTrials.getValue() >= maxTrials.getValue()) {
							Toast.makeText(getContext(), "Minimum is larger or equal to maximum.", Toast.LENGTH_SHORT).show();
						} else if (trialType == null) {
							Toast.makeText(getContext(), "A trial type needs to be selected.", Toast.LENGTH_SHORT).show();
						} else {
							returnedExperiment = new Experiment(expName, currentID, "please refresh",
									trialType, expDescription, checkLocationOn, minTrials.getValue(), maxTrials.getValue(),
									checkPublicOn, currentStringDate.getCurrentDate(), UUID.randomUUID().toString(), false, latlng);

							//Source: Shweta Chauhan; https://stackoverflow.com/users/6021469/shweta-chauhan
							//Code: https://stackoverflow.com/questions/40085608/how-to-pass-data-from-one-fragment-to-previous-fragment
							Intent intent = new Intent(getActivity(), addExpFragment.class);
							intent.putExtra("newExp", (Parcelable) returnedExperiment);
							addExpFragment.this.getTargetFragment().onActivityResult(getTargetRequestCode(), 1, intent);
							activity.getSupportFragmentManager().popBackStack();
						}
					}
				});

			}
		});

		return view;

	}//onCreateView

	@Override
	public void onDestroy() {
		super.onDestroy();
		activity.getSupportActionBar().setTitle("Home");
//        getActivity().getSupportFragmentManager().popBackStack();
		BottomNavigationView bottomNavigation = activity.findViewById(R.id.bottom_navigation);
		bottomNavigation.setVisibility(View.VISIBLE);
	}

	/**
	 * Gets the integer value "i" from the RadioGroup and determines what the trial type of the
	 * experiment is based on the given "i" value.
	 *
	 * @param trialTypeID Holds the int value to determine what String the trialType is.
	 * @return trialType
	 * @author Bosco Chan
	 */
	public String findTrialType(int trialTypeID) {
		switch (trialTypeID) {
			case binomialID:
				return "Binomial";
			case countID:
				return "Count";
			case nonNegativeID:
				return "Non-Negative Count";
			case measurementID:
				return "Measurement";
			default:
				return null;
		}

	}//findTrialType

}//addExpFragment


