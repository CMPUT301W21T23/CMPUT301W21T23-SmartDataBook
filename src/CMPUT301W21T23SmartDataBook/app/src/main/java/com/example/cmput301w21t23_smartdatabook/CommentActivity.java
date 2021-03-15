package com.example.cmput301w21t23_smartdatabook;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions);

        commentList = findViewById(R.id.followedExpListView);

        commentDataList = new ArrayList<>();


        commentAdapter = new CommentList(this, commentDataList);
        commentList.setAdapter(commentAdapter);

    }
}
