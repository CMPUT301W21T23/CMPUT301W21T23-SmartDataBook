package com.example.cmput301w21t23_smartdatabook;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkNameEditField() {
        final String usernameDefault = "Enter Username";
        final String emailDefault = "Enter Email";
        final String correctUsername = "Krutik";
        final String correctEmail = "sonikrutik0@gmail.com";


//        emailTextView = (TextInputLayout) solo.getView(R.id.emailTextField);

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnScreen(780, 1943); //"Settings" Menu Item
        solo.sleep(1000);

        assertEquals(((EditText) solo.getView(R.id.usernameTextField)).getText().toString(), usernameDefault);
        assertEquals(((EditText) solo.getView(R.id.emailTextField)).getText().toString(), emailDefault);

        solo.sleep(2000);

        solo.clearEditText((EditText) solo.getView(R.id.usernameTextField));
        solo.clearEditText((EditText) solo.getView(R.id.emailTextField));

        solo.enterText((EditText) solo.getView(R.id.usernameTextField), correctUsername);
        solo.enterText((EditText) solo.getView(R.id.emailTextField), correctEmail);

        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.saveButtonView));
        solo.sleep(2000);

        solo.clickOnScreen(444, 1925); //"Favorite" Menu Item
        solo.clickOnScreen(780, 1943); //"Settings" Menu Item
        solo.sleep(2000);

        String enteredName = ((EditText) solo.getView(R.id.usernameTextField)).getText().toString();
        String enteredEmail = ((EditText) solo.getView(R.id.emailTextField)).getText().toString();

        assertEquals(enteredName, correctUsername);
        assertEquals(enteredEmail, correctEmail);

        solo.sleep(2000);
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
