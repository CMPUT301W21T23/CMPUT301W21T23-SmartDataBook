package com.example.cmput301w21t23_smartdatabook;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.cmput301w21t23_smartdatabook.experimentDetails.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.example.cmput301w21t23_smartdatabook.trials.UploadTrial;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ExperimentDetailsPageTest {
    private Solo solo;
    private View addTrialsBtn;
    private View addExpButton;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        addExpButton = rule.getActivity().findViewById(R.id.add_experiment_button);

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
    public void checkAddTrialsButton(){
        solo.assertCurrentActivity("Wrong Acitvity", MainActivity.class);
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", ExperimentDetails.class);
        solo.sleep(6000);
        solo.clickOnButton("UPLOAD TRIALS");
        solo.sleep(2000);
        solo.assertCurrentActivity("right Activity", UploadTrial.class);
        solo.sleep(2000);
        solo.goBack();
        solo.sleep(2000);
        solo.assertCurrentActivity("Right Activity", ExperimentDetails.class);
    }

    // check if the fav page experiments displays the details fo the experiment properly
    // Pre-Req: that there exists an experiment in the home Fragment
    @Test
    public void checkExpInfo(){
        solo.sleep(6000);

        solo.assertCurrentActivity("Wrong Acitvity", MainActivity.class);

        ListView experimentList = rule.getActivity().findViewById(R.id.experiment_list);
        Experiment experiment = (Experiment) experimentList.getItemAtPosition(0);

        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", ExperimentDetails.class);
        solo.sleep(1000);
        assertTrue(solo.searchText(experiment.getExpName()));
        assertTrue(solo.searchText(experiment.getDate()));
        assertTrue(solo.searchText(experiment.getDescription()));
        assertTrue(solo.searchText(experiment.getOwnerUserID()));
        assertTrue(solo.searchText("Max Trials: "+String.valueOf(experiment.getMaxTrials())));
        assertTrue(solo.searchText("Min Trials: "+String.valueOf(experiment.getMinTrials())));
        assertTrue(solo.searchText(experiment.getTrialType()));
        assertEquals(solo.getView(R.id.Publish_text).getVisibility(), (View.INVISIBLE));
        assertEquals(solo.getView(R.id.endExp).getVisibility(), (View.INVISIBLE));
    }

    // check if the option to end experiment and and publish are visible for owner also check if the publish checkBox is checked
    // Pre-Req: that there exists an experiment in the favorite fragment
    @Test
    public void checkExpInfoForOwner(){
        solo.assertCurrentActivity("Wrong Class", MainActivity.class);
        solo.waitForFragmentById(R.layout.home_page, 1000);
        solo.sleep(7000);
        solo.clickOnView(addExpButton);
        solo.waitForFragmentById(R.layout.new_experiment_location_on, 1000);

        //Source: Bouabane Mohamed Salah; https://stackoverflow.com/users/1600405/bouabane-mohamed-salah
        //Code: https://stackoverflow.com/questions/30456474/set-numberpicker-value-with-robotium
        rule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NumberPicker minPicker = rule.getActivity().findViewById(R.id.minTrialsNumberPicker);
                minPicker.setValue(10);
            }
        });
        solo.sleep(1000);
        rule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NumberPicker maxPicker = rule.getActivity().findViewById(R.id.maxTrialsNumberPicker);
                maxPicker.setValue(30);
            }
        });

        solo.enterText( (EditText) solo.getView(R.id.newExperimentLocationOnExperimentNameEditText), "checkExpInfoForOwner()");
        solo.sleep(1000);
        solo.enterText( (EditText) solo.getView(R.id.newExperimentLocationOnExperimentDescriptionEditText), "checkExpInfoForOwner()");
        solo.sleep(1000);
        solo.clickOnRadioButton(2);
        solo.sleep(1000);
        solo.clickOnView(rule.getActivity().findViewById(R.id.newExperimentLocationToggleSwitch));
        solo.sleep(1000);
        solo.clickOnButton("Create");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Class", MainActivity.class);

        solo.sleep(3000);

        ListView experimentList = rule.getActivity().findViewById(R.id.experiment_list);
        Experiment experiment = (Experiment) experimentList.getItemAtPosition(0);

        solo.clickInList(0);

        solo.assertCurrentActivity("Wrong Activity", ExperimentDetails.class);
        assertEquals(solo.getView(R.id.Publish_text).getVisibility(), (View.VISIBLE));
        assertEquals(solo.getView(R.id.endExp).getVisibility(), (View.VISIBLE));
        solo.isCheckBoxChecked(0);

    }

    // check if the publish box is unchecked if the experiment is not published on creation
    // also check thats the experiment is not added in the homePage fragment
    // Pre-Req: the created experiment must be the first experiment made by the user
    @Test
    public void checkPublishedBoxUnchecked(){
        solo.assertCurrentActivity("Wrong Class", MainActivity.class);
        solo.waitForFragmentById(R.layout.home_page, 1000);
        solo.sleep(9000);
        solo.clickOnScreen(975, 1799);
        solo.waitForFragmentById(R.layout.new_experiment_location_on, 1000);

        //Source: Bouabane Mohamed Salah; https://stackoverflow.com/users/1600405/bouabane-mohamed-salah
        //Code: https://stackoverflow.com/questions/30456474/set-numberpicker-value-with-robotium
        rule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NumberPicker minPicker = rule.getActivity().findViewById(R.id.minTrialsNumberPicker);
                minPicker.setValue(10);
            }
        });
        solo.sleep(1000);
        rule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NumberPicker maxPicker = rule.getActivity().findViewById(R.id.maxTrialsNumberPicker);
                maxPicker.setValue(30);
            }
        });

        solo.enterText( (EditText) solo.getView(R.id.newExperimentLocationOnExperimentNameEditText), "checkPublishedBoxUnchecked()");
        solo.sleep(1000);
        solo.enterText( (EditText) solo.getView(R.id.newExperimentLocationOnExperimentDescriptionEditText), "checkPublishedBoxUnchecked()");
        solo.sleep(1000);
        solo.clickOnRadioButton(2);
        solo.sleep(1000);
        solo.clickOnView(rule.getActivity().findViewById(R.id.newExperimentLocationToggleSwitch));
        solo.sleep(1000);
        solo.clickOnView(rule.getActivity().findViewById(R.id.newExperimentLocationPublishToggleSwitch));
        solo.sleep(2000);
        solo.clickOnButton("Create");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Class", MainActivity.class);

        solo.sleep(3000);

        assertFalse(solo.searchText("checkPublishedBoxUnchecked()"));

        solo.sleep(3000);

        solo.clickOnScreen(444, 1925); //"Favorite" Menu Item

        solo.sleep(1000);

        ListView experimentList = rule.getActivity().findViewById(R.id.followedExpListView);
        Experiment experiment = (Experiment) experimentList.getItemAtPosition(0);

        solo.sleep(2000);

        solo.clickInList(0);

