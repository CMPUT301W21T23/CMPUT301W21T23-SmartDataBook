package com.example.cmput301w21t23_smartdatabook.comments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.comments.Comment;
import com.example.cmput301w21t23_smartdatabook.R;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions);

        Intent getIntent = getIntent();
        currentID = getIntent.getExtras().getString("currentID");
        experiment = (Experiment) getIntent.getSerializableExtra("Experiment");

        commentList = findViewById(R.id.followedExpListView);
        commentDataList = new ArrayList<>();
        commentAdapter = new CommentList(this, commentDataList);
        commentList.setAdapter(commentAdapter);



    }
}
