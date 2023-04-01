package com.example.codecatchersapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
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
    private MonsterNameGenerator monsterNameGenerator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_reveal_activity);

        // Find the monster name and update it
        monsterNameGenerator = new MonsterNameGenerator();

        // Get hash from intent
        Intent intent = getIntent();
        String hash = intent.getStringExtra("hash");
        String binaryHash = intent.getStringExtra("binaryHash");
        System.out.println("binaryHashAGAIN: " + binaryHash);

        String displayScore;
        try {
            Score score = new Score(hash); // New score object that calculates score based on contents delivered from QR
            displayScore = score.getScore();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


        RelativeLayout rootLayout = findViewById(R.id.root_layout);
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the next activity here
                Intent intent = new Intent(ScoreRevealActivity.this, QROptionsActivity.class);
                startActivity(intent);
            }
        });

        // Find score value and update it
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(displayScore);

        // Set the binaryHash value in the MonsterView
        MonsterView viewMonster = findViewById(R.id.MonsterView);
        if (viewMonster != null) {
            viewMonster.setBinaryHash(binaryHash);
        }


        // Find the monster name TextView and update it
        TextView monsterNameTextView = findViewById(R.id.monsterNameTextView);
        String monsterName = monsterNameGenerator.generateName(binaryHash);
        monsterNameTextView.setText(monsterName);



    }}
