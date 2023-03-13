package com.example.codecatchersapp;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SocialMenuActivity extends AppCompatActivity {

    private TextView titleTextView;
    private Button browseUsersButton;
    private Button browseQRCodesButton;
    private Button leaderboardsButton;
    private Button myProfileButton;

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
