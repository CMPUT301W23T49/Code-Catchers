package com.example.codecatchersapp;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MapActivityTest {

    @Rule
    public ActivityTestRule<ScannerActivity> rule =
            new ActivityTestRule<>(ScannerActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        // No need for Solo initialization here
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }
}
