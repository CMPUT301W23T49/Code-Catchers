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
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 The UserSettingsActivity class is responsible for displaying and allowing the user to edit their settings.
 It extends the AppCompatActivity class and overrides the onCreate() method.
 */
public class UserSettingsActivity extends AppCompatActivity {
    private FloatingActionButton backButton;
    private EditText editUserName;
    private EditText editContactInfo;
    private Button cancelButton;
    private Button saveButton;
    private String deviceID;
    private String userName;
    private String contactInfo;

    /**
     This method is called when the activity is created.
     It initializes the views and sets the corresponding listeners.
     @param savedInstanceState A Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view
        setContentView(R.layout.user_settings);

        // Get intent extras
        Intent intent = getIntent();
        deviceID = intent.getStringExtra("deviceID");
        userName = intent.getStringExtra("userName");
        contactInfo = intent.getStringExtra("contactInfo");

        // Get the views for UserSettingsActivity
        backButton = findViewById(R.id.back_button);
        editUserName = findViewById(R.id.edit_username);
        editContactInfo = findViewById(R.id.edit_contact);
        cancelButton = findViewById(R.id.cancel_button);
        saveButton = findViewById(R.id.save_button);

        // Set the text views
        editUserName.setText(userName);
        editContactInfo.setText(contactInfo);

        // Set on click listeners
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
