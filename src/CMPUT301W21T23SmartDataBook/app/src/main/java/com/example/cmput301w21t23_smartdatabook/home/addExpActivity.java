package com.example.cmput301w21t23_smartdatabook.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

public class addExpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.new_experiment_location_on);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_experiment_location_on, container, false);
        NumberPicker maxTrials = view.findViewById(R.id.maxTrialsNumberPicker);
        NumberPicker minTrials = view.findViewById(R.id.minTrialsNumberPicker);
        TextInputEditText name = view.findViewById(R.id.newExperimentLocationOnExperimentNameEditText);
        TextInputEditText description = view.findViewById(R.id.newExperimentLocationOnExperimentDescriptionEditText);
        RadioButton binomialRadioButton = view.findViewById(R.id.binomialRadioButton);
        RadioButton countRadioButton = view.findViewById(R.id.countRadioButton);
        RadioButton nonNegativeRadioButton = view.findViewById(R.id.nonNegativeRadioButton);
        RadioButton measurmentRadioButton = view.findViewById(R.id.measurmentRadioButton);
        SwitchMaterial LocationToggle = view.findViewById(R.id.ExperimentLocationToggleSwitch);
        SwitchMaterial PublicPrivateToggle = view.findViewById(R.id.ExperimentLocationPublicPrivateToggleSwitch);
        
        return view;
    }
}
