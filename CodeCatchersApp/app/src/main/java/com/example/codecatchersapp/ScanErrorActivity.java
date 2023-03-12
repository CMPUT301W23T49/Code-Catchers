package com.example.codecatchersapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ScanErrorActivity extends AppCompatActivity {
    private Button retryButton;
    private Button quitToMenuButton;

    @SuppressLint("MissingInflatedId")  // idk y this is needed
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_error);
        Intent intent = getIntent();

        retryButton = findViewById(R.id.retry_button);
        quitToMenuButton = findViewById(R.id.quit_to_menu_button);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                Intent intent = new Intent(ScanErrorActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        });

        quitToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                Intent intent = new Intent(ScanErrorActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
