package com.example.cmput301w21t23_smartdatabook.trials;

import com.google.android.gms.maps.model.LatLng;

/**
 * Class Trial
 * The class that constructs the trials
 * The class has the location, the experiment type,  number of counts, number of passes, number of fails, input
 *
 * @author Jayden, Krutik, Natnail
 */
public class Trial {

	private boolean geoLocationSettingOn;
	private String expType;
	private Object value;
	private String uid;
	private String trialID;
	private String date;
	private LatLng location;

	/**
	 * Constructor for trial when the trials need location (location enabled)
	 *
	 * @param geoLocationSettingOn
	 * @param expType
	 * @param value
	 * @param uid
	 * @param trialID
	 * @param date
	 * @param location
	 */
	public Trial(boolean geoLocationSettingOn, String expType, Object value, String uid, String trialID, String date, LatLng location) {
		this.geoLocationSettingOn = geoLocationSettingOn;
		this.expType = expType;
		this.value = value;
		this.uid = uid;
		this.trialID = trialID;
		this.date = date;
		this.location = location;
	}

	/**
	 * Constructor for trial when the trials don't need location (location disabled)
	 *
	 * @param geoLocationSettingOn
	 * @param expType
	 * @param value
	 * @param uid
	 * @param trialID
	 * @param date
	 */
	public Trial(boolean geoLocationSettingOn, String expType, Object value, String uid, String trialID, String date) {
		this.geoLocationSettingOn = geoLocationSettingOn;
		this.expType = expType;
		this.value = value;
		this.uid = uid;
		this.trialID = trialID;
		this.date = date;
	}

	/**
	 * Getters for location
	 *
	 * @return location, a Latlng object showing the trial's location
	 */
	public LatLng getLocation() {
		return location;
	}

	/**
	 * Setters for location
	 *
	 * @param location
	 */
	public void setLocation(LatLng location) {
		this.location = location;
	}

	/**
	 * Getters for date
	 *
	 * @return date, a string showing the trial uploaded date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Setters for date
	 *
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * This method checks the boolean value of geoLocationSettingOn
	 *
	 * @return geoLocationSettingOn: a boolean variable checking whether geolcoation is on or off
	 */
	public boolean isGeoLocationSettingOn() {
		return geoLocationSettingOn;
	}

	/**
	 * Getters for experiment type
	 *
	 * @return expType: a string object showing the experiment's type (binomial, non-negative count, count, measurement)
	 */
	public String getExpType() {
		return expType;
	}

	/**
	 * Getters for value
	 *
	 * @return value: object class that consists of the trial's value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Setters for value
	 *
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Getters for uid
	 *
	 * @return uid: a string object consists of the user's id
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * Getters for trial ID
	 *
	 * @return trialID: a string object consists of the trial's ID
	 */
	public String getTrialID() {
		return trialID;
	}

}
