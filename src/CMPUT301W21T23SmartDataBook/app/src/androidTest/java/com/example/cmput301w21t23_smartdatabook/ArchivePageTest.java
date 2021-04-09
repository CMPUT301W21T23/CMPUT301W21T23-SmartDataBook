package com.example.cmput301w21t23_smartdatabook;

import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.cmput301w21t23_smartdatabook.experiment.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
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
        solo.assertCurrentActivity("Wrong Class", MainActivity.class);
        solo.waitForFragmentById(R.layout.home_page, 1000);
        solo.clickOnScreen(974, 1750);
        solo.waitForFragmentById(R.layout.add_experiment, 1000);

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
        solo.enterText( (EditText) solo.getView(R.id.descriptionField), "Coin Flip");
        solo.sleep(1000);
        solo.clickOnRadioButton(0);
        solo.sleep(1000);
        solo.clickOnView(rule.getActivity().findViewById(R.id.newExperimentLocationToggleSwitch));
        solo.sleep(1000);
        solo.clickOnButton("Create");
        solo.sleep(1000);


        solo.clickOnText("Binomial");
        solo.assertCurrentActivity("Wrong Class", ExperimentDetails.class);
        solo.clickOnText("Archive");
        solo.clickOnText("Archive Experiment");
        solo.sleep(1000);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnScreen(681,1986);
        Assert.assertTrue(solo.searchText("Binomial"));
        solo.clickOnText("Binomial");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Class", ExperimentDetails.class);
        solo.sleep(1000);
        solo.clickOnText("Un-Archive");
        solo.sleep(1000);
        solo.clickOnText("Un-Archive Experiment");
        solo.goBack();
        solo.clickOnScreen(418, 1986);
        solo.sleep(1000);
        solo.clickOnScreen(681,1986);
        solo.sleep(1000);
        Assert.assertFalse(solo.searchText("Binomial"));

    }
}
