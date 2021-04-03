package com.example.cmput301w21t23_smartdatabook.comments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.Date;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Class: Replies Activity
 * shows the activity of the comments
 * @author Bosco Chan Afaq Nabi
 */
public class RepliesActivity extends AppCompatActivity {

    ArrayAdapter<Comment> repliesAdapter;
    ArrayList<Comment> repliesDataList;
    ListView repliesList;

    private String currentID;
    private Experiment experiment;
    private Comment parentComment;

    FirebaseFirestore db;
    Database database = new Database();

    TextView owner;
    TextView date;
    TextView commentID;
    TextView commentText;

    private User user = User.getUser();

    FloatingActionButton addReply;
    Date curDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_replies);

        View addCommentView = LayoutInflater.from(RepliesActivity.this).inflate(R.layout.add_comment, null);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        parentComment = (Comment) intent.getSerializableExtra("Comment");

        owner = findViewById(R.id.comment_owner_username);
        owner.setText(parentComment.getUserUniqueID());

        date = findViewById(R.id.commentDate);
        date.setText(parentComment.getDate().toString());

        commentID = findViewById(R.id.commentID);
        commentID.setText(parentComment.getCommentID().substring(0,6));

        commentText = findViewById(R.id.commentText);
        commentText.setText(parentComment.getText());

        //    https://stackoverflow.com/questions/19826693/how-can-i-make-a-textview-automatically-scroll-as-i-add-more-lines-of-text
        EditText newComment = addCommentView.findViewById(R.id.newComment);
//        newComment.setMovementMethod(new ScrollingMovementMethod());

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

                                Comment comment = new Comment(commentText, user.getUserUniqueID(), UUID.randomUUID().toString(), curDate.getCurrentDate());

                                database.addCommentToDB(db.collection("Replies")
                                        .document(parentComment.getCommentID())
                                        .collection("Answers")
                                        .document(UUID.randomUUID().toString()), comment);

                                repliesAdapter.notifyDataSetChanged();
                                recreate();
                            }
                        }).create().show();
                Log.d("Test", "add replies button clicked");
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
