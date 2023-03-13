package com.example.codecatchersapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
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

public class SocialMenuActivityTest extends JUnitCore {

    @Rule
    public ActivityTestRule<SocialMenuActivity> rule = new ActivityTestRule<>(SocialMenuActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @Test
    public void checkButtons() {

        // Test browse by users button
        onView(withId(R.id.browse_users_button)).perform(click());
        intended(hasComponent(SearchUsersActivity.class.getName()));
        pressBack();

        // Test leaderboards button
        onView(withId(R.id.leaderboards_button)).perform(click());
        intended(hasComponent(LeaderboardsActivity.class.getName()));
        pressBack();
    }

    @After
    public void tearDown() {
        Intents.release();
    }
}
