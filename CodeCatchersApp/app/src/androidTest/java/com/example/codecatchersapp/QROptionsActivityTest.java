package com.example.codecatchersapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.common.returnsreceiver.qual.This;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class QROptionsActivityTest extends JUnitCore {
    private ActivityScenario<QROptionsActivity> activityScenario;

    @Before
    public void setUp() {
        activityScenario = ActivityScenario.launch(QROptionsActivity.class);
    }

    @Test
    public void testClickOnContinueButton() {
        onView(withId(R.id.continue_photo_button)).perform(click());
        activityScenario.onActivity(activity -> {
            // Verify that the onClick() method is triggered
            // Add assertions here
        });
    }

    /**
     *
     */
    @Test
    public void testSaveComment() {
        String comment = "QROptionsActivityTest test comment";
        onView(withId(R.id.editTextNewMonComment)).perform(typeText(comment), closeSoftKeyboard());
        onView(withId(R.id.continue_photo_button)).perform(click());
        activityScenario.onActivity(activity -> {
            // Verify that the comment was added to the database
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference collectionReference = db.collection("PlayerDB/someUserID1/Monsters/someMonsterID/comments");
            Query query = collectionReference.whereEqualTo("userName", "myUser").whereEqualTo("commentText", comment);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    assertEquals(1, querySnapshot.size());
                }
            });
        });
    }


/*
    @Test
    public void testSaveGeolocation() {
        // Create a mock FirebaseFirestore instance
        FirebaseFirestore mockDb = FirebaseFirestore.getInstance()

        // Create a mock CollectionReference for the geolocationData collection
        CollectionReference mockGeoLocationCollection = mock(CollectionReference.class);
        when(mockDb.collection("PlayerDB/someUserID1/Monsters/someMonsterID/geolocationData")).thenReturn(mockGeoLocationCollection);

        // Create a mock DocumentReference for the Location Data document
        DocumentReference mockLocationDataDocument = mock(DocumentReference.class);
        when(mockGeoLocationCollection.document("Location Data")).thenReturn(mockLocationDataDocument);

        // Create a Map object with latitude and longitude values
        Map<String, Object> mockCoordinates = new HashMap<>();
        mockCoordinates.put("Latitude", 10.0);
        mockCoordinates.put("Longitude", 20.0);

        // Create a mock OnSuccessListener for the update() method
        OnSuccessListener<Void> mockSuccessListener = mock(OnSuccessListener.class);

        // Call the saveGeolocation() function with the mock values
        QROptionsActivity qroptionsActivity = new QROptionsActivity();
        qroptionsActivity.db = mockDb;
        qroptionsActivity.finalLatitude = 10.0;
        qroptionsActivity.finalLongitude = 20.0;
        qroptionsActivity.saveGeolocation();

        // Verify that the update() method was called with the correct values and success listener
        verify(mockLocationDataDocument).update(eq(mockCoordinates), eq(mockSuccessListener));
    }*/

    @Test
    public void testGoMainMenu() {
        onView(withId(R.id.continue_photo_button)).perform(click());
        intended(hasComponent(MainMenuActivity.class.getName()));
    }

    @After
    public void tearDown() {
        activityScenario.close();
    }
}
