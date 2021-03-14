package com.example.cmput301w21t23_smartdatabook.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.MainActivity;
import com.example.cmput301w21t23_smartdatabook.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.new_experiment_location_on, container, false);

        ( (AppCompatActivity) getActivity() ).getSupportActionBar().hide();

        NumberPicker maxTrials = view.findViewById(R.id.maxTrialsNumberPicker);
        NumberPicker minTrials = view.findViewById(R.id.minTrialsNumberPicker);
        TextInputEditText name = view.findViewById(R.id.newExperimentLocationOnExperimentNameEditText);
        TextInputEditText description = view.findViewById(R.id.newExperimentLocationOnExperimentDescriptionEditText);
        RadioGroup trialChoice = view.findViewById(R.id.typeoFTrialRadioGroup);
        SwitchMaterial LocationToggle = view.findViewById(R.id.ExperimentLocationToggleSwitch);
        SwitchMaterial PublicPrivateToggle = view.findViewById(R.id.ExperimentLocationPublicPrivateToggleSwitch);

        //String expName = name.getText().toString();
        // TODO: grab the input in the text fields and add to the firebase
        // TODO: also need to pass the user id to this class

        //Get the new experiment that was bundled towards addExpFragment
//        final Experiment newExperiment = new Experiment("third", "123",
//                "Binomial", "testtrial", false, 30,60, true, "03/05/2021");
//
//        //Set experiment object values
//        newExperiment.setMaxTrials(maxTrials.getValue());
//        newExperiment.setMinTrials(minTrials.getValue());
//        newExperiment.setExpName(Objects.requireNonNull(name.getText()).toString());
//        newExperiment.setDescription(Objects.requireNonNull(description.getText()).toString());
//
//        //Set trialType through Experiment Object function
//        newExperiment.setTrialType( newExperiment.findTrialType( trialChoice.getCheckedRadioButtonId() ) );
//
//        newExperiment.setRegionOn(LocationToggle.getShowText());
//        newExperiment.setPublic(PublicPrivateToggle.getShowText());

        final Experiment newExperiment = new Experiment("fourth", "123",
                "Binomial", "unique", false, 30,60, true, "03/05/2021");

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

                //Source: Shweta Chauhan; https://stackoverflow.com/users/6021469/shweta-chauhan
                //Code: https://stackoverflow.com/questions/40085608/how-to-pass-data-from-one-fragment-to-previous-fragment
                Intent intent = new Intent(getActivity(), homePage.class);
                intent.putExtra("newExp", newExperiment);
                getTargetFragment().onActivityResult(getTargetRequestCode(), 1, intent);
                getFragmentManager().popBackStack();

            }
        });

        return view;

    }


}
