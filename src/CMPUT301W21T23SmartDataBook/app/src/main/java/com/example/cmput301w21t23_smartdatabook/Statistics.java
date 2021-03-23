package com.example.cmput301w21t23_smartdatabook;

/**
 * Class Statistics
 * This class consists of an experiment's statistics
 * @see Experiment
 * @author Alex Mak
 */
public class Statistics extends Experiment{
    // extends from experiment because experiment has all the trials, subject to change in future if we make a new class to handle such issue

    /**
     * Statistics class' public constructor
     * @param expName
     * @param ownerUserID
     * @param trialType
     * @param description
     * @param regionOn
     * @param minTrials
     * @param maxTrials
     * @param isPublic
     * @param date
     * @param expID
     */
    public Statistics(String expName, String ownerUserID, String trialType, String description, boolean isEnd, boolean regionOn, int minTrials, int maxTrials, boolean isPublic, String date, String expID) {
        super(expName, ownerUserID, trialType, description, regionOn, minTrials, maxTrials, isPublic, date, expID, isEnd);
    }

    // this file will not be needed if we are expressing our statistics through histogram/plots

    /**
     * This function displays the statistics for binomial trials
     *  If the trial is a binomial trial, then display:
     *  the pass percentage
     *  total number of inputs
     *  number of passes
     *  number of failures
     *  (optional: mean or median if you really need them)
     */

    public void binomialTrialStat(){

    }

    /**
     * This function displays the statistics for count trials
     * If the trial is a count trial, then display:
     * mean
     * median
     * mode?
     * standard deviation
     * quartiles
     * not sure about std dev, and quartiles since they are more appropriate to be shown in the histogram/plots
     */
    public void countTrialStat(){

    }

    /**
     * This function displays the statistics for measurement trials
     * If the trial is a measurement trial, then display:
     * mean
     * median
     * mode?
     * standard deviation
     * quartiles
     * not sure about quartiles since they are more appropriate to be shown in the histogram/plots
     */

    public void measurementTrialStat(){

    }







}
