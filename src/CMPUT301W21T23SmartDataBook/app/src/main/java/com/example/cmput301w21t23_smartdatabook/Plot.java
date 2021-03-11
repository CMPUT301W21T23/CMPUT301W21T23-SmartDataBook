package com.example.cmput301w21t23_smartdatabook;

public class Plot extends Experiment{
    // extends from experiment because experiment has all the trials, subject to change in future if we make a new class to handle such issue
    public Plot(String expName, String ownerUserID, String trialType, String description, boolean regionOn, int minTrials, int maxTrials, boolean isPublic, String date) {
        super(expName, ownerUserID, trialType, description, regionOn, minTrials, maxTrials, isPublic, date);
    }

    // will need a xml file for plot in future
}
