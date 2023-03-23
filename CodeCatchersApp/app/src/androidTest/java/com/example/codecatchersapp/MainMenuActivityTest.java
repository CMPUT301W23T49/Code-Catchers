package com.example.codecatchersapp;

import android.Manifest;
import android.app.Activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class MainMenuActivityTest {

    @Rule
    public ActivityTestRule<MainMenuActivity> rule = new ActivityTestRule<>(MainMenuActivity.class);
    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA);


    @Before
    public void setUp() {
        Intents.init();
    }

    @Test
    public void checkScanQRButtons() {
        // Test scan QR code
        onView(withId(R.id.scan_qr_button)).check(matches(isDisplayed())).perform(click());
        intended(hasComponent(ScannerActivity.class.getName()));
        pressBack();
    }

    @Test
    public void checkSocialButtons() {

        // Test social menu button
        checkIdDisplayed(R.id.social_button);

        intended(hasComponent(SocialMenuActivity.class.getName()));

        pressBack();
}
    @Test
    public void checkMapButtons() {

        // Test map button
        checkIdDisplayed(R.id.map_button);
        intended(hasComponent(MapActivity.class.getName()));
        pressBack();
    }
    private void checkIdDisplayed(int id) {
        onView(withId(id)).check(matches(isDisplayed())).perform(click());
    }

    @After
    public void tearDown() {
        Intents.release();
    }

}
