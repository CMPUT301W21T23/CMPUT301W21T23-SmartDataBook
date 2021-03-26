package com.example.cmput301w21t23_smartdatabook;

import com.example.cmput301w21t23_smartdatabook.trials.Trial;

/**
 * Class: Measurement Trial
 * This class consists the measurement trial, it mainly uses the attributes: input 
 * It also has getter and setters method for input, in case the experiment's owner need to ignore results
 * @author Alex Mak 
 * @see Trial , Experiment
 */

public class MeasurementTrial extends Trial{
    public MeasurementTrial(String trialValue, String expType) {
        super(trialValue, expType);
    }

    /**
     * Public constructor for the MeasurementTrial class
     * @param count
     * @param numPass
     * @param numFail
     * @param input
     */
//    public MeasurementTrial(int count, int numPass, int numFail, float input) {
//        super(count, numPass, numFail, input);
//    }

    // setter and getter for input (in case the experiment's owner want to ignore some experiment

    /**
     * Setter for the input attribute of the MeasurementTrial class
     * @param input
     */
    public void setInput(float input){super.input=input;}

    /**
     * Getter for the input attribute of the MeasurementTrial class
     * @return float
     */
    public float getInput(){return super.input;}
}
