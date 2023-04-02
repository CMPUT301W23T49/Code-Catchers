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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 * HamburgerMenuFragment is a DialogFragment that displays a hamburger menu with navigation options.
 * It fetches the user's data from the database and allows the user to navigate to the main menu
 * and user profile.
 */
public class HamburgerMenuFragment extends DialogFragment {
    private Context context;
    private UserAccount user;
    private int id;
    private Button mainMenuButton;
    private Button profileButton;
    FirebaseFirestore db;

    /**
     * Constructor for HamburgerMenuFragment.
     *
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
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    /**
     * Called to create a new dialog for the fragment.
     *
     * @param savedInstanceState a Bundle containing any previous saved state.
     * @return a new Dialog instance to be displayed by the fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the hamburger menu view
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.fragment_hamburger, null);

        // Connect to the database
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("PlayerDB");
        String deviceID = Settings.Secure.getString(this.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        DocumentReference documentReference = collectionReference.document(deviceID);
        Log.i("TAG", deviceID);
        // Get the tag passed from the show() method
        String tag = getTag();

        // Get the views for the buttons
        mainMenuButton = view.findViewById(R.id.hb_main_menu_button);
        profileButton = view.findViewById(R.id.hb_profile_button);

        // Set the click listeners for the buttons
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG", "Clicked the main menu button");

                // Move to the MainMenuActivity
                Intent mainMenuIntent = new Intent(getContext(), MainMenuActivity.class);
                startActivity(mainMenuIntent);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG", "Clicked the profile button");
                // Get the fragment manager for the UserProfileFragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment profileFragment = new UserProfileFragment(deviceID);
                fragmentManager.beginTransaction()
                        .replace(id, profileFragment)
                        .addToBackStack(null)
                        .commit();
                // Hide the hamburger menu when moving to the new fragment
                HamburgerMenuFragment.this.dismiss();

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder.setView(view).create();
    }
}
