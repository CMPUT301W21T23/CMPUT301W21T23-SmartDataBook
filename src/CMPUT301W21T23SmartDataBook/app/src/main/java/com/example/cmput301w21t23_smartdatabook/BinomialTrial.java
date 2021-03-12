package com.example.cmput301w21t23_smartdatabook;

/**
 * Class: Binomial Trial
 * This class consists the binomial trial, it mainly uses the attributes: numPass, numFail
 * It has methods to increment and decrement numPass and numFail
 * It also has getter and setters for numPass and numFail, in case the experiment's owner need to ignore results
 * It also has methods to calculate statisitics explicitly for binomial trials such as total number of pass and fails, and the percentage of pass
 * @author Alex Mak 
 * @see Trial, Experiment
 */

public class BinomialTrial extends Trial{

    public float passPercentage;
    public int numTotal; // total=numPass+numFailure

    public BinomialTrial(int count, int numPass, int numFail, Float input, float passPercentage, int numTotal) {
        super(count, numPass, numFail, input);
        this.passPercentage=passPercentage;

        // just try to calculate the pass percentage and total
    }

    // increment/decrement number of pass
    public void incrementPass (){super.numPass++;}
    public void decrementPass (){super.numPass--;}

    // increment/decrement number of fail
    public void incrementFail (){super.numFail++;}
    public void decrementFail (){super.numFail--;}

    // setters and getters for numpass and numfail for experiment owner to modify it if needed
    public void setNumpass(int numPass){super.numPass=numPass;}
    public int getNumpass(){return super.numPass;}

    public void setNumfail(int numFail){super.numFail=numFail;}
    public int getNumfail(){return super.numFail;}

    // set child attributes: numTotal and passPercentage
    public void setNumTotal(){numTotal=super.numPass+super.numFail;}
    public void setPassPercentage(){passPercentage=((float)super.numPass/(super.numPass+super.numFail))*100;}


}
