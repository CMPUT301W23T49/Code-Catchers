/**
 * The UserAccountActivity class contains methods for
 * displaying and handling functions that assist the
 * player in creating an account and logging into the game
 * @author [Unknown]
 * @version 1.0
 * @since [Monday March 13 2021]
 */
package com.example.codecatchersapp;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import android.provider.Settings;
import android.content.Context;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * UserAccountActivity is an extends AppCompatActivity.
 * It is responsible for displaying create_account.xml.
 */
public class UserAccountActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etContact;
    private Button continueButton;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                String contactInfo = etContact.getText().toString().trim();

                // Create a new UserAccount object and set the username
                saveAccount(username,contactInfo);

                Intent intent = new Intent(UserAccountActivity.this, MainMenuActivity.class);
                //startActivity(intent);

                //UserAccountActivity.this.startActivity(intent);
                startActivity(intent);      // TODO: ERROR CHECK FOR NO USERNAME! --> NOAH

            }
        });
    }

    /**
     * Saves a user to the database by device ID
     * @param userName
     * @param contactInfo
     */
    public void saveAccount(String userName, String contactInfo){
        Log.d(TAG, "Saving account for user: " + userName);
        Context context = getApplicationContext();
        String deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "Device ID: " + deviceID);

        UserAccount userAccount = new UserAccount(userName, contactInfo, deviceID);

        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("PlayerDB");

        DocumentReference documentReference = collectionReference.document(deviceID);
        documentReference.set(userAccount)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User account saved to database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error saving user account to database.", e);
                    }
                });

    }
}