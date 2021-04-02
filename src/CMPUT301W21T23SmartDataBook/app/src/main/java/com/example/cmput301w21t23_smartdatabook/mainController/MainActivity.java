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
import com.example.cmput301w21t23_smartdatabook.QRCode.ScannerActivity;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.archives.ArchivePage;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.fav.FavPage;
import com.example.cmput301w21t23_smartdatabook.home.addExpFragment;
import com.example.cmput301w21t23_smartdatabook.home.homePage;
import com.example.cmput301w21t23_smartdatabook.settings.SettingsPage;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.gms.maps.model.LatLng;
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

    private ActionBar toolbar;
    private boolean searchShow;
    private LatLng lkll;

    public Database database;

    public User user;

//    LocationCallback m = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            Log.d("debug", "working????");
//            Location lastKnownLocation = locationResult.getLastLocation();
//            lkll =  new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
//        }
//    };

//    public LatLng getLastKnownLatLng() {
//        return this.lkll;
//    }
//
//    public LocationCallback getLocationCallback() {
//        return m;
//    }

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
        if (fragment instanceof ArchivePage) searchShow = true;
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
                startActivity(new Intent(this, ScannerActivity.class));
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
                    if (currentFragment instanceof ArchivePage) {
                        ((ArchivePage)currentFragment).doUpdate(query, currentFragment);
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

//    public void getLatLng() {
//        Dexter.withContext(getApplicationContext())
//			.withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//			.withListener(new PermissionListener() {
//				@Override
//				public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//					FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
//			        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//			            // Following check removed from conditionals
//			        	// ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//	                        	Log.d("lwpInstance", "working?");
//				         fusedLocationClient.requestLocationUpdates(LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY), m, Looper.myLooper());
//
//				        // Below method executes when location is successfully obtained, deprecated due to above mLocationCallback
//			            fusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
//			                @Override
//			                public void onSuccess(Location location) {
//			                    // Got last known location. In some rare situations this can be null.
//			                    if (location != null) {
//			                        MainActivity.this.lkll = new LatLng(location.getLatitude(), location.getLongitude());
//			                    } else {
//			                        System.out.println("Location do not exist yet. Maybe something wrong?");
//			                    }
//			                }
//			            });
//			        }
//				}
//				@Override
//				public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                    Toast.makeText(getBaseContext(), "" + permissionDeniedResponse.isPermanentlyDenied(), Toast.LENGTH_SHORT).show();
//
//                    //Source: Opeyemi, https://stackoverflow.com/users/8226150/opeyemi
//                    //Code: https://stackoverflow.com/questions/50639292/detecting-wether-a-permission-can-be-requested-or-is-permanently-denied
//                    if (permissionDeniedResponse.isPermanentlyDenied()) {
//                        //permission is permanently denied navigate to user setting
//                        new AlertDialog.Builder(MainActivity.this)
//                                .setTitle("Camera permission was denied permanently.")
//                                .setMessage("Allow Camera access through your settings.")
//                                .setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
//                                        intent.setData(uri);
//                                        startActivityForResult(intent, 101);
//                                        dialog.cancel();
//                                    }
//                                })
//                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.cancel();
//                                    }
//                                }
//                        ).show();
//                    }
//                }
//				@Override
//				public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//					permissionToken.continuePermissionRequest();
//
//				}
//			}).check();
////        generalDataCallBack.onDataReturn(null);
//    }

}//mainActivity