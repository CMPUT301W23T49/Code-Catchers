package com.example.codecatchersapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UserAccountActivityTest extends JUnitCore {

    @Rule
    public ActivityTestRule<UserAccountActivity> rule = new ActivityTestRule<>(UserAccountActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @Test
    public void checkButtons() {

        // Test continue button
        onView(withId(R.id.button)).perform(click());
        intended(hasComponent(MainMenuActivity.class.getName()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }


}

