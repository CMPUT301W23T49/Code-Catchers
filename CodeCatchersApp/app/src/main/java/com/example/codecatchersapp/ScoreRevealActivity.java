package com.example.codecatchersapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ScoreRevealActivity extends AppCompatActivity {

    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_reveal_activity);

        // Get score from intent
        Intent intent = getIntent();
        String contents = intent.getStringExtra("contents");

        String contentss = "BFG5DGW54"; // use to test if the calculator and display is actually working

        String displayScore;
        try {
            Score score = new Score(contentss); // New score object that calculates score based on contents delivered from QR
            displayScore = score.getScore();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        RelativeLayout rootLayout = findViewById(R.id.root_layout);
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the next activity here
                Intent intent = new Intent(ScoreRevealActivity.this, QrActionsActivity.class);
                startActivity(intent);
            }
        });



        // Find score value and update it
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(displayScore);

        // Find monster image and set it's image resource
        //ImageView monsterImageView = findViewById(R.id.monsterImageView);
        //monsterImageView.setImageResource(R.drawable.monster_image);
    }


}
