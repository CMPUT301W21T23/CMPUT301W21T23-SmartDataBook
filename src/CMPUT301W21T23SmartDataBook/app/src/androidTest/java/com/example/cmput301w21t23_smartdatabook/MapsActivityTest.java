package com.example.cmput301w21t23_smartdatabook;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.cmput301w21t23_smartdatabook.geolocation.MapsActivity;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MapsActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    //Finally, add tearDown() method using the @After tag to run after every test method.
    //This method closes the activity after each test.
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

    @Test
    public void checkOpenMap() {
        solo.clickOnScreen(1013, 133);
        solo.assertCurrentActivity("Right Activity", MapsActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Right Activity", MainActivity.class);
    }

}
