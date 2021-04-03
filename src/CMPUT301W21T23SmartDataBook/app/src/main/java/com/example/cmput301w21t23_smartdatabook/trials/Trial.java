package com.example.cmput301w21t23_smartdatabook.trials;

import android.location.Location;

import com.google.firebase.Timestamp;

import java.time.LocalDateTime;

/**
 * Class Trial
 * The class that constructs the trials
 * The class has the location, the experiment type,  number of counts, number of passes, number of fails, input
 * @author Jayden, Krutik, Natnail
 */
public class Trial {

    private boolean geoLocationSettingOn;
//    private Location location;
    private String expType;
    private Object value;
    private String uid;
    private String trialID;
    private Timestamp date;

    public Trial(boolean geoLocationSettingOn, String expType, Object value, String uid, String trialID, Timestamp date) {
        this.geoLocationSettingOn = geoLocationSettingOn;
        this.expType = expType;
        this.value = value;
        this.uid = uid;
        this.trialID = trialID;
        this.date = date;

    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public boolean isGeoLocationSettingOn() {
        return geoLocationSettingOn;
    }

    public void setGeoLocationSettingOn(boolean geoLocationSettingOn) {
        this.geoLocationSettingOn = geoLocationSettingOn;
    }

    public String getExpType() {
        return expType;
    }

    public void setExpType(String expType) {
        this.expType = expType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTrialID() {
        return trialID;
    }

    public void setTrialID(String trialID) {
        this.trialID = trialID;
    }
}
