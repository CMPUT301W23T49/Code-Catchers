package com.example.codecatchersapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ScoreRevealActivity extends Activity {

    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_reveal_activity);

        // Get score from intent
        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);

        // Find score value and update it
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(score);

        // Find monster image and set it's image resource
        ImageView monsterImageView = findViewById(R.id.monsterImageView);
        //monsterImageView.setImageResource(R.drawable.monster_image);
    }


}
