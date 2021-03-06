package com.example.cmput301w21t23_smartdatabook;

import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.cmput301w21t23_smartdatabook.experiment.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.example.cmput301w21t23_smartdatabook.trials.UploadTrial;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class StatsViewTest {
    private Solo solo;
    private View addExpButton;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        addExpButton = rule.getActivity().findViewById(R.id.add_experiment_button);
    }

    //Finally, add tearDown() method using the @After tag to run after every test method.
    //This method closes the activity after each test.
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

    @Test
    public void testStatsView() {

        createExperiment();
        addTrials();
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickInList(3);
        solo.assertCurrentActivity("Wrong Activity", ExperimentDetails.class);
        solo.clickOnButton("VIEW STATS");

        assertTrue(Float.parseFloat(((TextView) solo.getView(R.id.meanTextView)).getText().toString().replace("Mean (Pass %): ", "")) > 0);
        assertTrue(Float.parseFloat(((TextView) solo.getView(R.id.medianTextView)).getText().toString().replace("Median: ", "")) > 0);
        assertTrue(Float.parseFloat(((TextView) solo.getView(R.id.stdDeviationTextView)).getText().toString().replace("Std: ", "")) > 0);

        //Binomial will give either a 0.0 or 1.0 for quartile value
        assertTrue(Float.parseFloat(((TextView) solo.getView(R.id.quartile1TextView)).getText().toString().replace("Quartile 1: ", "")) >= 0);
    }

    //Adds 8 test trials to the experiment
    public void addTrials() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", ExperimentDetails.class);
        solo.clickOnButton("UPLOAD TRIALS");
        solo.assertCurrentActivity("Wrong Activity", UploadTrial.class);
        solo.clickOnButton("add new trials");
        solo.enterText((EditText) solo.getEditText("Enter positive number of passes/failures"), "5");
        solo.clickOnText("Add passes");
        solo.clickOnButton("add new trials");
        solo.enterText((EditText) solo.getEditText("Enter positive number of passes/failures"), "3");
        solo.clickOnText("Add failure");
    }

    //Create a working experiment
    public void createExperiment() {
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

        solo.enterText((EditText) solo.getView(R.id.newExperimentLocationOnExperimentNameEditText), "Binomial");
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.description), "Coin Flip");
        solo.sleep(1000);
        solo.clickOnRadioButton(0);
        solo.sleep(1000);
        solo.clickOnView(rule.getActivity().findViewById(R.id.newExperimentLocationToggleSwitch));
        solo.sleep(1000);
        solo.clickOnButton("Create");
        solo.sleep(1000);

    }

}
