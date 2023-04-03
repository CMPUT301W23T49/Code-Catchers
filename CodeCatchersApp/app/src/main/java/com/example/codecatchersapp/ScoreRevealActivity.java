/**
 * This activity is used to display the score and monster name to the user.
 * The activity is started by the ScannerActivity when a QR code is scanned.
 * The activity is started by the CameraActivity when a photo is taken.
 *
 * The activity displays the score and monster name to the user.
 * The activity also displays a monster image based on the hash of the monster.
 * @author Code Catchers [Noah Eglauer]
 * @version 1.0
 * @since [Saturday March 6 2021]
 */
package com.example.codecatchersapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.security.NoSuchAlgorithmException;
/**
 * The ScoreRevealActivity class contains methods for
 * displaying the score and monster name to the user
 * @author [Noah Eglauer]
 * @version 1.0
 * @since [Saturday March 6 2021]
 */
public class ScoreRevealActivity extends AppCompatActivity {

    private int score;                                                 // Score
    private MonsterNameGenerator monsterNameGenerator;                 // Monster name generator

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                            // Call the super class onCreate to complete view hierarchy
        setContentView(R.layout.score_reveal_activity);                // Set the layout for score reveal activity


        monsterNameGenerator = new MonsterNameGenerator();             // New monster name generator

        // Get hash from intent
        Intent intent = getIntent();
        String hash = intent.getStringExtra("shaHash");             // Used for score calculation
        String binaryHash = intent.getStringExtra("binaryHash"); // Used for monster name generation and monster view
        System.out.println("binaryHashAGAIN: " + binaryHash);
        System.out.println("hashAGAIN: " + hash);

        String displayScore;                                           // Score to be displayed
        try {
            Score score = new Score(hash); // New score object that calculates score based on contents delivered from QR
            displayScore = score.getScore();                          // Get score from score object
        } catch (NoSuchAlgorithmException e) {                        // Catch exception
            throw new RuntimeException(e);                            // Throw runtime exception
        }

        RelativeLayout rootLayout = findViewById(R.id.root_layout);   // Find root layout
        rootLayout.setOnClickListener(new View.OnClickListener() {    // Set click listener for root layout
            /**
             * Handles the click event for the root layout.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {                             // Handle click
                Intent intent = new Intent(ScoreRevealActivity.this, QROptionsActivity.class); // New intent for QR options activity
                intent.putExtra("shaHash", hash);               // Put hash in intent
                intent.putExtra("binaryHash", binaryHash);
                startActivity(intent);                                // Start QR options activity
            }
        });

        TextView scoreTextView = findViewById(R.id.scoreTextView);    // Find score text view
        scoreTextView.setText(displayScore);                          // Set score text view to display score

        MonsterView viewMonster = findViewById(R.id.MonsterView);     // Find monster view
        if (viewMonster != null) {                                    // If monster view is not null
            viewMonster.setBinaryHash(binaryHash);                    // Set binary hash in monster view
        }

        TextView monsterNameTextView = findViewById(R.id.monsterNameTextView);        // Find monster name text view
        String monsterName = monsterNameGenerator.generateNameFromBinary(binaryHash); // Generate monster name from binary hash
        System.out.println("binaryHashNAMEGENERATOR: " + binaryHash);                 // Print binary hash
        monsterNameTextView.setText(monsterName);                                     // Set monster name text view to display monster name

    }

}
