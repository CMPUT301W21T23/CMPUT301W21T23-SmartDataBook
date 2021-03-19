package com.example.cmput301w21t23_smartdatabook;

 /**
 * Class: Count Trial
 * This class consists the count trial, it mainly uses the attributes: count, 
 * It has methods to increment and decrement count
 * It also has getter and setters for numPass and numFail, in case the experiment's owner need to ignore results
 * It also has a method to check whether the count is non-negative or not
 * @author Alex Mak, Krutik Soni
 * @see Trial, Experiment
 * @version 1
 */

public class CountTrial extends Trial{
     public CountTrial(String trialValue, String expType) {
         super(trialValue, expType);
     }

     // might need to be fixed in future, change to local attributes if necessary

     /**
      * Public constructor for the CountTrial class
      * @param count
      * @param numPass
      * @param numFail
      * @param input
      */
//    public CountTrial(int count, int numPass, int numFail, Float input) {
//        super(count, numPass, numFail, input);
//    }


     /**
      * Increases count by 1
      */
    public void addCount(){super.count++;
    }

     /**
      * Decreases count by 1
      */
     public void minusCount(){
        super.count--;
    }

    // setters and getters:
    // for experimenter's owner to ignore certain experiment results

     /**
      * Setter for Count attribute of CountTrial Class
      * @param count
      */
    public void setCount(int count){
        super.count=count;
    }

     /**
      * Getter for the count attribute of the CountTrial Class
      * @return int representing count
      */
    public int getCount(){return super.count;}


    // (optional: not sure do we need that) adding new trials for adding new data, delete Trial
//    public Trial addNewTrial(){...}
//    public Trial deleteTrial(){...}

//     /**
//     * This method serves to check whether the type of trial is non-negative count, or just regular count
//     * @return boolean value whether the trial has the type of non-negative count or not
//     * @author Alex Mak
//     * @see Experiment
//     * @version 1
//     */
 
    // determine whether count type is regular, or non-negative: unfinished method since not sure how to interact with other classes
//    public boolean isNonNegative(){}

}
