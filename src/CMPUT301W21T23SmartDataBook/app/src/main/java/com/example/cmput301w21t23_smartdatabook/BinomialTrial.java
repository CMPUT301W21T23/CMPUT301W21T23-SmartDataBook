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

    /**
     * Public Constructor for BinomialTrial Class
     * @param count
     * @param numPass
     * @param numFail
     * @param input
     * @param passPercentage
     * @param numTotal
     */
    public BinomialTrial(int count, int numPass, int numFail, Float input, float passPercentage, int numTotal) {
        super(count, numPass, numFail, input);
        this.passPercentage=passPercentage;

        // just try to calculate the pass percentage and total
    }


    /**
     * Increases the numPass attribute by 1
     */
    public void incrementPass (){super.numPass++;}

    /**
     * Decreases the numPass attribute by 1
     */
    public void decrementPass (){super.numPass--;}

    /**
     * Increases the number of fails by 1
     */
    public void incrementFail (){super.numFail++;}

    /**
     * Decreases teh number of fails by 1
     */
    public void decrementFail (){super.numFail--;}

    /**
     * Setter for numPass for experiment
     * @param numPass
     */
    public void setNumPass(int numPass){super.numPass=numPass;}

    /**
     * Getter for numPass for experiment
     * @return int for the number of passes
     */
    public int getNumPass(){return super.numPass;}

    /**
     * Setter for numFail for the experiment
     * @param numFail
     */
    public void setNumFail(int numFail){super.numFail=numFail;}

    /**
     * Getter for numFail that returns int of number of fails
     * @return int which is the number of fails
     */
    public int getNumFail(){return super.numFail;}

    /**
     * Set child attributes numTotal to numPass + numFail
     */
    public void setNumTotal(){numTotal=super.numPass+super.numFail;}

    /**
     * Set child attributes numPercentage to (numPass / (numPass + numFail)) * 100
     */
    public void setPassPercentage(){passPercentage=((float)super.numPass/(super.numPass+super.numFail))*100;}
}
