
package com.example.cmput301w21t23_smartdatabook;

import java.util.ArrayList;

abstract class User{
    String userName;
    String userContact;
    String userUniqueID;

    ArrayList<Experiment> ownedExperimentList;
    ArrayList<Experiment> favoriteExperimentList;

    public String getUserUniqueID() {
        return userUniqueID;
    }

    public void setUserUniqueID(String userUniqueID) {
        this.userUniqueID = userUniqueID;
    }

    public ArrayList<Experiment> getOwnedExperimentList() {
        return ownedExperimentList;
    }

    public void setOwnedExperimentList(ArrayList<Experiment> ownedExperimentList) {
        this.ownedExperimentList = ownedExperimentList;
    }

    public ArrayList<Experiment> getFavoriteExperimentList() {
        return favoriteExperimentList;
    }

    public void setFavoriteExperimentList(ArrayList<Experiment> favoriteExperimentList) {
        this.favoriteExperimentList = favoriteExperimentList;
    }

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


    /**
     * Creates a new comment object given the text string, userUniqeID string, 
     * and commentID int
     * @return gives a new comment object. 
     */
//    public Comment createComment(String text, String userUniqueID, int commentID){
//       return new Comment(text, userUniqueID, commentID);
//
//    }
}

