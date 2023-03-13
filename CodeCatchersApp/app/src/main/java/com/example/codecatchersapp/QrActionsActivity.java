/**
 QrActionsActivity
 This class is used to control the behavior of the QR Actions activity.
 The QR Actions activity allows users to add optional data to a QR code before it is scanned,
 such as a comment or a photo.
 @author Noah Eglauer
 @version 1.0
 @since March 5 2023
 */
package com.example.codecatchersapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QrActionsActivity extends AppCompatActivity {
    /**
     * A switch that determines whether the user should be prompted to add their geolocation to the QR code.
     */
    private Switch geolocationSwitch;

    /**
     * A switch that determines whether the user should be prompted to take a photo to add to the QR code.
     */
    private Switch photoSwitch;
    /**
     * An edit text that allows the user to add a comment to the QR code.
     */
    private EditText commentEditText;
    /**
     * A button that allows the user to continue to the next activity.
     */
    private Button continueButton;
    /**
     * This method is called when the activity is first created.
     * It sets the content view to the QR actions layout, finds views by their IDs, and sets click listeners for the buttons.
     *
     * @param savedInstanceState  The saved instance state.
     */
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