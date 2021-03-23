package com.example.cmput301w21t23_smartdatabook;

/**
 * Class: Plot:
 * This class consists of the plot, that extends from the experiment
 * @see Experiment
 * @author Alex Mak
 */
public class Plot extends Experiment{
    // extends from experiment because experiment has all the trials, subject to change in future if we make a new class to handle such issue

    /**
     * Plot's public constructor
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
    public Plot(String expName, String ownerUserID, boolean isEnd, String trialType, String description, boolean regionOn, int minTrials, int maxTrials, boolean isPublic, String date, String expID) {
        super(expName, ownerUserID, trialType, description, regionOn, minTrials, maxTrials, isPublic, date, expID,isEnd);
    }

    // will need a xml file for plot in future
}
