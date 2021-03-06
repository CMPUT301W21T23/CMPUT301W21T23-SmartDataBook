package com.example.cmput301w21t23_smartdatabook;

import java.util.ArrayList;

class Comment extends User{

    String text;
    int commentID;
    String userUniqueID;

    //Contains a list of comment objects that are replying to the 
    //parent comment object.
    ArrayList<Comment> replyList;

    public Comment(String text, String userUniqueID, int commentID){
        this.text = text;
        this.userUniqueID = userUniqueID;
        this.commentID = commentID;
    }

    /**
     * Gives the text string contained by the comment.
     * @return gives the text string.
     */
    public String getComment () {
        return text;
    }

    /**
     * Gives the comment's unique ID.
     * @return gives the unique ID associated with the comment.
     */
    public int getID () {
        return commentID;
    }



}