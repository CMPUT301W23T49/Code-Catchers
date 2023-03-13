package com.example.codecatchersapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static java.util.regex.Pattern.matches;

//import androidx.test.espresso.contrib.RecyclerViewActions;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SearchUsersActivityTest {

    @Rule
    public ActivityTestRule<SearchUsersActivity> mActivityRule =
            new ActivityTestRule<>(SearchUsersActivity.class);

    private SearchUsersActivity mActivity;
    private androidx.appcompat.widget.SearchView searchView;

    @Before
    public void setUp() {
        Intents.init();
        mActivity = mActivityRule.getActivity();
        searchView = mActivity.findViewById(R.id.search_view);
    }

    @Test
    public void testRecyclerViewAdapter() {
        // Check that the RecyclerView has been set up correctly
        RecyclerView recyclerView = mActivity.findViewById(R.id.rv_users);
        assertNotNull(recyclerView.getAdapter());
        assertTrue(recyclerView.getAdapter() instanceof UserAdapter);
    }

    @Test
    public void testSearchView() {
        assertNotNull(searchView);
        assertTrue(searchView.isFocusable());

        // Type a query into the searchView
        onView(withId(R.id.search_view)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("CoolUser537"), pressKey(KeyEvent.KEYCODE_ENTER));

        // Check that the RecyclerView has been updated with the search results
        RecyclerView recyclerView = mActivity.findViewById(R.id.rv_users);
        assertNotNull(recyclerView.getAdapter());
        assertTrue(recyclerView.getAdapter() instanceof UserAdapter);
        assertTrue(recyclerView.getAdapter().getItemCount() > 0);
    }

    @Test
    public void testBackButton() {
        // Check that clicking the back button returns to the SocialMenuActivity
        onView(withId(R.id.back_button)).perform(click());
        intended(hasComponent(SocialMenuActivity.class.getName()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }
}
