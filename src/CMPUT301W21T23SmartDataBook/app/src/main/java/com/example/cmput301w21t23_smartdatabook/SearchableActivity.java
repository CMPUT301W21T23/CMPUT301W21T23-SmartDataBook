package com.example.cmput301w21t23_smartdatabook;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


/**
 * this class handles the search query, and do the appropriate actions
 * @Author Jayden
 */
public class SearchableActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
        }
    }

}
