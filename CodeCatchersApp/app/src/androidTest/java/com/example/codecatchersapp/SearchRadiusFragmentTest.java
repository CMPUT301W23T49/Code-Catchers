package com.example.codecatchersapp;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**

 This is a JUnit test class for the SearchRadiusFragment that tests if the "Go" and "Cancel" buttons are visible.
 It uses the AndroidJUnit4ClassRunner and ActivityTestRule to launch the MapDisplayActivity and show the SearchRadiusFragment dialog.
 The test then uses Espresso to check if the "Go" and "Cancel" buttons are visible in the dialog.
 */

@RunWith(AndroidJUnit4ClassRunner.class)
public class SearchRadiusFragmentTest {

    @Rule
    public ActivityTestRule<MapDisplayActivity> activityRule = new ActivityTestRule<>(MapDisplayActivity.class);

    @Test
    public void testButtonsVisible() {
        // Open the SearchRadiusFragment dialog
        SearchRadiusFragment fragment = SearchRadiusFragment.newInstance();
        fragment.show(activityRule.getActivity().getSupportFragmentManager(), "SearchRadiusFragment");

        // Check if the "Go" and "Cancel" buttons are visible
        onView(withText("Go")).check(matches(isDisplayed()));
        onView(withText("Cancel")).check(matches(isDisplayed()));
    }

}