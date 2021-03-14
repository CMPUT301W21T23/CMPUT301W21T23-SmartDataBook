package com.example.cmput301w21t23_smartdatabook.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.MainActivity;
import com.example.cmput301w21t23_smartdatabook.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

/**
 * @Author Afaq
 * This class is the activity of which contains the add experiment, it has:
 * An experiment's minimum and maximum number of trials
 * An experiment's name and description
 * Radio button to choose which type of the trial the experiment has (binomial/ measurement/count/ non-negtaive count trials)
 * switch button that turns on/ off an experiment's trial location.
 * a back button that allows the user to go back
 * @author Afaq Nabi, Bosco Chan
 * @see xml files that is associated with this addExpFragment
 * @version 1
 */
public class addExpFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private static final int binomialID = 10;
    private static final int countID = 11;
    private static final int nonNegativeID = 12;
    private static final int measurementID = 13;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.new_experiment_location_on, container, false);

        ( (AppCompatActivity) getActivity() ).getSupportActionBar().hide();

        NumberPicker maxTrials = view.findViewById(R.id.maxTrialsNumberPicker);
        NumberPicker minTrials = view.findViewById(R.id.minTrialsNumberPicker);

        TextInputLayout name = view.findViewById(R.id.experimentNameField);
        TextInputLayout description = view.findViewById(R.id.descriptionField);

        RadioGroup trialChoice = view.findViewById(R.id.typeoFTrialRadioGroup);
        RadioButton binomial = view.findViewById(R.id.binomialRadioButton);
        RadioButton count = view.findViewById(R.id.countRadioButton);
        RadioButton nonNegative = view.findViewById(R.id.nonNegativeRadioButton);
        RadioButton measurement = view.findViewById(R.id.measurmentRadioButton);

        SwitchMaterial LocationToggle = view.findViewById(R.id.ExperimentLocationToggleSwitch);
        SwitchMaterial PublicPrivateToggle = view.findViewById(R.id.ExperimentLocationPublicPrivateToggleSwitch);

        //Source: user; https://stackoverflow.com/users/493939/user
        //Code: https://stackoverflow.com/questions/10356733/getcheckedradiobuttonid-returning-useless-inthttps://stackoverflow.com/questions/10356733/getcheckedradiobuttonid-returning-useless-int
        binomial.setId(binomialID);
        count.setId(countID);
        nonNegative.setId(nonNegativeID);
        measurement.setId(measurementID);

        //Source: Zoftino; https://www.zoftino.com/
        //Code: https://www.zoftino.com//android-number-picker-tutorial
        maxTrials.setMinValue(2);
        maxTrials.setMaxValue(50);
        minTrials.setMinValue(1);
        minTrials.setMaxValue(25);

        final AppCompatImageButton back_btn = view.findViewById(R.id.newExperimentLocationOnBackButtonView);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        final Button addExperiment = view.findViewById(R.id.newExperimentLocationOnCreateButtonView);
        addExperiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String expName = name.getEditText().getText().toString();
                String expDescription = description.getEditText().getText().toString();
                String trialType = findTrialType(trialChoice.getCheckedRadioButtonId());

                Experiment newExperiment = new Experiment(expName, "123", trialType, expDescription, true, minTrials.getValue(), maxTrials.getValue(), true, "03/05/2021");

                //Source: Shweta Chauhan; https://stackoverflow.com/users/6021469/shweta-chauhan
                //Code: https://stackoverflow.com/questions/40085608/how-to-pass-data-from-one-fragment-to-previous-fragment
                Intent intent = new Intent(getActivity(), homePage.class);
                intent.putExtra("newExp", newExperiment);
                getTargetFragment().onActivityResult(getTargetRequestCode(), 1, intent);
                getFragmentManager().popBackStack();

            }
        });

        return view;

    }//onCreateView
    

    /**
     * Gets the integer value "i" from the RadioGroup and determines what the trial type of the
     * experiment is based on the given "i" value.
     * @author Bosco Chan
     * @param trialTypeID Holds the int value to determine what String the trialType is.
     * @return trialType
     */
    public String findTrialType(int trialTypeID) {
        switch(trialTypeID) {
            case binomialID:
                return "Binomial";
            case countID:
                return "Count";
            case nonNegativeID:
                return "Non-Negative Count";
            default:
                return "Measurement";
        }

    }//findTrialType

}//addExpFragment


