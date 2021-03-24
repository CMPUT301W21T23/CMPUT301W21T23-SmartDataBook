package com.example.cmput301w21t23_smartdatabook.comments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301w21t23_smartdatabook.Date;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.comments.Comment;

import java.util.ArrayList;

/**
 * Class: commentList
 * @author Bosco Chan
 */
public class CommentList extends ArrayAdapter {

    private final ArrayList<Comment> comments;
    private final Context context;

    Date date;

    /**
     * Public Constructor of the CommentList class
     * @param context
     * @param comments
     */
    public CommentList (Context context, ArrayList<Comment> comments) {
        super(context, 0, comments);
        this.context = context;
        this.comments = comments;
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return view: the view that contains the list of comments and their comment ID
     */
    @NonNull
    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        }

        date = new Date();

        Comment comment = comments.get(position);

        TextView commentText = view.findViewById(R.id.commentText);
        TextView commentID = view.findViewById(R.id.commentID);
        TextView commentDate = view.findViewById(R.id.commentDate);


        commentText.setText( comment.getText() );
        commentID.setText( ""+ comment.getCommentID() );
        commentDate.setText(date.getDate());


        return view;
    }
}
