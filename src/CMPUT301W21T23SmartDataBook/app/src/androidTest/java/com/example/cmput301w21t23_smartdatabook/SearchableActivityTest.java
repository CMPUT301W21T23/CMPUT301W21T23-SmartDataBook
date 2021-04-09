package com.example.cmput301w21t23_smartdatabook;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

import androidx.appcompat.widget.SearchView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

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
//        createExperiment("Count", "Traffic Car Count", 1);

        SearchView searchView = (SearchView) solo.getView(R.id.app_bar_search);
        solo.clickOnView(searchView);
        solo.sleep(2000);
        int id = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        solo.sleep(1000);
        EditText editText = (EditText) searchView.findViewById(id);
        solo.sleep(1000);
        solo.enterText(editText, "Count");

        createExperiment("Binomial", "Coin Flip", 0);
        createExperiment("Count", "Traffic Car Count", 1);
        createExperiment("Non-Negative Count", "Broken Eggs in carton", 2);
        createExperiment("Measurement", "Car Temperature", 3);

        ListView experimentList = rule.getActivity().findViewById(R.id.experiment_list);
        solo.sleep(1000);
        solo.clickOnScreen(863, 124);
        //search by description
        solo.enterText((EditText) solo.getView(androidx.appcompat.R.id.search_src_text), "Broken Eggs in carton");
        solo.clickOnScreen(628, 274);
        assertTrue(solo.searchText("Broken Eggs in carton"));
        solo.goBack();

        solo.clickOnScreen(863, 124);
        //Search by experiment name
        solo.enterText((EditText) solo.getView(androidx.appcompat.R.id.search_src_text), "Binomial");
        solo.clickOnScreen(628, 274);
        assertTrue(solo.searchText("Binomial"));
        solo.sleep(2000);
    }

    //Create a working experiment
    public void createExperiment(String expName, String expType, int buttonIndex) {
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
