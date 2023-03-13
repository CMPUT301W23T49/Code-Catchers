/**
 * SocialMenuActivity is an activity that displays the social menu for the Code Catchers app.
 * It allows users to browse other users, browse QR codes, view leaderboards, and access their own profile.
 *
 * @author  Noah Eglauer
 * @version 1.0
 * @since   March 5 2023
 */
package com.example.codecatchersapp;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SocialMenuActivity extends AppCompatActivity {
    /** The text view that displays the title of the activity. */
    private TextView titleTextView;
    /** The button that allows users to browse other users. */
    private Button browseUsersButton;
    /** The button that allows users to browse QR codes. */
    private Button browseQRCodesButton;
    /** The button that allows users to view leaderboards. */
    private Button leaderboardsButton;
    /** The button that allows users to access their own profile. */
    private Button myProfileButton;
    /**
     * This method is called when the activity is first created.
     * It sets the content view to the social menu layout, finds views by their IDs, and sets click listeners for the buttons.
     *
     * @param savedInstanceState  The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.social_menu);

        // Find views by their IDs
        titleTextView = findViewById(R.id.title);
        browseUsersButton = findViewById(R.id.browse_users_button);
        browseQRCodesButton = findViewById(R.id.browse_QR_codes_button);
        leaderboardsButton = findViewById(R.id.leaderboards_button);
        myProfileButton = findViewById(R.id.my_profile_button);

        // Set click listeners for the buttons
        browseUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                Intent searchUsersIntent = new Intent(SocialMenuActivity.this, SearchUsersActivity.class);
                startActivity(searchUsersIntent);
            }
        });

        browseQRCodesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
            }
        });

        leaderboardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                Intent searchUsersIntent = new Intent(SocialMenuActivity.this, LeaderboardsActivity.class);
                startActivity(searchUsersIntent);
            }
        });

        myProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
            }
        });
    }
}
