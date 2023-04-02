package com.example.codecatchersapp;

import static org.junit.Assert.*;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class contains test cases for the ScannerActivity class.
 */
@RunWith(AndroidJUnit4.class)
public class ScannerActivityTest {
    /**
     * Rule for granting camera permission during tests.
     */
    @Rule
    public GrantPermissionRule cameraPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.CAMERA);

    /**
     * Set up the test environment before each test case.
     */
    @Before
    public void setUp() {
        // no need
    }

    /**
     * Test that the app has camera permission.
     */
    @Test
    public void testCameraPermission() {
        // Check that the app has camera permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int cameraPermission = ApplicationProvider.getApplicationContext()
                    .checkSelfPermission(Manifest.permission.CAMERA);
            assertEquals(PackageManager.PERMISSION_GRANTED, cameraPermission);
        }
    }

    /**
     * Test that the scanner view is not null.
     */
    @Test
    public void testScannerView() {
        // Test that the scanner view is not null
        ActivityScenario<ScannerActivity> scenario = ActivityScenario.launch(ScannerActivity.class);
        scenario.onActivity(activity -> assertNotNull(activity.findViewById(R.id.scanner_view)));
    }
}
