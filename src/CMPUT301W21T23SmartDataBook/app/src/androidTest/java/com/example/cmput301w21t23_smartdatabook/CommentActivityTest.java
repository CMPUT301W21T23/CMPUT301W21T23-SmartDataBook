package com.example.cmput301w21t23_smartdatabook;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.cmput301w21t23_smartdatabook.comments.Comment;
import com.example.cmput301w21t23_smartdatabook.comments.CommentActivity;
import com.example.cmput301w21t23_smartdatabook.experiment.Experiment;
import com.example.cmput301w21t23_smartdatabook.experiment.ExperimentDetails;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.example.cmput301w21t23_smartdatabook.trials.UploadTrial;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CommentActivityTest {

    private Solo solo;
    private View addExpButton;
    private View addCommentButton;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        addExpButton = rule.getActivity().findViewById(R.id.add_experiment_button);
        addCommentButton = rule.getActivity().findViewById(R.id.add_comment_button);

    }

    //Finally, add tearDown() method using the @After tag to run after every test method.
    //This method closes the activity after each test.
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

    @Test
    public void testAddComment() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        createExperiment();
        solo.goBack();
        solo.clickOnText("Comments");
        solo.assertCurrentActivity("Wrong Activity", CommentActivity.class);
        solo.clickOnScreen(980, 2053);
        solo.enterText( solo.getEditText("Comment Text"), "Hello!");
        solo.clickOnText("Add Comment");
        solo.waitForActivity("CommentActivity",1000);

        ListView commentList = solo.getCurrentActivity().findViewById(R.id.comment_list);
        Comment comment = (Comment) commentList.getItemAtPosition(0);
        assertEquals(comment.getText(), "Hello!");
    }

    //Create a working experiment
    public void createExperiment() {
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

        solo.enterText( (EditText) solo.getView(R.id.newExperimentLocationOnExperimentNameEditText), "Binomial");
        solo.sleep(1000);
        solo.enterText( (EditText) solo.getView(R.id.newExperimentLocationOnExperimentDescriptionEditText), "Coin Flip");
        solo.sleep(1000);
        solo.clickOnRadioButton(0);
        solo.sleep(1000);
        solo.clickOnView(rule.getActivity().findViewById(R.id.newExperimentLocationToggleSwitch));
        solo.sleep(1000);
        solo.clickOnButton("Create");
        solo.sleep(1000);

    }
}
