package com.example.cmput301w21t23_smartdatabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cmput301w21t23_smartdatabook.fav.favPage;
import com.example.cmput301w21t23_smartdatabook.settings.settingsPage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

import com.example.cmput301w21t23_smartdatabook.home.homePage;

//https://androidwave.com/bottom-navigation-bar-android-example/ -> bottom tab navigation
public class MainActivity extends AppCompatActivity {

    ListView experimentList;
    ArrayAdapter<Experiment> experimentAdapter;
    ArrayList<Experiment> experimentDataList;

    BottomNavigationView bottomNavigation;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Home");
        openFragment(homePage.newInstance("",""));

        //anonymous authentication testing
//        mAuth = FirebaseAuth.getInstance();
//
//        //experimentList testing
//        experimentList = findViewById(R.id.experimentList);
//        experimentDataList = new ArrayList<>();
//
//        experimentDataList.add(new Experiment("first", "123",
//                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));
//        experimentDataList.add(new Experiment("second", "123",
//                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));
//        experimentDataList.add(new Experiment("third", "123",
//                "Binomial", "testtrial", false, 30,60, true, "03/05/2021"));
//
//        experimentAdapter = new CardList(this, experimentDataList);
//
//        experimentList.setAdapter(experimentAdapter);
//
//        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MainActivity.this, CommentActivity.class);
//                startActivity(intent);
//            }
//        });

    }//onCreate
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