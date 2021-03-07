package com.example.cmput301w21t23_smartdatabook;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CommentSection extends AppCompatActivity {

    ListView commentList;

    ArrayAdapter<Comment> commentAdapter;
    ArrayList<Comment> commentDataList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.comment_section);

        commentList = findViewById(R.id.commentList);
        
        commentDataList.add(new Comment ("Hello", "01", commentDataList.size() + 1 ) );
        
        
    }


}