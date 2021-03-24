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

import com.example.cmput301w21t23_smartdatabook.Database;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RepliesActivity extends AppCompatActivity {

    ArrayAdapter<Comment> repliesAdapter;
    ArrayList<Comment> repliesDataList;

    private String currentID;
    private Experiment experiment;
    private Comment parentComment;

    FirebaseFirestore db;
    Database database;

    TextView owner;
    TextView date;
    TextView commentID;
    TextView commentText;
    ListView repliesList;
    FloatingActionButton addReply;

//    https://stackoverflow.com/questions/19826693/how-can-i-make-a-textview-automatically-scroll-as-i-add-more-lines-of-text
//    outputText.setMovementMethod(new ScrollingMovementMethod());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_replies);

        View addCommentView = LayoutInflater.from(RepliesActivity.this).inflate(R.layout.add_comment, null);

        Intent intent = getIntent();
        currentID = intent.getStringExtra("currentID");
        parentComment = (Comment) intent.getSerializableExtra("Comment");

        owner = findViewById(R.id.comment_owner_username);
        owner.setText(parentComment.getUserUniqueID());

        date = findViewById(R.id.commentDate);
        date.setText((CharSequence) parentComment.getDate());

        commentID = findViewById(R.id.commentID);
        commentID.setText(parentComment.getCommentID());

        commentText = findViewById(R.id.commentText);
        commentText.setText(parentComment.getText());

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

                                HashMap<String, String> data = new HashMap<>();
                                data.put("CommentText", commentText);
                                data.put("UserID", currentID);
                                data.put("CommentID", UUID.randomUUID().toString());
                                database.fillCommentList(repliesDataList, repliesAdapter, currentID);
                            }

                        }).create().show();
                Log.d("Test", "add replies button clicked");
            }
        });

        repliesList = findViewById(R.id.replies_list);
        repliesDataList = new ArrayList<>();

        repliesDataList.add(new Comment("test replies",currentID,"12", "03/21/2021"));
        repliesDataList.add(new Comment("test replies",currentID,"12", "03/21/2021"));
        repliesDataList.add(new Comment("test replies",currentID,"12", "03/21/2021"));
        repliesDataList.add(new Comment("test replies",currentID,"12", "03/21/2021"));
        repliesDataList.add(new Comment("test replies",currentID,"12", "03/21/2021"));
        repliesDataList.add(new Comment("test replies",currentID,"12", "03/21/2021"));

        repliesDataList.add(new Comment("test replies",currentID,"12", "03/21/2021"));

        repliesDataList.add(new Comment("test replies",currentID,"12", "03/21/2021"));

        repliesDataList.add(new Comment("test replies",currentID,"12", "03/21/2021"));

        repliesDataList.add(new Comment("test replies",currentID,"12", "03/21/2021"));

        repliesDataList.add(new Comment("test replies",currentID,"12", "03/21/2021"));



        repliesAdapter = new CommentList(this, repliesDataList, currentID);
        repliesList.setAdapter(repliesAdapter);


    }

}
