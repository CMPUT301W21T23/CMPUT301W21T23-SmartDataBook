
package com.example.cmput301w21t23_smartdatabook.comments;

import com.example.cmput301w21t23_smartdatabook.user.User;

import java.io.Serializable;
import java.util.ArrayList;

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

    //Contains a list of comment objects that are replying to the 
    //parent comment object.
    ArrayList<Comment> replyList;

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

    public String getUserUniqueID() {
        return userUniqueID;
    }

    public void setUserUniqueID(String userUniqueID) {
        this.userUniqueID = userUniqueID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter that returns the String text attribute of the Comment class
     *
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * Setter for the text attribute of the Comment class
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Getter for the commentID attribute of the Comment class
     *
     * @return int of commentID
     */
    public String getCommentID() {
        return commentID;
    }

    /**
     * Setter for commentID attribute of the Comment class
     *
     * @param commentID
     */
    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    /**
     * Returns ArrayList of Comment type which is a collection of Comments
     *
     * @return replyList, list of replies
     */
    public ArrayList<Comment> getReplyList() {
        return replyList;
    }

    /**
     * Setter for the ReplyList for each comment
     *
     * @param replyList
     */
    public void setReplyList(ArrayList<Comment> replyList) {
        this.replyList = replyList;
    }

//    /**
//     * getter for the question
//     * @return question, the question itself
//     */
//    public Comment getQuestion(){
//        return question;
//    }

//    /**
//     * setter for question
//     * if set the question only if the reply list is not null
//     * @param replyList
//     */
//     public void setQuestion(ArrayList <Comment> replyList){
//        // only set the question if the list is not empty
//        if (!replyList.isEmpty()){
//            this.question=replyList.get(0);
//        }


    }



