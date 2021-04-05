package com.example.cmput301w21t23_smartdatabook;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;


/**
 * Class: Experiment
 * class that constructs the experiment
 * @author Afaq Nabi, Bosco Chan
 */
public class Experiment implements Serializable, Parcelable {

    private String expID;
    private String expName;
    private String ownerUserID;

    private String ownerUserName;
    private String trialType;
    private String description;

    // private Map region;
    private int minTrials;
    private int maxTrials;
    private boolean isPublic;

    // private Trial Array<Trial>;
    private boolean requireLocation;
    private String date;
    private LatLng latlng;

    // end experiment
    private boolean isEnd;

    /**
     * Public constructor for Experiment without location settings
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
     * @param isEnd
     */
    public Experiment(String expName, String ownerUserID, String ownerUserName,
                      String trialType, String description, boolean regionOn,
                      int minTrials, int maxTrials, boolean isPublic, String date,
                      String expID, boolean isEnd) {

        this.expName = expName;
        this.ownerUserID = ownerUserID;
        this.ownerUserName = ownerUserName;
        this.trialType = trialType;
        this.description = description;
        this.requireLocation = regionOn;
        this.minTrials = minTrials;
        this.maxTrials = maxTrials;
        this.isPublic = isPublic;
        this.date = date;
        this.expID = expID;
        this.isEnd = isEnd;
        this.latlng = null;
    }

    protected Experiment(Parcel in) {
        expID = in.readString();
        expName = in.readString();
        ownerUserID = in.readString();
        ownerUserName = in.readString();
        trialType = in.readString();
        description = in.readString();
        minTrials = in.readInt();
        maxTrials = in.readInt();
        isPublic = in.readByte() != 0;
        requireLocation = in.readByte() != 0;
        date = in.readString();
        latlng = in.readParcelable(LatLng.class.getClassLoader());
        isEnd = in.readByte() != 0;
    }

    public static final Creator<Experiment> CREATOR = new Creator<Experiment>() {
        @Override
        public Experiment createFromParcel(Parcel in) {
            return new Experiment(in);
        }

        @Override
        public Experiment[] newArray(int size) {
            return new Experiment[size];
        }
    };

    public String getOwnerUserName() {
        return ownerUserName;
    }

    // TODO: update following method description
    /**
     * Public constructor for the Experiment with location settings
     * @param expName
     * @param ownerUserID
     * @param trialType
     * @param description
     * @param requireLocation
     * @param minTrials
     * @param maxTrials
     * @param isPublic
     * @param date
     */
    public Experiment(String expName, String ownerUserID, String ownerUserName,
                      String trialType, String description, boolean requireLocation,
                      int minTrials, int maxTrials, boolean isPublic, String date,
                      String expID, boolean isEnd, LatLng latlng) {

        this.expName = expName;
        this.ownerUserID = ownerUserID;
        this.ownerUserName = ownerUserName;
        this.trialType = trialType;
        this.description = description;
        this.requireLocation = requireLocation;
        this.minTrials = minTrials;
        this.maxTrials = maxTrials;
        this.isPublic = isPublic;
        this.date = date;
        this.expID = expID;
        this.isEnd = isEnd;
        this.latlng = latlng;
    }


    public void setEnd(boolean end) {
        isEnd = end;
    }

    public boolean getIsEnd() {
        return isEnd;
    }

    /**
     * getter for the date of the experiment
     * @return String of date
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter for the date attribute of the class
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter for the experiment name of the experiment
     * @return String of experiments name
     */
    public String getExpName() {
        return expName;
    }

    /**
     * Setter for the experiments name
     * @param expName
     */
    public void setExpName(String expName) {
        this.expName = expName;
    }

    /**
     * Getter for the owners user ID which is a hashed code assigned by the app
     * @return
     */
    public String getOwnerUserID() {
        return ownerUserID;
    }

    /**
     * Setter for the owners users ID
     * @param ownerUserID
     */
    public void setOwnerUserID(String ownerUserID) {
        this.ownerUserID = ownerUserID;
    }

    /**
     * Getter for the type of the trial (Count, Binomial...)
     * @return String for the type the trial is
     */
    public String getTrialType() {
        return trialType;
    }

    /**
     * Setter for the type of trial
     * @param trialType
     */
    public void setTrialType(String trialType) {
        this.trialType = trialType;
    }

    /**
     * Getter for the description of the experiment
     * @return String of description of the experiment
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description of the experiment
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns if the regionOn is True/False for the expeiment
     * @return Boolean to represent True/False for regionOn attrubute
     */
    public boolean getRequireLocation() {
        return requireLocation;
    }


    /**
     * Set the regionOn attribute of experiment to True/False
     * @param requireLocation
     */
    public void setRequireLocation(boolean requireLocation) {
        this.requireLocation = requireLocation;
    }

    /**
     * Getter for the min number of trials for the experiment
     * @return Int that is the minimun number of trials for the experiment
     */
    public int getMinTrials() {
        return minTrials;
    }

    /**
     * Setter for the minimun number of trials the experiment can have
     * @param minTrials
     */
    public void setMinTrials(int minTrials) {
        this.minTrials = minTrials;
    }

    /**
     * Getter for the max number of trials for the experiment
     * @return Int representing max number fo the trials for the experiment
     */
    public int getMaxTrials() {
        return maxTrials;
    }

    /**
     * Setter for the max trials the experiment can have
     * @param maxTrials
     */
    public void setMaxTrials(int maxTrials) {
        this.maxTrials = maxTrials;
    }

    /**
     * Gets Boolean to represent whether the experiment is public or private
     * @return Boolean True/False representing private/public state of the experiment
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Sets the state of the experiment to be public or private
     * @param aPublic which is either True/False
     */
    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    /**
     * Boolean to represent whether the experiment the location setting to be on or off
     * @return Boolean that is True/False to represent on/off requireLocation setting
     */
    public boolean requiresLocation() {
        return requireLocation;
    }

    /**
     * Flips the current setting for requireLocation from
     * on -> off
     * or off -> on
     */
    public void toggleRequireLocation() {
        this.requireLocation = !this.requireLocation;
    }

    /**
     * Getter for the location of the experiment
     * @return Location object
     */
    public LatLng getLatLng() {
        return this.latlng;
    }

    /**
     * Setter for the location of the experiment using a Location object
     * @param latlng
     */
    public void setLatLng(LatLng latlng) {
        this.latlng = latlng;
    }

    public String getExpID() {
        return expID;
    }

    public void setExpID(String expID) {
        this.expID = expID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(expID);
        dest.writeString(expName);
        dest.writeString(ownerUserID);
        dest.writeString(ownerUserName);
        dest.writeString(trialType);
        dest.writeString(description);
        dest.writeInt(minTrials);
        dest.writeInt(maxTrials);
        dest.writeByte((byte) (isPublic ? 1 : 0));
        dest.writeByte((byte) (requireLocation ? 1 : 0));
        dest.writeString(date);
        dest.writeParcelable(latlng, flags);
        dest.writeByte((byte) (isEnd ? 1 : 0));
    }
}//Experiment



