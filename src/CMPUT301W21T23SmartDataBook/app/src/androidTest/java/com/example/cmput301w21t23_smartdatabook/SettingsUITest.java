package com.example.cmput301w21t23_smartdatabook;

import android.app.Activity;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SettingsUITest {

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
    public void checkInputs() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(1000);
        solo.clickOnScreen(780, 1943); //"Settings" Menu Item
        solo.clickInList(0);
        //insert fragment check here
    }
}
