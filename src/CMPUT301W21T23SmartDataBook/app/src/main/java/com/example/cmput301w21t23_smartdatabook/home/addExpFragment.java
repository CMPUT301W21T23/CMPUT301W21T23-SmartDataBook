package com.example.cmput301w21t23_smartdatabook.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
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

    private Experiment newExperiment = new Experiment("", "",
            "", "", false, 1,2, false, "03/05/2021");

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
        SwitchMaterial LocationToggle = view.findViewById(R.id.ExperimentLocationToggleSwitch);
        SwitchMaterial PublicPrivateToggle = view.findViewById(R.id.ExperimentLocationPublicPrivateToggleSwitch);

        maxTrials.setMinValue(2);
        maxTrials.setMaxValue(50);
        minTrials.setMinValue(1);
        minTrials.setMaxValue(25);

        //String expName = name.getText().toString();
        // TODO: grab the input in the text fields and add to the firebase
        // TODO: also need to pass the user id to this class

        //Source: Zoftino; https://www.zoftino.com/
        //Code: https://www.zoftino.com//android-number-picker-tutorial
        maxTrials.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                newExperiment.setMaxTrials(picker.getValue());
            }
        });
        minTrials.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                newExperiment.setMinTrials(picker.getValue());
            }
        });

        newExperiment.setExpName( name.getEditText().getText().toString().trim() );
        newExperiment.setDescription( description.getEditText().getText().toString().trim() );

        //Set trialType through Experiment Object function
        newExperiment.setTrialType( newExperiment.findTrialType( trialChoice.getCheckedRadioButtonId() ) );

        newExperiment.setRegionOn(LocationToggle.getShowText());
        newExperiment.setPublic(PublicPrivateToggle.getShowText());

        //newExperiment = new Experiment("sixth", "123", "Binomial", "unique", false, 30,60, true, "03/05/2021");

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

                Log.d("MaxTrials", "Max:" + newExperiment.getMaxTrials() + "|");
                Log.d("MinTrials", "Min:" + newExperiment.getMinTrials() + "|");
                Log.d("EXPERIMENT NAME", "Name:" + newExperiment.getExpName() + "|");
                Log.d("TrialType", "TrialType:" + newExperiment.getTrialType() + "|");
                Log.d("RegionOn", "RegionOn:" + newExperiment.getRegionOn() + "|");
                System.exit(0);

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

}//addExpFragment


