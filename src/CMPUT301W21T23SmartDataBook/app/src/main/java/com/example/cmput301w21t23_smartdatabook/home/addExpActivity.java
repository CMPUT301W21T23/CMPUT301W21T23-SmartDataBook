package com.example.cmput301w21t23_smartdatabook.home;

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

import com.example.cmput301w21t23_smartdatabook.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

/**
 * @Author Afaq
 * this class is the activity of which contains the add experiment form
 */
public class addExpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_experiment_location_on);

        Intent intent = getIntent();

        NumberPicker maxTrials = findViewById(R.id.maxTrialsNumberPicker);
        NumberPicker minTrials = findViewById(R.id.minTrialsNumberPicker);
        TextInputEditText name = findViewById(R.id.newExperimentLocationOnExperimentNameEditText);
        TextInputEditText description = findViewById(R.id.newExperimentLocationOnExperimentDescriptionEditText);
        RadioButton binomialRadioButton = findViewById(R.id.binomialRadioButton);
        RadioButton countRadioButton = findViewById(R.id.countRadioButton);
        RadioButton nonNegativeRadioButton = findViewById(R.id.nonNegativeRadioButton);
        RadioButton measurmentRadioButton = findViewById(R.id.measurmentRadioButton);
        SwitchMaterial LocationToggle = findViewById(R.id.ExperimentLocationToggleSwitch);
        SwitchMaterial PublicPrivateToggle = findViewById(R.id.ExperimentLocationPublicPrivateToggleSwitch);
        AppCompatImageButton back_btn = findViewById(R.id.newExperimentLocationOnBackButtonView);

        String expName = name.getText().toString();
        // TODO: grab the input in the text fields and add to the firebase
        // TODO: also need to pass the user id to this class



    }

//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.new_experiment_location_on, container, false);
//        NumberPicker maxTrials = view.findViewById(R.id.maxTrialsNumberPicker);
//        NumberPicker minTrials = view.findViewById(R.id.minTrialsNumberPicker);
//        TextInputEditText name = view.findViewById(R.id.newExperimentLocationOnExperimentNameEditText);
//        TextInputEditText description = view.findViewById(R.id.newExperimentLocationOnExperimentDescriptionEditText);
//        RadioButton binomialRadioButton = view.findViewById(R.id.binomialRadioButton);
//        RadioButton countRadioButton = view.findViewById(R.id.countRadioButton);
//        RadioButton nonNegativeRadioButton = view.findViewById(R.id.nonNegativeRadioButton);
//        RadioButton measurmentRadioButton = view.findViewById(R.id.measurmentRadioButton);
//        SwitchMaterial LocationToggle = view.findViewById(R.id.ExperimentLocationToggleSwitch);
//        SwitchMaterial PublicPrivateToggle = view.findViewById(R.id.ExperimentLocationPublicPrivateToggleSwitch);
//        AppCompatImageButton back_btn = view.findViewById(R.id.newExperimentLocationOnBackButtonView);
//
//        return view;
//    }
}
