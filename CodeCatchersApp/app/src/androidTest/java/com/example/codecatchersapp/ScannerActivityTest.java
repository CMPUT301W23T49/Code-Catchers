package com.example.codecatchersapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScannerActivityTest extends JUnitCore {

    @Rule
    public ActivityScenarioRule<ScannerActivity> activityScenarioRule =
            new ActivityScenarioRule<>(ScannerActivity.class);

    @Test
    public void checkError() {
        ActivityScenario<ScannerActivity> activityScenario = activityScenarioRule.getScenario();
            onView(withId(R.id.scanner_view)).check(new ViewAssertion() {
                @Override
                public void check(View view, NoMatchingViewException noViewFoundException) {
                    System.out.println("TEST");
                }
            });
    }
}

