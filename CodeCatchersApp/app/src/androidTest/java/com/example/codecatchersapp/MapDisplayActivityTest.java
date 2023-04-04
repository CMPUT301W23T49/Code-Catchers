/**
 * Instrumentation tests for MapDisplayActivity class.
 */
package com.example.codecatchersapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MapDisplayActivityTest {

    private MapDisplayActivity mActivity;

    @Rule
    public ActivityTestRule<MapDisplayActivity> mActivityRule = new ActivityTestRule<>(MapDisplayActivity.class);

    /**
     * Sets up the test environment before executing the test methods.
     * @throws Exception if any error occurs during the setup
     */
    @Before
    public void setUp() throws Exception {
        mActivity = mActivityRule.getActivity();
    }

    /**
     * Tests if the map is displayed on the screen.
     */
    @Test
    public void testMapIsDisplayed() {
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    /**
     * Tests if the search button is clickable.
     */
    @Test
    public void testSearchButtonIsClickable() {
        onView(withId(R.id.search_button)).perform(click());
    }

    /**
     * Cleans up the test environment after executing the test methods.
     * @throws Exception if any error occurs during the setup
     */
    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}