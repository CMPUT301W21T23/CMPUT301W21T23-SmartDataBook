package com.example.cmput301w21t23_smartdatabook.trials;

import android.location.Location;

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

    public Trial(boolean geoLocationSettingOn, String expType) {
        this.geoLocationSettingOn = geoLocationSettingOn;
        this.expType = expType;
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

}
