package com.example.codecatchersapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * This class contains test cases for the ScanErrorActivity class.
 */
@RunWith(AndroidJUnit4.class)
public class ScanErrorActivityTest {

    private ActivityScenario<ScanErrorActivity> scenario;
    private Button retryButton;
    private Button quitToMenuButton;

    /**
     * Set up the test environment before each test case.
     * @throws Exception if an error occurs during setup.
     */
    @Before
    public void setUp() throws Exception {
        scenario = ActivityScenario.launch(ScanErrorActivity.class);
        scenario.onActivity(activity -> {
            retryButton = activity.findViewById(R.id.retry_button);
            quitToMenuButton = activity.findViewById(R.id.quit_to_menu_button);
        });
    }
    /**
     * Test that the retry button is clickable.
     */
    @Test
    public void testRetryButtonIsClickable() {
        assertThat(retryButton, notNullValue());
        assertThat(retryButton.isClickable(), equalTo(true));
    }

    /**
     * Test that the quit to menu button is clickable.
     */
    @Test
    public void testQuitToMenuButtonIsClickable() {
        assertThat(quitToMenuButton, notNullValue());
        assertThat(quitToMenuButton.isClickable(), equalTo(true));
    }
}
