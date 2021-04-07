package com.example.cmput301w21t23_smartdatabook.mainController;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.archives.ArchivePage;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.fav.FavPage;
import com.example.cmput301w21t23_smartdatabook.geolocation.MapsActivity;
import com.example.cmput301w21t23_smartdatabook.home.addExpFragment;
import com.example.cmput301w21t23_smartdatabook.home.homePage;
import com.example.cmput301w21t23_smartdatabook.settings.SettingsPage;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


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

    private boolean searchShow;
    private boolean mapShow;
    private boolean from_user;

    public Database database;

    public User user;

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
        mapShow = fragment instanceof homePage;
        if (fragment instanceof FavPage) searchShow = true;
        if (fragment instanceof ArchivePage) searchShow = true;
        if (fragment instanceof addExpFragment) bottomNavigation.setVisibility(View.GONE);
        invalidateOptionsMenu();
        Log.d("MainActivity:73", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()) + " (Size of the stack. 1 = home)");
    }

    /**
     * This receives a request from other fragments to update the selected item of the bottom navigation, without triggering the onNavigationItemSelectedListener.
     * @param targetMenu
     */
    public void setBottomNavigationItem(int targetMenu) {
        // Touch this, and you will fall in a never-ending loop of onAttachFragment -> BottomNavigationView.onClickListner -> Open new fragment -> onAttachFragment
        from_user = false;

        bottomNavigation.setSelectedItemId(targetMenu);
    }

    /**
     * This handles the action of the back button pressed of the android device while on MainActivity.
     */
    @Override
    public void onBackPressed() {
        // From stackoverflow: https://stackoverflow.com/questions/5448653/how-to-implement-onbackpressed-in-fragments
        // Answer: https://stackoverflow.com/posts/24881908/revisions
        // By Hw.Master: https://stackoverflow.com/users/1072254/hw-master

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 1) {
            // From stackoverflow: https://stackoverflow.com/questions/3105673/how-to-kill-an-application-with-all-its-activities
            // Answer: https://stackoverflow.com/posts/10597017/revisions
            // By Thirumalvalavan: https://stackoverflow.com/users/1404798/thirumalvalavan
            android.os.Process.killProcess(android.os.Process.myPid());
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

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
        ActionBar toolbar = getSupportActionBar();
        database = new Database();
        from_user = true;

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

    /**
     * The function runs on the destruction of the main activity, no longer can get insstance of the current user
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().getCurrentUser().delete();
    }

    /**
     * This function handles when the user clicked map, it goes to map activity
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_map:
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra("main", "true");
                startActivity(intent);
                break;
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**
     * This method set up menu's search icon
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu); // Make search thing visible

        menu.findItem(R.id.app_bar_search).setVisible(searchShow); // This is to change visibility of search according to current fragment or status
        menu.findItem(R.id.app_bar_map).setVisible(mapShow);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        // I have no idea
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
                if (currentFragment != null && currentFragment.isVisible()) {
                    if (currentFragment instanceof homePage) {
                        ((homePage)currentFragment).doUpdate(query);
                    }
                    if (currentFragment instanceof FavPage) {
                        ((FavPage)currentFragment).doUpdate(query);
                    }
                    if (currentFragment instanceof ArchivePage) {
                        ((ArchivePage)currentFragment).doUpdate(query);
                    }

                }


                return false;
            }

            // This gets called every time text is updated, AND search edittext is clicked
            @Override
            public boolean onQueryTextChange(String newText) {
                onQueryTextSubmit(newText);
                return false;
            }
        });

        return true;
    }

    /**
     * This function supports opening fragments, by using fragment transaction
     * @param fragment
     */
    public void openFragment(Fragment fragment) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (from_user) {
                        switch (item.getItemId()) {
                            case R.id.home_nav:
                                openFragment(homePage.newInstance(""));
                                return true;

                            case R.id.fav_nav:
                                openFragment(FavPage.newInstance(""));
                                return true;

                            case R.id.settings_nav:
                                openFragment(SettingsPage.newInstance(""));
                                return true;

                            case R.id.archived_nav:
                                openFragment(ArchivePage.newInstance(""));
                                return true;
                        }
                    } else {
                        from_user = true;
                        return true;
                    }
                    return false;
                }
            };


}//mainActivity