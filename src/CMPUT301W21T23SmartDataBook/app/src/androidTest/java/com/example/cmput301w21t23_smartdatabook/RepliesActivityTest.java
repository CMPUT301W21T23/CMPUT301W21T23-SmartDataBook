package com.example.cmput301w21t23_smartdatabook;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.cmput301w21t23_smartdatabook.comments.Comment;
import com.example.cmput301w21t23_smartdatabook.comments.CommentActivity;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RepliesActivityTest {

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
    public void testAddReplies() {
        createExperiment();
        addComment();
        solo.clickInList(0);
        solo.clickOnScreen(948, 1909);
        solo.enterText(solo.getEditText("Comment Text"), "World!");
        solo.clickOnText("Add Comment");
        solo.sleep(2000);

        ListView replyList = solo.getCurrentActivity().findViewById(R.id.replies_list);
        Comment comment = (Comment) replyList.getItemAtPosition(0);
        assertEquals(comment.getText(), "World!");

    }

    //Add a comment to an experiment
    public void addComment() {
        solo.clickOnText("Comments");
        solo.assertCurrentActivity("Wrong Activity", CommentActivity.class);
        solo.clickOnScreen(980, 1930);
        solo.enterText(solo.getEditText("Comment Text"), "Hello!");
        solo.clickOnText("Add Comment");
        solo.sleep(2000);

        ListView commentList = solo.getCurrentActivity().findViewById(R.id.comment_list);
        Comment comment = (Comment) commentList.getItemAtPosition(0);
        assertEquals(comment.getText(), "Hello!");
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
