package com.example.cmput301w21t23_smartdatabook;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AddExpFragIntentTest {

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

    /**
     * Test for proper response for empty name and description field
     */
    @Test
    public void checkInvalidText() {
        solo.assertCurrentActivity("Wrong Class", MainActivity.class);
        solo.waitForFragmentById(R.layout.home_page, 1000);
        solo.clickOnView(addExpButton);
        solo.waitForFragmentById(R.layout.new_experiment_location_on, 1000);
        solo.clickOnButton("Create");
        solo.sleep(1000);

        assertFalse(solo.waitForFragmentById(R.layout.home_page, 1000));
    }

    /**
     * Test when the selected min and max trials values are equal to each other
     */
    @Test
    public void checkEqualMinMax() {
        solo.assertCurrentActivity("Wrong Class", MainActivity.class);
        solo.waitForFragmentById(R.layout.home_page, 1000);
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
        rule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NumberPicker maxPicker = rule.getActivity().findViewById(R.id.maxTrialsNumberPicker);
                maxPicker.setValue(10);
            }
        });

        solo.enterText( (EditText) solo.getView(R.id.newExperimentLocationOnExperimentNameEditText), "Name");
        solo.enterText( (EditText) solo.getView(R.id.newExperimentLocationOnExperimentDescriptionEditText), "Description");
        solo.clickOnRadioButton(0);
        solo.clickOnButton("Create");

        assertFalse(solo.waitForFragmentById(R.layout.home_page, 1000));

    }//checkEqualMinMax

    /**
     * Test if a new experiment can be created
     */
    @Test
    public void checkValidCreation() {
        solo.assertCurrentActivity("Wrong Class", MainActivity.class);
        solo.waitForFragmentById(R.layout.home_page, 1000);
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

        solo.enterText( (EditText) solo.getView(R.id.newExperimentLocationOnExperimentNameEditText), "Name");
        solo.sleep(1000);
        solo.enterText( (EditText) solo.getView(R.id.newExperimentLocationOnExperimentDescriptionEditText), "Description");
        solo.sleep(1000);
        solo.clickOnRadioButton(2);
        solo.sleep(1000);
        solo.clickOnView(rule.getActivity().findViewById(R.id.newExperimentLocationToggleSwitch));
        solo.sleep(1000);
        solo.clickOnButton("Create");
        solo.sleep(1000);

        ListView experimentList = rule.getActivity().findViewById(R.id.experiment_list);
        Experiment experiment = (Experiment) experimentList.getItemAtPosition(0);
        assertEquals(experiment.getExpName(), "Name");

    }

}
