package com.example.codecatchersapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Intent;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ScoreRevealActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo= new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start(){
        Activity activity = rule.getActivity();}

    @Test
    public void testScoreIsDisplayed() {
        Intent intent = new Intent();
        intent.putExtra("contents", "BFG5DGW54");
        rule.launchActivity(intent);

        onView(withId(R.id.scoreTextView))
                .check(matches(isDisplayed()))
                .check(matches(withText("19")));
    }

    @Test
    public void testActivityRuns() {
        Intent intent = new Intent();
        intent.putExtra("contents", "BFG5DGW54");
        rule.launchActivity(intent);

        onView(withId(R.id.root_layout)).check(matches(isDisplayed()));
    }

}