package com.example.cmput301w21t23_smartdatabook;

/**
 * Class: Measurement Trial
 * This class consists the measurement trial, it mainly uses the attributes: input 
 * It also has getter and setters method for input, in case the experiment's owner need to ignore results
 * @author Alex Mak 
 * @see Trial, Experiment
 */

public class MeasurementTrial extends Trial{

    public MeasurementTrial(int count, int numPass, int numFail, float input) {
        super(count, numPass, numFail, input);
    }

    // setter and getter for input (incase the experiment's owner want to ignore some experiment
    public void setInput(float input){super.input=input;}
    public float getInput(){return super.input;}
}
