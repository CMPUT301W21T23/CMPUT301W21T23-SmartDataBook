package com.example.cmput301w21t23_smartdatabook;

import android.app.Activity;
import android.content.ClipData;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test the UI of the favPage.java class. Assumes that an experiment object is already made
 */
public class FavPageTest {
    private Solo solo;
    private View addExpButton;
    private View searchBar;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        addExpButton = rule.getActivity().findViewById(R.id.add_experiment_button);
        searchBar = rule.getActivity().findViewById((R.id.app_bar_search));
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    //Finally, add tearDown() method using the @After tag to run after every test method.
    //This method closes the activity after each test.
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

    @Test
    public void checkClickExperiment(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(1000);
        solo.clickInList(0);
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", ExperimentDetails.class);
    }

    @Test
    public void checkSearchBar(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(searchBar);
        solo.enterText(0,"Grain");
        solo.sleep(6000);
        // TODO: check the output and click one
    }

    @Test
    public void checkClickSearchBarDropDown(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(searchBar);
        solo.enterText(0,"Grain");
        solo.sleep(6000);
        // TODO: click an experiment and check the experiment details activity
        solo.assertCurrentActivity("Wrong Activity", ExperimentDetails.class);
    }

    @Test
    public void checkBackButton(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(1000);
        solo.clickOnScreen(444, 1925); //"Favorite" Menu Item
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", ExperimentDetails.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

}
