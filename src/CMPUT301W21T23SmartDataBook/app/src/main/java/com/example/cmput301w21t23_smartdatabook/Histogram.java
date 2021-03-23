package com.example.cmput301w21t23_smartdatabook;


/**
 * class: histogram
 * This class consists the histogram
 * @see Experiment
 * @author Alex Mak
 */
public class Histogram extends Experiment{
    // extends from experiment because experiment has all the trials, subject to change in future if we make a new class to handle such issue

    /**
     * Histogram's public constructor
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
    public Histogram(String expName, String ownerUserID, String trialType, String description, boolean regionOn, int minTrials, int maxTrials, boolean isPublic, String date, String expID, boolean isEnd) {
        super(expName, ownerUserID, trialType, description, regionOn, minTrials, maxTrials, isPublic, date, expID, isEnd);
    }
    // will need a xml file for histogram in future
}
