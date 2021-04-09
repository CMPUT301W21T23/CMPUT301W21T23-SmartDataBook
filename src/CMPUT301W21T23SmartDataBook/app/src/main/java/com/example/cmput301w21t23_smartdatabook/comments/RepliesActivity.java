package com.example.cmput301w21t23_smartdatabook.comments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.stats.StringDate;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Class: Replies Activity
 * shows the activity of the comments
 * @author Bosco Chan, Afaq Nabi
 * @see Comment, CommentList
 */

public class RepliesActivity extends AppCompatActivity {
    // initialize variables
    ArrayAdapter<Comment> repliesAdapter;
    ArrayList<Comment> repliesDataList;
    ListView repliesList;

    private Comment parentComment;

    FirebaseFirestore db;
    Database database = new Database();

    TextView owner;
    TextView dateView;
    TextView commentID;
    TextView commentText;

    private User user = User.getUser();

    FloatingActionButton addReply;
    StringDate curStringDate = new StringDate();

    /**
     * onCreate method of Replies Activity, handles most of Replies activity's functions
     * This function sets up the view and visual representation of the screen
     * This function allows users to reply comment based on other's comments, by using a floating button
     * Then, an alert dialog will appear to verify the user's decisions
     * After the users verify their answers and reply an comment, the associated data is passed through databse
     * when there are new replies, the commentList updates
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_replies);

        View addCommentView = LayoutInflater.from(RepliesActivity.this).inflate(R.layout.add_comment, null);

        db = FirebaseFirestore.getInstance();

        // setting up the content of replies activity
        Intent intent = getIntent();
        parentComment = (Comment) intent.getSerializableExtra("Comment");

        owner = findViewById(R.id.comment_owner_username);
        owner.setText(parentComment.getUserUniqueID());

        dateView = findViewById(R.id.commentDate);
        dateView.setText(""+curStringDate.getDate(parentComment.getDate()));

        commentID = findViewById(R.id.commentID);
        commentID.setText(parentComment.getCommentID().substring(0,6));

        commentText = findViewById(R.id.commentText);
        commentText.setText(parentComment.getText());

        // We have used the following website to learn more about how to textview scrollable as there are more text
        // URL:https://stackoverflow.com/questions/19826693/how-can-i-make-a-textview-automatically-scroll-as-i-add-more-lines-of-text
        // From: Darwin Louis
        EditText newComment = addCommentView.findViewById(R.id.newComment);

        addReply = findViewById(R.id.add_replies_button);
        addReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(RepliesActivity.this);
                builder.setView(addCommentView);
                builder.setTitle("New Comment");
                builder.setNegativeButton("Cancel", null)
                        .setPositiveButton("Add Comment", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String commentText = newComment.getText().toString();

                                Comment comment = new Comment(commentText, user.getUserUniqueID(), UUID.randomUUID().toString(), curStringDate.getCurrentDate());

                                database.addCommentToDB(db.collection("Replies")
                                        .document(parentComment.getCommentID())
                                        .collection("Answers")
                                        .document(UUID.randomUUID().toString()), comment);

                                repliesAdapter.notifyDataSetChanged();
                                recreate();
                            }
                        }).create().show();
            }
        });

        repliesList = findViewById(R.id.replies_list);
        repliesDataList = new ArrayList<>();

        repliesAdapter = new CommentList(this, repliesDataList);
        repliesList.setAdapter(repliesAdapter);

        database.fillCommentList(db.collection("Replies")
                .document(parentComment.getCommentID())
                .collection("Answers"), repliesDataList, repliesAdapter);

    }

}