//        solo.clickOnScreen(73, 1920); //"Home" Menu Item

        solo.clickOnText("checkPublishedBoxUnchecked()");

        assertEquals(solo.getView(R.id.Publish_text).getVisibility(), (View.VISIBLE));
        assertEquals(solo.getView(R.id.endExp).getVisibility(), (View.VISIBLE));

        assertFalse(solo.isCheckBoxChecked(0));
    }

    // check if the publish box is unchecked if the experiment is not published on creation and clicks publish to see if it appears in homePage fragment
    @Test
    public void checkClickPublish(){
        solo.assertCurrentActivity("Wrong Class", MainActivity.class);
        solo.waitForFragmentById(R.layout.home_page, 1000);
        solo.sleep(9000);
        solo.clickOnScreen(975, 1799);
        solo.waitForFragmentById(R.layout.new_experiment_location_on, 1000);

        //Source: Bouabane Mohamed Salah; https://stackoverflow.com/users/1600405/bouabane-mohamed-salah
        //Code: https://stackoverflow.com/questions/30456474/set-numberpicker-value-with-robotium
        rule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NumberPicker minPicker = rule.getActivity().findViewById(R.id.minTrialsNumberPicker);
                minPicker.setValue(10);
            }
        });
        solo.sleep(1000);
        rule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NumberPicker maxPicker = rule.getActivity().findViewById(R.id.maxTrialsNumberPicker);
                maxPicker.setValue(30);
            }
        });

        solo.enterText( (EditText) solo.getView(R.id.newExperimentLocationOnExperimentNameEditText), "checkClickPublish()");
        solo.sleep(1000);
        solo.enterText( (EditText) solo.getView(R.id.newExperimentLocationOnExperimentDescriptionEditText), "checkClickPublish()");
        solo.sleep(1000);
        solo.clickOnRadioButton(2);
        solo.sleep(1000);
        solo.clickOnView(rule.getActivity().findViewById(R.id.newExperimentLocationToggleSwitch));
        solo.sleep(1000);
        solo.clickOnView(rule.getActivity().findViewById(R.id.newExperimentLocationPublishToggleSwitch));
        solo.sleep(2000);
        solo.clickOnButton("Create");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Class", MainActivity.class);

        solo.sleep(3000);

        assertFalse(solo.searchText("checkClickPublish()"));

        solo.clickOnScreen(444, 1925); //"Favorite" Menu Item

        solo.sleep(1000);

        ListView experimentList = rule.getActivity().findViewById(R.id.followedExpListView);
        Experiment experiment = (Experiment) experimentList.getItemAtPosition(0);

        solo.sleep(2000);

        solo.clickInList(0);

        assertEquals(solo.getView(R.id.Publish_text).getVisibility(), (View.VISIBLE));
        assertEquals(solo.getView(R.id.endExp).getVisibility(), (View.VISIBLE));

        assertFalse(solo.isCheckBoxChecked(0));
        solo.sleep(1000);
        solo.clickOnScreen(650, 830);
        solo.sleep(1000);
        solo.goBack();
        solo.sleep(1000);
        solo.clickOnScreen(73, 1920); //"Home" Menu Item
        solo.sleep(1000);
        assertTrue(solo.searchText("checkClickPublish()"));

    }

    // check if the experimenter is able to see the publish and end experiment fields
    // pre-req: there must be one experiment on the homePage Fragment that is not made by the current user
    @Test
    public void checkExpInfoForExperimenter(){
        solo.assertCurrentActivity("Wrong Class", MainActivity.class);

        solo.clickInList(0);
        solo.sleep(2000);
        solo.assertCurrentActivity("Right Activity", ExperimentDetails.class);
        solo.sleep(1000);

        assertEquals(solo.getView(R.id.Publish_text).getVisibility(), (View.INVISIBLE));
        assertEquals(solo.getView(R.id.endExp).getVisibility(), (View.INVISIBLE));

    }

}
