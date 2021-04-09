package com.example.cmput301w21t23_smartdatabook.experiment;

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
//        this.ownerUserName = ownerUserName;
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

    // for parcellable
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

    /**
     * Setters for isEnd
     * @param end
     */

    public void setEnd(boolean end) {
        isEnd = end;
    }

    /**
     * Getters for isEnd
     * @return isEnd: a boolean variable checking whether an experiment has been ended or not
     */
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
     * Getter for the owners user ID which is a hashed code assigned by the app
     * @return
     */
    public String getOwnerUserID() {
        return ownerUserID;
    }

    /**
     * Getter for the type of the trial (Count, Binomial...)
     * @return String for the type the trial is
     */
    public String getTrialType() {
        return trialType;
    }

    /**
     * Getter for the description of the experiment
     * @return String of description of the experiment
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns if the regionOn is True/False for the expeiment
     * @return Boolean to represent True/False for regionOn attrubute
     */
    public boolean getRequireLocation() {
        return requireLocation;
    }

    /**
     * Getter for the min number of trials for the experiment
     * @return Int that is the minimun number of trials for the experiment
     */
    public int getMinTrials() {
        return minTrials;
    }

    /**
     * Getter for the max number of trials for the experiment
     * @return Int representing max number fo the trials for the experiment
     */
    public int getMaxTrials() {
        return maxTrials;
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
     * Getter for the location of the experiment
     * @return Location object
     */
    public LatLng getLatLng() {
        return this.latlng;
    }

    /**
     * Getters for expID
     * @return expID: a string object consists the experiment ID
     */
    public String getExpID() {
        return expID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * This functionm writes the experiment's content to the parcel
     * @param dest
     * @param flags
     */
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



