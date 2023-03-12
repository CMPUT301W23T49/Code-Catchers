package com.example.codecatchersapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QrActionsActivity extends AppCompatActivity {

    private Switch geolocationSwitch;
    private Switch photoSwitch;
    private EditText commentEditText;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_actions);

        Intent intent = getIntent();

        geolocationSwitch = findViewById(R.id.geolocation_switch);
        photoSwitch = findViewById(R.id.photo_switch);
        commentEditText = findViewById(R.id.editTextNewMonComment);
        continueButton = findViewById(R.id.continue_photo_button);

        // Add any necessary listeners and functionality to the views
    }
}