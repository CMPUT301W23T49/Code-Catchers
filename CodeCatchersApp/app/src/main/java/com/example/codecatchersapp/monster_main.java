package com.example.codecatchersapp;

import android.graphics.Paint;
import android.os.Bundle;
import com.example.codecatchersapp.R;

import androidx.appcompat.app.AppCompatActivity;



public class monster_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster);

        // Find the MonsterView and generate a monster
        MonsterView viewMonster = findViewById(R.id.MonsterView);
        monsterFeatures monster = new monsterFeatures(this, viewMonster.getCanvas(), new Paint());

    }
}
