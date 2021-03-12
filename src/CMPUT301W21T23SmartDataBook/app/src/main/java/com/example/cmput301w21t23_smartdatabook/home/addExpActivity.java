package com.example.cmput301w21t23_smartdatabook.home;

import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

public class addExpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.new_experiment_location_on);

        View view = R.layout.new_experiment_location_on;

        NumberPicker maxTrials = R.findViewById(maxTrialsNumberPicker);
        NumberPicker minTrials = R.findViewById(minTrialsNumberPicker);
        TextInputEditText name = R.findViewById(newExperimentLocationOnExperimentNameEditText);
        TextInputEditText description = R.findViewById(newExperimentLocationOnExperimentDescriptionEditText);
        RadioButton binomialRadioButton = R.findViewById(binomialRadioButton);
        RadioButton countRadioButton = R.findViewById(countRadioButton);
        RadioButton nonNegativeRadioButton = R.findViewById(nonNegativeRadioButton);
        RadioButton measurmentRadioButton = R.findViewById(measurmentRadioButton);
        SwitchMaterial LocationToggle = R.findViewById(ExperimentLocationToggleSwitch);
        SwitchMaterial PublicPrivateToggle = R.findViewById(ExperimentLocationPublicPrivateToggleSwitch);
        


    }
}
