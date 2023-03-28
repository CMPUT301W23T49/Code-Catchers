package com.example.codecatchersapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.codecatchersapp.ScannerActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ScannerActivityTest {

    @Rule
    public ActivityScenarioRule<ScannerActivity> activityScenarioRule =
            new ActivityScenarioRule<>(ScannerActivity.class);

    @Test
    public void checkError() {
        ActivityScenario<ScannerActivity> activityScenario = activityScenarioRule.getScenario();

        // Check if the scanner view is displayed
        onView(withId(R.id.scanner_view)).check(matches(isDisplayed()));
    }
}