package com.example.codecatchersapp;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MapActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<ScannerActivity> rule =
            new ActivityTestRule<>(ScannerActivity.class, true, true);

    // *run before all tests

    @Before
    public void seUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    // *gets activity
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();

    }}