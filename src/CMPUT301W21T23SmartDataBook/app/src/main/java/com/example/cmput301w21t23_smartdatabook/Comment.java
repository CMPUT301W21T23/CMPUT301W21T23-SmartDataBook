
package com.example.cmput301w21t23_smartdatabook;

import java.util.ArrayList;

class Comment extends User{

    String text;
    int commentID;
    Comment question;

    //Contains a list of comment objects that are replying to the 
    //parent comment object.
    ArrayList<Comment> replyList;

    /**
     * Public constructor for the Comment class
     * @param text
     * @param userUniqueID
     * @param commentID
     */
    public Comment(String text, String userUniqueID, int commentID){
        super(null, null, userUniqueID);
        this.text = text;
        this.userUniqueID = userUniqueID;
        this.commentID = commentID;
        this.question=question;
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
    public int getCommentID() { return commentID; }

    /**
     * Setter for commentID attribute of the Comment class
     * @param commentID
     */
    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    /**
     * Returns ArrayList of Comment type which is a collection of Comments
     * @return
     */
    public ArrayList<Comment> getReplyList() {
        return replyList;
    }

    /**
     * Setter for the ReplyList for each comment
     * @param replyList
     */
    public void setReplyList(ArrayList<Comment> replyList) {
        this.replyList = replyList;
    }
    
    public Comment getQuestion(){
        return question;
    }

     public void setQuestion(ArrayList <Comment> replyList){
        // only set the question if the list is not empty
        if (!replyList.isEmpty()){
            this.question=replyList.get(0);
        }

    }

}

