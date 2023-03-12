package com.example.codecatchersapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ScoreRevealActivity extends Activity {

    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_reveal_activity);

        // Get score from intent
        Intent intent = getIntent();
        String qr_contents = intent.getStringExtra("qrContents");

        Score score;
        try {
            score = new Score(qr_contents);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        int calculated_score = score.getScore_();

        // Find score value and update it
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(calculated_score);

        // Find monster image and set it's image resource
        ImageView monsterImageView = findViewById(R.id.monsterImageView);
        //monsterImageView.setImageResource(R.drawable.monster_image);
    }

}
