/**
 * The UserAccountActivity class contains methods for
 * displaying and handling functions that assist the
 * player in creating an account and logging into the game
 * @author [Unknown]
 * @version 1.0
 * @since [Monday March 13 2021]
 */
package com.example.codecatchersapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

/**
 * UserAccountActivity is an extends AppCompatActivity.
 * It is responsible for displaying create_account.xml.
 */
public class UserAccountActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etContact;
    private Button continueButton;

    /**
     * Called when the activity is starting.
     * Connects to the create_account.xml and sets it as the content view.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        etUsername = findViewById(R.id.et_username);
        etContact = findViewById(R.id.et_contact);
        continueButton = findViewById(R.id.button);

        continueButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the continueButton is pressed.
             * Sets userAccount.
             * Switches intent to MainMenuActivity.
             *
             */
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