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

public class MainMenuActivityTest extends JUnitCore {

    @Rule
    public ActivityTestRule<MainMenuActivity> rule = new ActivityTestRule<>(MainMenuActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @Test
    public void checkButtons() {
        // Test scan QR code
        onView(withId(R.id.scan_qr_button)).perform(click());
        intended(hasComponent(ScoreRevealActivity.class.getName()));
        pressBack();

        // Test social menu button
        onView(withId(R.id.social_button)).perform(click());
        intended(hasComponent(SocialMenuActivity.class.getName()));
        pressBack();

        // Test map button
        onView(withId(R.id.map_button)).perform(click());
        intended(hasComponent(MapActivity.class.getName()));
        pressBack();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

}
