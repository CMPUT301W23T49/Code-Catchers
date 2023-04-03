/**
 * a class
 * @author CMPUT301W23T49
 * @version 1.0
 * @since [Monday April 3]
 */
package com.example.codecatchersapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 * HamburgerMenuFragment is a DialogFragment that displays a hamburger menu with navigation options.
 * It fetches the user's data from the database and allows the user to navigate to the main menu
 * and user profile.
 */
public class HamburgerMenuFragment extends DialogFragment {

    private Context context;                                        // The context of the fragment host
    private UserAccount user;                                       // The user's account
    private int id;                                                 // The fragment container view ID
    private Button mainMenuButton;                                  // Button to navigate to the main menu
    private Button profileButton;                                   // Button to navigate to the user profile
    FirebaseFirestore db;                                           // The database

    /**
     * Constructor for HamburgerMenuFragment.
     * @param id the fragment container view ID.
     */
    public HamburgerMenuFragment(int id) {
        this.id = id;
    }

    /**
     * Called when the fragment is attached to its host. Here, we save the context.
     *
     * @param context the context of the fragment host.
     */
    @Override
    public void onAttach(@NonNull Context context) {               // Called when the fragment is attached to its host
        this.context = context;                                    // Save the context
        super.onAttach(context);                                   // Call the superclass method
    }

    /**
     * Called to create a new dialog for the fragment.
     *
     * @param savedInstanceState a Bundle containing any previous saved state.
     * @return a new Dialog instance to be displayed by the fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {                                          // Called to create a new dialog for the fragment
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.fragment_hamburger, null);      // Inflate the hamburger menu view

        db = FirebaseFirestore.getInstance();                                                                    // Connect to database instance
        CollectionReference collectionReference = db.collection("PlayerDB");                        // Get the collection reference
        String deviceID = Settings.Secure.getString(this.getContext().getContentResolver(), Settings.Secure.ANDROID_ID); // Get the device ID
        DocumentReference documentReference = collectionReference.document(deviceID);                            // Get the document reference
        Log.i("TAG", deviceID);

        String tag = getTag();                                                                                   // The tag is the fragment container view ID

        mainMenuButton = view.findViewById(R.id.hb_main_menu_button);                                            // Button to navigate to the main menu
        profileButton = view.findViewById(R.id.hb_profile_button);                                               // Button to navigate to the user profile
        /**
         * Set the click listener for the main menu button.
         * When the button is clicked, the user is navigated to the MainMenuActivity.
         */
        mainMenuButton.setOnClickListener(new View.OnClickListener() {                                           // Set the click listener for the main menu button
            @Override
            public void onClick(View view) {                                                                    // When the button is clicked
                Log.i("TAG", "Clicked the main menu button");

                // Move to the MainMenuActivity
                Intent mainMenuIntent = new Intent(getContext(), MainMenuActivity.class);                       // Create an intent to start the MainMenuActivity
                startActivity(mainMenuIntent);                                                                  // Start the activity
            }
        });
        /**
         * Set the click listener for the profile button.
         * When the button is clicked, the user's data is fetched from the database and the user is
         * navigated to the MyProfileActivity.
         */
        profileButton.setOnClickListener(new View.OnClickListener() {                                           // Set the click listener for the profile button
            @Override
            public void onClick(View view) {                                                                    // When the button is clicked
                Log.i("TAG", "Clicked the profile button");

                Intent myProfileIntent = new Intent(getContext(), MyProfileActivity.class);                     // Create an intent to start the MyProfileActivity
                startActivity(myProfileIntent);                                                                 // Start the activity

                // Hide the hamburger menu when moving to the new fragment
                HamburgerMenuFragment.this.dismiss();

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());                                   // Create a new AlertDialog.Builder

        return builder.setView(view).create();                                                                  // Return the AlertDialog
    }
}
