package com.example.cmput301w21t23_smartdatabook.mainController;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmput301w21t23_smartdatabook.QRCode.ScanQrActivity;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.archives.ArchivePage;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.fav.FavPage;
import com.example.cmput301w21t23_smartdatabook.home.addExpFragment;
import com.example.cmput301w21t23_smartdatabook.home.homePage;
import com.example.cmput301w21t23_smartdatabook.settings.SettingsPage;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

/**
 * the Purpose of this class is to register the user in the database and initialize the bottom tab navigation
 * functionality of the app
 * the bottom tab should get covered if a new acitivity is opened
 *
 * @Author Afaq, Jayden, Natnail Ghebresilasie
 * @Refrences https://androidwave.com/bottom-navigation-bar-android-example/
 */

public class MainActivity extends AppCompatActivity{

    BottomNavigationView bottomNavigation;

    private ActionBar toolbar;
    private boolean searchShow;

    public Database database;

    public User user;

//    //Implement interrupted exception throw on database object instantiation
//    {
//        try {
//            database = new Database(this);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    // VERY IMPORTANT FUNCTION
    // searchShow is set to false by default
    // if fragment is homePage or FavPage, set searchShow to true
    // invalidateOptionsMenu calls onCreateOptionsMenu

    /**
     * This method runs when new fragment is attached to this activity
     * @param fragment
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        searchShow = fragment instanceof homePage;
        if (fragment instanceof FavPage) searchShow = true;
        if (fragment instanceof addExpFragment) bottomNavigation.setVisibility(View.GONE);
        invalidateOptionsMenu();
    }


    public String test() {
        return "This means you can get the attribute of MainActivity, call made from a Fragment";
    }

    /**
     * This method runs on the creation of the main activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.app_toolbar));
        toolbar = getSupportActionBar();
        database = new Database();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Home");

        //anonymous authentication testing
        database.authenticateAnon(new GeneralDataCallBack() {
            @Override
            public void onDataReturn(Object returnedData) {
                String userID = (String) returnedData;
                database = Database.getDataBase();

                user = User.getUser();
                user.setUserUniqueID(userID);

                if (user.getUserName().length() == 0){
                    user.setUserName("User - " + userID.substring(0,4));
                }

                final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, homePage.newInstance(""));
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();


            }
        });

    } //onCreate

    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().getCurrentUser().delete();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_qr:
                startActivity(new Intent(this, ScanQrActivity.class));
                break;
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }


    /**
     * THis method set up menu's search icon
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu); // Make search thing visible

        menu.findItem(R.id.app_bar_search).setVisible(searchShow); // This is to change visibility of search according to current fragment or status

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();


        // I have no idea
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
                if (currentFragment != null && currentFragment.isVisible() && !query.equals("")) {
                    if (currentFragment instanceof homePage) {
                        ((homePage)currentFragment).doUpdate(query, currentFragment);
                    }
                    if (currentFragment instanceof FavPage) {
                        ((FavPage)currentFragment).doUpdate(query, currentFragment);
                    }
                }


                return false;
            }

            // This gets called every time text is updated, AND search edittext is clicked
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("queryTextSubmit", "AFAQ NABI");
                onQueryTextSubmit(newText);
//                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
//                if (currentFragment != null && currentFragment.isVisible() && !newText.equals("")) {
//                    if (currentFragment instanceof homePage) {
//                        ((homePage)currentFragment).doUpdate(newText);
//                    }
//                    if (currentFragment instanceof FavPage) {
//                        ((FavPage)currentFragment).doUpdate(newText);
//                    }
//                }

                return false;
            }


        });

        return true;
    }

    /**
     * This function supports opening fragments
     * @param fragment
     */
    public void openFragment(Fragment fragment) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

//    user.setUserUniqueID(currentID);

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home_nav:
                            toolbar.setTitle("Home");
                            openFragment(homePage.newInstance(""));
                            return true;

                        case R.id.fav_nav:
                            toolbar.setTitle("Favorites");
                            openFragment(FavPage.newInstance(""));
                            return true;

                        case R.id.settings_nav:
                            toolbar.setTitle("Settings");
                            openFragment(SettingsPage.newInstance(""));
                            return true;
                        case R.id.archived_nav:
                            toolbar.setTitle("Archived");
                            openFragment(ArchivePage.newInstance(""));
                            return true;
                    }
                    return false;
                }
            };

}//mainActivity