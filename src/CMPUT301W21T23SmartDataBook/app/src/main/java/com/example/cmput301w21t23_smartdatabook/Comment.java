
package com.example.cmput301w21t23_smartdatabook;

import java.util.ArrayList;

class Comment extends User{

    String text;
    int commentID;
    Comment question;

    //Contains a list of comment objects that are replying to the 
    //parent comment object.
    ArrayList<Comment> replyList;

    public Comment(String text, String userUniqueID, int commentID){
        super(null, null, userUniqueID);
        this.text = text;
        this.userUniqueID = userUniqueID;
        this.commentID = commentID;
        this.question=question;
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
    
    public Comment getQuestion(){
        return question;
    }

    public void setQuestion(ArrayList <Comment> replyList){
        this.question=replyList.get(0);
    }

}

