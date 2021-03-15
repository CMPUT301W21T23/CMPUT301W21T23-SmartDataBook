
package com.example.cmput301w21t23_smartdatabook;

import java.util.ArrayList;

/**
 * Abstract class user
 * Constrcuts the user
 * @see Experiment
 * @author Bosco Chan
 */
abstract class User{
    String userName;
    String userContact;
    String userUniqueID;

    ArrayList<Experiment> ownedExperimentList;
    ArrayList<Experiment> favoriteExperimentList;

    /**
     * method that obtains the user's unique ID
     * @return userUniqueID
     */
    public String getUserUniqueID() {
        return userUniqueID;
    }

    /**
     * method that sets the user's unique ID
     * @param userUniqueID
     */
    public void setUserUniqueID(String userUniqueID) {
        this.userUniqueID = userUniqueID;
    }

    /**
     * Getter method for ownedExperimentList: obtains the owned experiment list
     * @return ownedExperimentList, the arrayList that consists of the user's owned experiment
     */
    public ArrayList<Experiment> getOwnedExperimentList() {
        return ownedExperimentList;
    }

    /**
     * Setter method for ownedExperimentList: sets the owned experiment list
     * @param ownedExperimentList
     */
    public void setOwnedExperimentList(ArrayList<Experiment> ownedExperimentList) {
        this.ownedExperimentList = ownedExperimentList;
    }

    /**
     * Getter Method for getFavoriteExperimentList: obtains the user's favorite experiment list
     * @return favouriteExperimentList, the arrayList that has the user's favourite experiment list
     */
    public ArrayList<Experiment> getFavoriteExperimentList() {
        return favoriteExperimentList;
    }

    /**
     * Setter Method for getFavoriteExperimentList: obtains the user's favorite experiment list
     * @param favoriteExperimentList
     */
    public void setFavoriteExperimentList(ArrayList<Experiment> favoriteExperimentList) {
        this.favoriteExperimentList = favoriteExperimentList;
    }

    /**
     * Getter method for userName: obtains the username
     * @return userName, a string that has user's username
     */
    public String getUserName () {
        return userName;
    }

    /**
     * Setter method for userName: sets the username
     * @param userName
     */
    public void setUserName (String userName) {
        this.userName = userName;
    }

    /**
     * Getter method for userContact: obtains the user's contact
     * @return userContact, the string that has the user's contact
     */
    public String getUserContact () {
        return userContact;
    }

    /**
     * Setter method for userContact: set the user's contact
     * @param userContact
     */
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

