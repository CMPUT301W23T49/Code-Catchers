package com.example.codecatchersapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class UserAccountActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etContact;
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
                startActivity(intent);

                //String username = etUsername.getText().toString().trim();

                Intent intent = new Intent(UserAccountActivity.this, MainMenuActivity.class);

                //UserAccountActivity.this.startActivity(intent);
                startActivity(intent);      // TODO: ERROR CHECK FOR NO USERNAME! --> NOAH

            }
        });
    }
}