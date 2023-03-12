package com.example.codecatchersapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        // String contents = "BFG5DGW54"; use to test if the calculator and display is actually working

        String displayScore;
        try {
            Score score = new Score(contents); // New score object that calculates score based on contents delivered from QR
            displayScore = score.getScore();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


        // Find score value and update it
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(displayScore);

        // Find monster image and set it's image resource
        //ImageView monsterImageView = findViewById(R.id.monsterImageView);
        //monsterImageView.setImageResource(R.drawable.monster_image);
    }

    public int calculateScore(String qr_contents) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(qr_contents.getBytes());

        // Convert Hash to Hex String
        BigInteger bigInt = new BigInteger(1, hash);
        String hexString = bigInt.toString(16);

        // Find all repeated digits and calculate score based on the proposed scoring method in the notes
        int score = 0;
        int consecutive = 1;

        for(int i = 0; i < hexString.length()-1; i++) {
            char c = hexString.charAt(i);
            if(c == hexString.charAt(i+1)){
                consecutive+=1;
            }
            else{
                if (consecutive > 1){
                    int digitValue = getDigitValue(c);
                    score += Math.pow(20, consecutive-2) * digitValue;
                }
                consecutive = 1;
            }
        }
        return score;
    }

    public static int getDigitValue(char hexDigit) {
        if (hexDigit >= '0' && hexDigit <= '9') {
            return hexDigit - '0';
        } else if (hexDigit >= 'a' && hexDigit <= 'f') {
            return hexDigit - 'a' + 10;
        } else if (hexDigit >= 'A' && hexDigit <= 'F') {
            return hexDigit - 'A' + 10;
        } else {
            throw new IllegalArgumentException("Invalid hex digit: " + hexDigit);
        }
    }

}
