package com.example.cmput301w21t23_smartdatabook;

import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.cmput301w21t23_smartdatabook.experiment.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.example.cmput301w21t23_smartdatabook.trials.UploadTrial;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ArchivePageTest {
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
    public void checkEndedExperiment(){
        // create experiment
        solo.assertCurrentActivity("Wrong Class", MainActivity.class);
        solo.waitForFragmentById(R.layout.home_page, 3000);
        solo.clickOnScreen(974, 1750);
        solo.waitForFragmentById(R.layout.add_experiment, 2000);

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

        solo.enterText( (EditText) solo.getView(R.id.newExperimentLocationOnExperimentNameEditText), "Binomial");
        solo.sleep(1000);
        solo.enterText( (EditText) solo.getView(R.id.description), "Coin Flip");
        solo.sleep(1000);
        solo.clickOnRadioButton(0);
        solo.sleep(1000);
        solo.clickOnView(rule.getActivity().findViewById(R.id.newExperimentLocationToggleSwitch));
        solo.sleep(1000);
        solo.clickOnButton("Create");
        solo.sleep(1000);

        // archive experiment
        solo.clickOnText("Binomial");
        solo.assertCurrentActivity("Wrong Class", ExperimentDetails.class);
        solo.clickOnText("Archive");
        solo.clickOnText("Archive Experiment");
        solo.sleep(1000);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnScreen(681,1986); // click on archive page

        // assert that experiment exists in the archive page
        Assert.assertTrue(solo.searchText("Binomial"));

        // check that the experiment is not public on the home page
        solo.sleep(2000);
        solo.clickOnScreen(160, 1986); // click on home page
        solo.sleep(2000);
        Assert.assertFalse(solo.searchText("Binomial"));
        solo.sleep(2000);

        // check that one cannot add more trials to this experiment
        solo.clickOnScreen(681,1986); // click on archive page
        solo.clickOnText("Binomial");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Class", ExperimentDetails.class);
        solo.sleep(2000);
        solo.clickOnButton("UPLOAD TRIALS");
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Class", UploadTrial.class);
        solo.sleep(2000);
        Assert.assertFalse(solo.searchText("add new trials"));
        solo.sleep(2000);

        // check that one can un-archive experiment
        solo.goBack();
        solo.sleep(2000);
        solo.clickOnText("Un-Archive");
        solo.sleep(1000);
        solo.clickOnText("Un-Archive Experiment");
        solo.goBack();

        // check if the experiemnt exists in hte home page
        solo.clickOnScreen(160, 1986); // click on home page
        Assert.assertTrue(solo.searchText("Binomial"));
        solo.sleep(1000);

        // check that the experiment is gone from the archive page
        solo.clickOnScreen(681,1986); // click on archive page
        solo.sleep(1000);
        Assert.assertFalse(solo.searchText("Binomial"));

    }
}
