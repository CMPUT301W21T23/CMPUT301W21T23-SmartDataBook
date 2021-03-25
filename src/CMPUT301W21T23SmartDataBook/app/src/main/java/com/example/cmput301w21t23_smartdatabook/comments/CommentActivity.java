package com.example.cmput301w21t23_smartdatabook.comments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.Database;
import com.example.cmput301w21t23_smartdatabook.Date;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.User;
import com.example.cmput301w21t23_smartdatabook.comments.Comment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.experimentDetails.UploadTrial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Class: CommentActivity
 * shows the activity of the comments
 * @author Bosco Chan
 */
public class CommentActivity extends AppCompatActivity {
    ListView commentList;
    ArrayAdapter<Comment> commentAdapter;
    ArrayList<Comment> commentDataList;

    private String currentID;
    private Experiment experiment;

    FirebaseFirestore db;
    Database database = new Database();
    User user = User.getUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_list);
        View addCommentView = LayoutInflater.from(CommentActivity.this).inflate(R.layout.add_comment, null);

        db = FirebaseFirestore.getInstance();

        Intent getIntent = getIntent();
        currentID = getIntent.getExtras().getString("currentID");
        experiment = (Experiment) getIntent.getSerializableExtra("Experiment");

        commentList = findViewById(R.id.comment_list);

        commentDataList = new ArrayList<>();

        Date date = new Date();

        commentAdapter = new CommentList(this, commentDataList);

        commentList.setAdapter(commentAdapter);

        database.fillCommentList(db.collection("Comments")
                .document(experiment.getExpID())
                .collection("Questions"), commentDataList, commentAdapter);

        //    https://stackoverflow.com/questions/19826693/how-can-i-make-a-textview-automatically-scroll-as-i-add-more-lines-of-text
        EditText newComment = addCommentView.findViewById(R.id.newComment);
//        newComment.setMovementMethod(new ScrollingMovementMethod());

        FloatingActionButton addCommentButton;
        addCommentButton = findViewById(R.id.add_comment_button);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                builder.setView(addCommentView);
                builder.setTitle("New Rpely");
                builder.setNegativeButton("Cancel", null)
                        .setPositiveButton("Add Comment", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String commentText = newComment.getText().toString();

                                Comment comment = new Comment(commentText, user.getUserUniqueID(), UUID.randomUUID().toString(), date.getDate());

                                database.addCommentToDB(db.collection("Comments")
                                        .document(experiment.getExpID())
                                        .collection("Questions")
                                        .document(UUID.randomUUID().toString()), comment);

                                commentAdapter.notifyDataSetChanged();
                                recreate();
                            }
                        }).create().show();
            }
        });

        commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), RepliesActivity.class);
                intent.putExtra("Comment", commentDataList.get(position));
                startActivity(intent);
            }
        });

    }
}
