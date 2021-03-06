package com.example.cmput301w21t23_smartdatabook;

import java.util.ArrayList;

abstract class User{
    String userName;
    String userContact;
    String userUniqueID;

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

    public String getUniqueID () {
        return uniqueID;
    }

    public void setUniqueID (String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public Comment creaComment(String text){
        Comment comment = new Comment();

        return comment;
    }

}