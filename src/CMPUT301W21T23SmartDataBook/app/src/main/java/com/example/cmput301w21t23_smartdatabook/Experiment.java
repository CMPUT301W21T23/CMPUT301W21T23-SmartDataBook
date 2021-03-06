package com.example.cmput301w21t23_smartdatabook;

/**
 *
 */
public class Experiment {
    private String expName;
    private String ownerUserID;
    private String trialType;
    private String description;
    private boolean regionOn;
    // private Map region;
    private int minTrials;
    private int maxTrials;
    private boolean isPublic;
    // private Trial Array<Trial>;

    public Experiment(String expName, String ownerUserID, String trialType, String description, int minTrials, int maxTrials, boolean isPublic) {
        this.expName = expName;
        this.ownerUserID = ownerUserID;
        this.trialType = trialType;
        this.description = description;
        this.minTrials = minTrials;
        this.maxTrials = maxTrials;
        this.isPublic = isPublic;
    }

}
