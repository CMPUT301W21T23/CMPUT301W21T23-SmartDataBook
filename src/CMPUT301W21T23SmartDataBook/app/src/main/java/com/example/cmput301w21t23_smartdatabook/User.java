package com.example.cmput301w21t23_smartdatabook;

abstract class User{
    String userName;
    String userContact;
    String uniqueID;

    ArrayList<Experiment> ownedExperimentList;
    ArrayList<Experiment> followedExperimentList;

    public String getUserName () {
        return userName;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }

    public String getUserContact () {
        return userContact;
    }

    public void setUserContact (String userContact) {
        this.userContact = userContact;
    }



}