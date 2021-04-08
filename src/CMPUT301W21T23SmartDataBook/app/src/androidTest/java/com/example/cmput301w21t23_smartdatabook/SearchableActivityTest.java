package com.example.cmput301w21t23_smartdatabook;

import androidx.appcompat.widget.SearchView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.example.cmput301w21t23_smartdatabook.experiment.Experiment;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SearchableActivityTest {

    private Solo solo;
    private MainActivity mainActivity;

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
    public void testSearch() {

//        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

//        createExperiment("Binomial", "Coin Flip", 0);
//        createExperiment("Count", "Traffic Car Count", 1);
//        createExperiment("Non-Negative Count", "Broken Eggs in carton", 2);
//        createExperiment("Measurement", "Car Temperature", 3);
        solo.clickOnScreen(863, 124);
//        solo.enterText( (EditText) , "Count");
        solo.sleep(2000);

        ListView experimentList = rule.getActivity().findViewById(R.id.experiment_list);
        Experiment experiment = (Experiment) experimentList.getItemAtPosition(2);

        //go to experiment details for Non-Negative Count
//        solo.clickInList(2);
//        assertTrue("Non-Negative Count".equals(experiment.getExpName()));

    }

    //Create a working experiment
    public void createExperiment(String expName, String expType, int buttonIndex) {
        solo.assertCurrentActivity("Wrong Class", MainActivity.class);
        solo.waitForFragmentById(R.layout.home_page, 1000);
        solo.clickOnScreen(974, 1750);
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
        rule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NumberPicker maxPicker = rule.getActivity().findViewById(R.id.maxTrialsNumberPicker);
                maxPicker.setValue(30);
            }
        });

        solo.enterText( (EditText) solo.getView(R.id.newExperimentLocationOnExperimentNameEditText), expName);
        solo.enterText( (EditText) solo.getView(R.id.newExperimentLocationOnExperimentDescriptionEditText), expType);
        solo.clickOnRadioButton(buttonIndex);
        solo.clickOnView(rule.getActivity().findViewById(R.id.newExperimentLocationToggleSwitch));
        solo.clickOnButton("Create");

    }
}
