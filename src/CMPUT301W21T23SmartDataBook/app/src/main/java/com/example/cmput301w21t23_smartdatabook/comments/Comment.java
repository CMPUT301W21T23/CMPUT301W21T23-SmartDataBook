
package com.example.cmput301w21t23_smartdatabook.comments;

import com.example.cmput301w21t23_smartdatabook.user.User;
import java.lang.String;

import java.io.Serializable;
import java.lang.String;
import java.util.ArrayList;
import java.util.Date;

/**
 * class: Comment
 * This class consists the comments made by users
 * It consists the attributes of the comment text, comment's ID, and question which is a type of a question
 * @see User
 * @author Krutik Soni, Natnail Ghebresilasie
 */
public class Comment implements Serializable {

    private String userUniqueID;
    private String text;
    private String commentID;
    private String date;

    /**
     * Public constructor for the Comment class
     *
     * @param text
     * @param userUniqueID
     * @param commentID
     */
    public Comment(String text, String userUniqueID, String commentID, String date) {
//        super(null, null, userUniqueID);
        this.text = text;
        this.userUniqueID = userUniqueID;
        this.commentID = commentID;
        this.date = date;

    }

    /**
     * Getter method that returns the string that contains the user's unique ID
     * @return userUniqueID
     */
    public String getUserUniqueID() {
        return userUniqueID;
    }

    /**
     * Getter method that returns the comment's date in string form
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * Setetr method that sets the Comment's date
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter that returns the String text attribute of the Comment class
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * Setter for the text attribute of the Comment class
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Getter for the commentID attribute of the Comment class
     * @return int of commentID
     */
    public String getCommentID() {
        return commentID;
    }

    }



