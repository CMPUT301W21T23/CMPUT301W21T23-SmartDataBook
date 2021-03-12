package com.example.cmput301w21t23_smartdatabook;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmput301w21t23_smartdatabook.fav.favPage;
import com.example.cmput301w21t23_smartdatabook.home.homePage;
import com.example.cmput301w21t23_smartdatabook.settings.settingsPage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;


public class SettingsActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private ActionBar toolbar;

    TextView usernameTxtView;
    TextView emailTxtView;
    Button editButtonSettings;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        db = FirebaseFirestore.getInstance();

        toolbar = getSupportActionBar();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        usernameTxtView = findViewById(R.id.usernameTxtView);
        emailTxtView = findViewById(R.id.emailTxtView);
        editButtonSettings = findViewById(R.id.editButtonSettings);
        currentUser = mAuth.getCurrentUser();





    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home_nav:
                            toolbar.setTitle("Home");
                            openFragment(homePage.newInstance("",""));
                            return true;

                        case R.id.fav_nav:
                            toolbar.setTitle("Favorites");
                            openFragment(favPage.newInstance("",""));
                            return true;

                        case R.id.settings_nav:
                            toolbar.setTitle("Settings");
                            openFragment(settingsPage.newInstance("",""));
                            return true;
                    }
                    return false;
                }
            };
}
