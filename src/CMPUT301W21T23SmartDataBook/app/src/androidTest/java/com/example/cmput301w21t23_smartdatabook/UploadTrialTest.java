package com.example.cmput301w21t23_smartdatabook;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UploadTrialTest {
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
    public void uploadTrialTest() {
        // run test on empty database
        createExperiment();
        solo.clickOnText("Binomial");
        solo.assertCurrentActivity("Wrong Class", ExperimentDetails.class);
        solo.clickOnButton("UPLOAD TRIALS");
        solo.assertCurrentActivity("Wrong Class", UploadTrial.class);
        solo.clickOnButton("add new trials");
        solo.enterText(0, "2");
        solo.clickOnButton("Add passes");
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Class", UploadTrial.class);
        solo.sleep(3000);
        ListView listView = (ListView) solo.getView(R.id.uploaded_trials);
        solo.sleep(3000);
        assertEquals(listView.getAdapter().getCount(), 2);
    }

    @Test
    public void addTooManyTrials() {
        createExperiment();
        solo.clickOnText("Binomial");
        solo.assertCurrentActivity("Wrong Class", ExperimentDetails.class);
        solo.clickOnButton("UPLOAD TRIALS");
        solo.assertCurrentActivity("Wrong Class", UploadTrial.class);
        solo.clickOnButton("add new trials");
        solo.enterText(0, "35");
        solo.clickOnButton("Add passes");
        assertFalse(solo.searchText("You cannot add more trials than the maximum trials for this experiment at once"));
    }

    @Test
    public void deleteTrialExperimenterTest() {
        solo.clickOnText("Binomial");
        solo.assertCurrentActivity("Wrong Class", ExperimentDetails.class);
        solo.clickOnButton("UPLOAD TRIALS");
        solo.assertCurrentActivity("Wrong Class", UploadTrial.class);
        solo.clickInList(0);
        solo.clickOnText("You don't have the privilege to delete trials");
    }

    @Test
    public void deleteTrialOwnerTest() {
        solo.assertCurrentActivity("Wrong Class", MainActivity.class);
        solo.waitForFragmentById(R.layout.home_page, 1000);
        solo.clickOnView(addExpButton);
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
        solo.sleep(1000);

        solo.clickOnText("Name");
        solo.assertCurrentActivity("Wrong Class", ExperimentDetails.class);
        solo.clickOnButton("UPLOAD TRIALS");
        solo.assertCurrentActivity("Wrong Class", UploadTrial.class);
        solo.clickOnButton("add new trials");
        solo.enterText(0, "2");
        solo.clickOnButton("Add Trials");
        Assert.assertTrue(solo.searchText("2"));
        solo.clickInList(0);
        solo.clickOnText("Delete");
        Assert.assertFalse(solo.searchText("2"));
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

    }
}
