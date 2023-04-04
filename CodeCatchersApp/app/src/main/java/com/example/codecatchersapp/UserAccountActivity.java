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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import android.widget.Toast;


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

                String userName = etUsername.getText().toString().trim();
                String contactInfo = etContact.getText().toString().trim();

                // Create a new UserAccount object and set the username

                if (userName.length() > 0){
                    saveAccount(userName,contactInfo);
                }
                else{
                    // do nothing
                }


            }
        });
    }

    /**
     * Saves a user account with the provided username and contact information. This method stores the
     * username in the shared preferences, retrieves the device ID, and creates a UserAccount object.
     * It then initializes the Firebase instance and checks if the given username exists in the database.
     *
     * @param userName    The username of the user for which the account will be saved.
     * @param contactInfo The contact information associated with the user account.
     */

    public void saveAccount(String userName, String contactInfo){
        Log.d(TAG, "Saving account for user: " + userName);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", userName);
        editor.apply();

        Context context = getApplicationContext();
        String deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "Device ID: " + deviceID);

        // Create a UserAccount object
        UserAccount userAccount = new UserAccount(userName, contactInfo, deviceID);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("PlayerDB");
        DocumentReference documentReference = collectionReference.document(deviceID);

        // Check if the passed in username exists in the database
        collectionReference.whereEqualTo("username", userName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /** Called when the task is complete.
             *
             * @param task
             */
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Chick if the query result is empty
                    if (task.getResult().isEmpty()) {
                        documentReference.set(userAccount)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "User account saved to database.");

                                        Map<String,Object> leaderboardFields = new HashMap<>();
                                        leaderboardFields.put("totalscore", "0");
                                        leaderboardFields.put("monstercount", "0");
                                        leaderboardFields.put("highestmonsterscore", "0");

                                        documentReference.update(leaderboardFields)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    /**
                                                     * Called when the task is successful.
                                                     * @param unused
                                                     */
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d("E","FIELDS ADDED");

                                                        // Start MainMenuActivity here after saving the user account and updating leaderboard fields
                                                        Intent intent = new Intent(UserAccountActivity.this, MainMenuActivity.class);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    /**
                                                     * Called when the task fails.
                                                     * @param e
                                                     */
                                                    @Override
                                                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                                                        Log.d("E","FAILURE, FIELDS NOT ADDED");
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    /**
                                     * Called when the task fails.
                                     * @param e
                                     */
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "Error saving user account to database.", e);
                                    }
                                });
                    } else {
                        Toast.makeText(UserAccountActivity.this, "Invalid username", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }
}
