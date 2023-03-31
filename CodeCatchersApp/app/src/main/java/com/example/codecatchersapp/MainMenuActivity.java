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
    }}

