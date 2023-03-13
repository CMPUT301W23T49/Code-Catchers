/**
 The HashGenerator class is used to generate and write hashes to Firebase Firestore.
 The generated hash is a six bit representation of a SHA-256 hash of the first six characters of a QR code.
 The generated hash and the QR code are written to the "hashes" collection in Firebase Firestore.
 @author [Mathew Maki]
 @version 1.0
 @since [Sunday March 5 2023]
 */
package com.example.codecatchersapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

 import com.example.codecatchersapp.MainMenuActivity;
 import com.example.codecatchersapp.UserAccount;

 import java.io.Serializable;

public class UserAccountActivity extends AppCompatActivity {
    /** EditText view for entering the username */
    private EditText etUsername;
    /** EditText view for entering the contact information */
    private EditText etContact;
    /** Continue button */
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        etUsername = findViewById(R.id.et_username);
        etContact = findViewById(R.id.et_contact);
        continueButton = findViewById(R.id.button);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the username entered by the user

                String username = etUsername.getText().toString().trim();

                // Create a new UserAccount object and set the username
                UserAccount userAccount = new UserAccount(username, "");

                Intent intent = new Intent(UserAccountActivity.this, MainMenuActivity.class);
                //startActivity(intent);

                //UserAccountActivity.this.startActivity(intent);
                startActivity(intent);      // TODO: ERROR CHECK FOR NO USERNAME! --> NOAH

            }
        });
    }
}