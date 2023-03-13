package com.example.codecatchersapp;
import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.widget.EditText;
import android.widget.ListView;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ScannerActivityTest {

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
