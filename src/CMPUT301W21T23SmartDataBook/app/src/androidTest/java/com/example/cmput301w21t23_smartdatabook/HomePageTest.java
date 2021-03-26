package com.example.cmput301w21t23_smartdatabook;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;

import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.experimentDetails.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 *  Instrumented test, which will execute on an Android device.
 * @Author Afaq
 * @See https://developer.android.com/training/testing/ui-testing/espresso-testing#java
 */
public class HomePageTest {

    private Solo solo;
    private View addExpButton;
    private View searchBar;
    private CheckBox box;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    Database database = new Database();

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
    public void checkAddExpBtn(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.waitForFragmentById(R.layout.home_page, 5000);
        solo.clickOnView(addExpButton);
        solo.waitForFragmentById(R.layout.new_experiment_location_on, 5000);
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.new_experiment_location_on);
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
    public void checkExperimentDetailsBackBtn(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(1000);
        solo.clickInList(0);
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", ExperimentDetails.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
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
    public void checkClickSearchBarDropDown() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(searchBar);
        solo.enterText(0,"Grain");
        solo.sleep(6000);
        // TODO: click an experiment and check the experiment details activity
        solo.assertCurrentActivity("Wrong Activity", ExperimentDetails.class);
    }

    @Test
    public void checkUnFollow() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(1000);
        solo.clickOnScreen(930, 622);
        solo.sleep(1000);
        box = rule.getActivity().findViewById(R.id.fav);
        assertTrue(box.isChecked()); //Box shouldn't return a false as the owner can't unfollow their own experiment

    }





}
