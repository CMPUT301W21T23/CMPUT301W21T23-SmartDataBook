package com.example.cmput301w21t23_smartdatabook;

public class Trial {
    private boolean geoLocationSettingOn;
    private String trialValue;
    private float longitude;
    private float latitude;

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public String getTrialValue() {
        return trialValue;
    }

    public void setTrialValue(String trialValue) {
        this.trialValue = trialValue;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
