/**

 MainMenuActivity is an Android activity that provides the main menu for the Codecatchers app.
 The main menu allows the user to navigate to different parts of the app, such as scanning QR codes,
 accessing social features, and viewing a map.
 @author [Noah E, Kyle, Josie]
 @version 1.0
 @since [Feb 23 2023]
 */
package com.example.codecatchersapp;


import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;



import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Intent intent = getIntent();


        Button scanQrButton = findViewById(R.id.scan_qr_button);
        Button socialButton = findViewById(R.id.social_button);
        Button mapButton = findViewById(R.id.map_button);
        /**
         * When the scanQrButton is clicked, the user is navigated to the ScannerActivity.
         */
        scanQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scannerIntent = new Intent(MainMenuActivity.this, ScannerActivity.class);
                startActivity(scannerIntent);
            }
        });
        /**
         * When the socialButton is clicked, the user is navigated to the SocialMenuActivity.
         */
        socialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement social functionality

                Intent socialIntent = new Intent(MainMenuActivity.this, SocialMenuActivity.class);
                startActivity(socialIntent);

            }
        });
        /**
         * When the mapButton is clicked, the user is navigated to the MapActivity.
         */
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement map functionality

                Intent mapIntent = new Intent(MainMenuActivity.this, MapActivity.class);
                startActivity(mapIntent);
            }
        });
    }}

