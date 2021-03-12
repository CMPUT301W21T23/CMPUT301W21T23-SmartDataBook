package com.example.cmput301w21t23_smartdatabook;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Class:experimentDetails
 * This class shows the details of the experiment,it displays:
 * An experiment's minimum and maximum number of trials
 * An experiment's name and description
 * Radio button to choose which type of the trial the experiment has (binomial/ measurement/count/ non-negtaive count trials)
 * switch button that turns on/ off an experiment's trial location.
 * @author Afaq Nabi, Bosco Chan
 * @version 1
 * @see Experiment, 
 */
public class experimentDetails extends AppCompatActivity {
    public int position;

    public experimentDetails(int pos) {
        this.position = pos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experiment_details);

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
