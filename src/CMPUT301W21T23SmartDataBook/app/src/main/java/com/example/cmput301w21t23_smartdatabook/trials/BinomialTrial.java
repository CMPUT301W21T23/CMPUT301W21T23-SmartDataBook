package com.example.cmput301w21t23_smartdatabook.trials;

import com.example.cmput301w21t23_smartdatabook.trials.Trial;

/**
 * Class: Binomial Trial
 * This class consists the binomial trial, it mainly uses the attributes: numPass, numFail
 * It has methods to increment and decrement numPass and numFail
 * It also has getter and setters for numPass and numFail, in case the experiment's owner need to ignore results
 * It also has methods to calculate statisitics explicitly for binomial trials such as total number of pass and fails, and the percentage of pass
 * @author Alex Mak, Krutik Soni, Natnail Ghebresilasie
 * @see Trial , Experiment
 */

public class BinomialTrial extends Trial{

    private boolean trialValue;
    private String num;

    public BinomialTrial(boolean geoLocationSettingOn, String expType, boolean trialValue, String num) {
        super(geoLocationSettingOn, expType);
        this.trialValue = trialValue;
        this.num = num;
    }

    public boolean isTrialValue() {
        return trialValue;
    }

    public void setTrialValue(boolean trialValue) {
        this.trialValue = trialValue;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
