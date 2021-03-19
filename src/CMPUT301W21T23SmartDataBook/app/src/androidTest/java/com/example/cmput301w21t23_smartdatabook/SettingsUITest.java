package com.example.cmput301w21t23_smartdatabook;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertEquals;

public class SettingsUITest {

    private Solo solo;
    private View addExpButton;
    private View searchBar;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        addExpButton = rule.getActivity().findViewById(R.id.add_experiment_button);
        searchBar = rule.getActivity().findViewById((R.id.app_bar_search));
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkNameEditField(){
        final String usernameDefault = "Username";
        final String emailDefault = "Email";
        final String newUsername = "Krutik";
        final String newEmail = "sonikrutik0@gmail.com";

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnScreen(780, 1943); //"Settings" Menu Item

        final EditText usernameTextView = solo.getCurrentActivity().findViewById(R.id.usernameTextField);
        final EditText emailTextView = solo.getCurrentActivity().findViewById(R.id.emailTextField);
        assertEquals(usernameTextView.getText().toString(), usernameDefault);
        assertEquals(emailDefault, emailTextView.getText().toString());

        solo.enterText((EditText) usernameTextView, newUsername);
        solo.enterText((EditText) emailTextView, newEmail);

        assertEquals(usernameDefault, usernameTextView.getText().toString());
        assertEquals(emailDefault, emailTextView.getText().toString());
    }


    //Finally, add tearDown() method using the @After tag to run after every test method.
    //This method closes the activity after each test.
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

    @Test
    public void checkInputs() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(1000);
        solo.clickOnScreen(780, 1943); //"Settings" Menu Item
        solo.clickInList(0);
        //insert fragment check here
    }
}
