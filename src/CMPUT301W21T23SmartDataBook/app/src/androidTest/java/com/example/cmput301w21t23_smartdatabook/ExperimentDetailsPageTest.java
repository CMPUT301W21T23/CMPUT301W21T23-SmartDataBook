package com.example.cmput301w21t23_smartdatabook;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

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

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

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

    @Test
    public void checkExpInfo(){
        ListView experimentList = rule.getActivity().findViewById(R.id.experiment_list);
        Experiment experiment = (Experiment) experimentList.getItemAtPosition(0);

        solo.assertCurrentActivity("Wrong Acitvity", MainActivity.class);
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", ExperimentDetails.class);
        solo.asserTrue(solo.searchText(experiment.getDate()));
//        solo.assertFalse(solo.getView(R.id.ClickedExpdate));

    }
}
