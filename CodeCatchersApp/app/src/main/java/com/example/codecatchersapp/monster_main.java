/**
 The monster_main class is an activity that displays a monster using the MonsterView class.
 It generates a new monster using the monsterFeatures class.
 @author [Josie Matalski]
 @version 1.0
 @since [Sunday March 4 2021]
 */
package com.example.codecatchersapp;

import android.graphics.Paint;
import android.os.Bundle;
import com.example.codecatchersapp.R;

import androidx.appcompat.app.AppCompatActivity;



public class monster_main extends AppCompatActivity {

    /**
     * Called when the activity is starting. Sets the content view to the activity_monster.xml layout, finds the MonsterView,
     * and generates a new monster using the monsterFeatures class.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster);

        // Find the MonsterView and generate a monster
        MonsterView viewMonster = findViewById(R.id.MonsterView);
        monsterFeatures monster = new monsterFeatures(this, viewMonster.getCanvas(), new Paint());

    }
}
