package com.example.cmput301w21t23_smartdatabook;

import android.location.Location;

public class Trial {
    private boolean geoLocationSettingOn;
    private String trialValue;
    // TODO to_be_deleted
    // private float longitude;
    // private float latitude;
    private Location location;


    // attributes for count(non-negative count)
    public int count;

    // attributes for binomial trials
    public int numPass;
    public int numFail;

    // attributes measurement trials
    public Float input;

    /**
     * Public constructor for the Trial class
     * @param count
     * @param numPass
     * @param numFail
     * @param input
     */
    public Trial(int count, int numPass, int numFail, Float input) {
        this.count = count;
        this.numPass = numPass;
        this.numFail = numFail;
        this.input = input;
    }

    /**
     * Getter for the location attribute of the trial
     * @return Location object
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Setter for the location attribute of the trial
     * @param location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /*public float getLongitude() {

        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }*/

    /**
     * Getter for the trialValue attribute
     * @return String
     */
    public String getTrialValue() {
        return trialValue;
    }

    /**
     * Setter for the trialValue attribute
     * @param trialValue
     */
    public void setTrialValue(String trialValue) {
        this.trialValue = trialValue;
    }

    /*public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }*/

}
