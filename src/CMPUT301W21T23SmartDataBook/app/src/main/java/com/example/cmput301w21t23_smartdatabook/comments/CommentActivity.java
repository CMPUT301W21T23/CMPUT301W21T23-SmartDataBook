package com.example.cmput301w21t23_smartdatabook.comments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.stats.StringDate;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.experiment.Experiment;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.example.cmput301w21t23_smartdatabook.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Class: CommentActivity
 * shows the activity of the comments
 * @author Bosco Chan, Afaq Nabi
 * @see Comment
 */
public class CommentActivity extends AppCompatActivity {
    // initialize variables
    ListView commentList;
    ArrayAdapter<Comment> commentAdapter;
    ArrayList<Comment> commentDataList;

    private Experiment experiment;

    FirebaseFirestore db;
    Database database = new Database();
    User user = User.getUser();

    StringDate stringDate = new StringDate();

    /**
     * onCreate method for comment, sets up most of the comment activity page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_list);
        View addCommentView = LayoutInflater.from(CommentActivity.this).inflate(R.layout.add_comment, null);

        db = FirebaseFirestore.getInstance();

        Intent getIntent = getIntent();
        experiment = (Experiment) getIntent.getSerializableExtra("Experiment");

        commentList = findViewById(R.id.comment_list);

        commentDataList = new ArrayList<>();

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
                builder.setTitle("New Reply");
                builder.setNegativeButton("Cancel", null)
                        .setPositiveButton("Add Comment", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String commentText = newComment.getText().toString();

                                Comment comment = new Comment(commentText, user.getUserUniqueID(), UUID.randomUUID().toString(), stringDate.getCurrentDate());

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
