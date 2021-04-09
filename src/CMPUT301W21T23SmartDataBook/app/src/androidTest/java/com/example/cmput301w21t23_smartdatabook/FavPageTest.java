package com.example.cmput301w21t23_smartdatabook;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.cmput301w21t23_smartdatabook.experiment.Experiment;
import com.example.cmput301w21t23_smartdatabook.experiment.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.robotium.solo.Solo;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test the UI of the favPage.java class. Assumes that an experiment object is already made
 */
public class FavPageTest {
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);
    private Solo solo;
    private View addExpButton;
    private View searchBar;

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        addExpButton = rule.getActivity().findViewById(R.id.add_experiment_button);
        searchBar = rule.getActivity().findViewById((R.id.app_bar_search));
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    //Finally, add tearDown() method using the @After tag to run after every test method.
    //This method closes the activity after each test.
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

    // check if the item in fav fragment is clickable
    // Pre-Req: that there exists an experiment in the favorite fragment
    @Test
    public void checkClickExperiment() {

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(1000);
        solo.clickOnScreen(444, 1925); //"Favorite" Menu Item
        solo.sleep(2000);
        solo.clickInList(0);
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", ExperimentDetails.class);
    }

    // check if searchbar is clickable
    @Test
    public void checkSearchBar() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(searchBar);
        solo.enterText(0, "Grain");
        solo.sleep(6000);
        // TODO: check the output and click one
    }

    // check if the result form search bar is clickable
    @Test
    public void checkClickSearchBarDropDown() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(searchBar);
        solo.enterText(0, "Grain");
        solo.sleep(6000);
        // TODO: click an experiment and check the experiment details activity
        solo.assertCurrentActivity("Wrong Activity", ExperimentDetails.class);
    }

    // check if the back button on the android device works
    @Test
    public void checkBackButton() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(1000);
        solo.clickOnScreen(444, 1925); //"Favorite" Menu Item
        solo.sleep(1000);

        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", ExperimentDetails.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    // check if the fav page experiments displays the details fo the experiment properly
    // Pre-Req: that there exists an experiment in the favorite fragment
    @Test
    public void checkDetails() {
        solo.sleep(3000);
        ListView experimentList = rule.getActivity().findViewById(R.id.experiment_list);
        Experiment experiment = (Experiment) experimentList.getItemAtPosition(0);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(1000);
        solo.clickOnScreen(444, 1925); //"Favorite" Menu Item
        solo.sleep(3000);
        solo.clickInList(0);
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", ExperimentDetails.class);
        solo.sleep(1000);
        TestCase.assertTrue(solo.searchText(experiment.getExpName()));
        TestCase.assertTrue(solo.searchText(experiment.getDate()));
        TestCase.assertTrue(solo.searchText(experiment.getDescription()));
        TestCase.assertTrue(solo.searchText(experiment.getOwnerUserID()));
        TestCase.assertTrue(solo.searchText("Max Trials: " + experiment.getMaxTrials()));
        TestCase.assertTrue(solo.searchText("Min Trials: " + experiment.getMinTrials()));
        TestCase.assertTrue(solo.searchText(experiment.getTrialType()));
        assertEquals(solo.getView(R.id.Publish_text).getVisibility(), (View.INVISIBLE));
        assertEquals(solo.getView(R.id.endExp).getVisibility(), (View.INVISIBLE));

    }

    // check if one can follow the experiemtn and if it shows up in the fav frgament
    // Pre-Req: that there exists an experiment in the homePage Fragment
    @Test
    public void followExp() {
        solo.sleep(3000);
        ListView experimentList = rule.getActivity().findViewById(R.id.experiment_list);
        Experiment experiment = (Experiment) experimentList.getItemAtPosition(0);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(1000);
        solo.clickOnScreen(929, 621);
        solo.sleep(2000);
        solo.clickOnScreen(444, 1925); //"Favorite" Menu Item
        solo.sleep(3000);
        solo.searchText(experiment.getExpName());
        solo.clickOnScreen(1949, 178);
        solo.sleep(2000);
        solo.clickOnScreen(929, 621);

    }

    // check if one can follow and unfollow the experiemtn and if it shows up in the fav frgament and gets removed from the fav fragment upon unfollow
    // Pre-Req: that there exists an experiment in the homePage Fragment
    @Test
    public void unfollowExp() {
        solo.sleep(3000);
        ListView experimentList = rule.getActivity().findViewById(R.id.experiment_list);
        Experiment experiment = (Experiment) experimentList.getItemAtPosition(0);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(1000);
        solo.clickOnScreen(929, 621); // follow experiment
        solo.sleep(2000);
        solo.clickOnScreen(444, 1925); //"Favorite" Menu Item
        solo.sleep(3000);
        assertTrue(solo.searchText(experiment.getExpName()));
        solo.sleep(2000);
        solo.clickOnScreen(73, 1920); //"Home" Menu Item
        solo.sleep(2000);
        solo.clickOnScreen(929, 621); // unfollow experiment
        solo.sleep(6000);
        solo.clickOnScreen(444, 1925); //"Favorite" Menu Item
        solo.sleep(6000);
        assertFalse(solo.searchText(experiment.getExpName()));
    }

}
