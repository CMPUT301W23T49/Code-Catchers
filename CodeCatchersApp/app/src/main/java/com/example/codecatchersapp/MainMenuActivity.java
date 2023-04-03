/**
 * a class
 * @author CMPUT301W23T49
 * @version 1.0
 * @since [Monday April 3]
 */
package com.example.codecatchersapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The MainMenuActivity class represents the main menu screen of the app. It contains buttons for navigating to the scanner,
 * social menu, and map screens.
 */
public class MainMenuActivity extends AppCompatActivity {
    /**
     * Called when the activity is starting. Sets the layout and sets up the click listeners for the buttons.
     * @param savedInstanceState saved state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Intent intent = getIntent();

        Button scanQrButton = findViewById(R.id.scan_qr_button);
        Button socialButton = findViewById(R.id.social_button);
        Button mapButton = findViewById(R.id.map_button);

        // Set up the click listeners for the buttons
        scanQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scannerIntent = new Intent(MainMenuActivity.this, ScannerActivity.class);
                startActivity(scannerIntent);
            }
        });

        socialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent socialIntent = new Intent(MainMenuActivity.this, SocialMenuActivity.class);
                startActivity(socialIntent);

            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mapIntent = new Intent(MainMenuActivity.this, MapDisplayActivity.class);
                startActivity(mapIntent);
            }
        });
    }
}