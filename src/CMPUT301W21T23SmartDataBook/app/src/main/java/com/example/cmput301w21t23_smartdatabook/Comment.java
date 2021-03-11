
package com.example.cmput301w21t23_smartdatabook;

import java.util.ArrayList;

class Comment extends User{

    String text;
    int commentID;

    //Contains a list of comment objects that are replying to the 
    //parent comment object.
    ArrayList<Comment> replyList;

    public Comment(String text, String userUniqueID, int commentID){
        this.text = text;
        this.userUniqueID = userUniqueID;
        this.commentID = commentID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCommentID() { return commentID; }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public ArrayList<Comment> getReplyList() {
        return replyList;
    }

    public void setReplyList(ArrayList<Comment> replyList) {
        this.replyList = replyList;
    }

}

