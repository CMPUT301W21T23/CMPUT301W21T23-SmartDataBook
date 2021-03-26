package com.example.cmput301w21t23_smartdatabook.trials;

import com.example.cmput301w21t23_smartdatabook.trials.Trial;

/**
 * Class: Count Trial
 * This class consists the count trial, it mainly uses the attributes: count, 
 * It has methods to increment and decrement count
 * It also has getter and setters for numPass and numFail, in case the experiment's owner need to ignore results
 * It also has a method to check whether the count is non-negative or not
 * @author Alex Mak, Krutik Soni
 * @see Trial , Experiment
 * @version 1
 */

public class CountTrial extends Trial{
    private String num;

    public CountTrial(boolean geoLocationSettingOn, String expType, String num) {
        super(geoLocationSettingOn, expType);
        this.num = num;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
