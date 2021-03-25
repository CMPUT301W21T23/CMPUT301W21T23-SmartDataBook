
package com.example.cmput301w21t23_smartdatabook.user;

import com.example.cmput301w21t23_smartdatabook.Experiment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Singleton User class
 * this class can be accessed anywhere in the code
 * only need to set the user once per run
 * @see Experiment
 * @author Bosco Chan, Afaq Nabi, Alex Mak, Jaydem Cho, Krutik Soni, Natnail Ghebresilasie
 */
public class User implements Serializable {
    private static User user;
    public String userName;
    public String userContact;
    public String userUniqueID;

    private User(){}

    public static User getUser(){
        if (user == null){
            user = new User();
        }
        return user;
    }

    ArrayList<Experiment> ownedExperimentList;
    ArrayList<Experiment> favoriteExperimentList;

    public User(String userName, String userContact, String userUniqueID) {
        this.userName = userName;
        this.userContact = userContact;
        this.userUniqueID = userUniqueID;
    }

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

