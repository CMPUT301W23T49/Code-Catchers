package com.example.codecatchersapp;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * The SocialMenuActivity class represents the main activity for the social menu screen.
 * It allows users to navigate to different parts of the social app.
 */
public class SocialMenuActivity extends AppCompatActivity {
    // UI components
    private TextView titleTextView;
    private ConstraintLayout constraintLayout;
    private Button browseUsersButton;
    private Button browseQRCodesButton;
    private Button leaderboardsButton;
    private Button myProfileButton;

    /**
     * Called when the activity is starting. Initializes the UI components and sets click listeners for the buttons.
     * @param savedInstanceState a Bundle object containing the activity's previously saved state
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
        constraintLayout = findViewById(R.id.social_menu);

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
                /**
                String deviceID = Settings.Secure.getString(SocialMenuActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                FragmentManager fragmentManager = getSupportFragmentManager();
                UserProfileFragment profileFragment = new UserProfileFragment(deviceID);
                fragmentManager.beginTransaction()
                        .replace(R.id.profile_container, profileFragment)
                        .addToBackStack(null)
                        .commit();

                // Hide the views from the social menu
                titleTextView.setVisibility(View.GONE);
                browseUsersButton.setVisibility(View.GONE);
                browseQRCodesButton.setVisibility(View.GONE);
                leaderboardsButton.setVisibility(View.GONE);
                myProfileButton.setVisibility(View.GONE);
                 **/
                Intent myProfileIntent = new Intent(SocialMenuActivity.this, MyProfileActivity.class);
                startActivity(myProfileIntent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Set the previously hidden views to visible
        titleTextView.setVisibility(View.VISIBLE);
        browseUsersButton.setVisibility(View.VISIBLE);
        browseQRCodesButton.setVisibility(View.VISIBLE);
        leaderboardsButton.setVisibility(View.VISIBLE);
        myProfileButton.setVisibility(View.VISIBLE);
    }
}
