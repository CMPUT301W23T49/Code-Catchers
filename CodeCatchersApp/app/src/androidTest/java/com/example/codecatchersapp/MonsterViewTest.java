package com.example.codecatchersapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class MonsterViewTest {

    private Context context;         // Context of the app under test.
    private MonsterView monsterView; // Class under test
    private Canvas canvas;           // Canvas object

    @Before
    public void setUp() {                                       // Set up the test
        context = ApplicationProvider.getApplicationContext();  // Get the context of the app under test.
        monsterView = new MonsterView(context, null);     // Create a new MonsterView object
        canvas = mock(Canvas.class);                            // Create a mock Canvas object
    }

    @Test
    public void testOnDraw() {                                // Test the onDraw method
        monsterView.onDraw(canvas);                           // Call the onDraw method
    }

    @Test
    public void testSetBinaryHash() {                        // Test the setBinaryHash method
        String newBinaryHash = "11010101010";                // Create a new binary hash value
        monsterView.setBinaryHash(newBinaryHash);            // Call the setBinaryHash method
    }
    @Test
    public void testSetMonster() {                                                // Test the setMonster method
        Paint paint = new Paint();
        monsterFeatures newMonster = new monsterFeatures(context, paint);
        monsterView.setMonster(newMonster);                                 // Call the setMonster method
    }
}
