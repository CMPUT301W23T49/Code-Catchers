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

public class HamburgerMenuFragment extends DialogFragment {
    private Context context;
    private UserAccount user;
    private int id;
    private Button mainMenuButton;
    private Button profileButton;
    FirebaseFirestore db;

    public HamburgerMenuFragment(int id) {
        this.id = id;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

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
                Intent mainMenuIntent = new Intent(getContext(), MainMenuActivity.class);
                startActivity(mainMenuIntent);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG", "Clicked the profile button");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment profileFragment = new UserProfileFragment(deviceID);
                fragmentManager.beginTransaction()
                        .replace(id, profileFragment)
                        .addToBackStack(null)
                        .commit();

                HamburgerMenuFragment.this.dismiss();

                /**
                // TODO: get the users
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Log.i("TAG", String.valueOf(documentSnapshot));
                                String userName =(String) documentSnapshot.get("UserName");
                                String contactInfo = (String) documentSnapshot.get("contactInfo");
                                user = new UserAccount(userName, contactInfo);
                                FragmentManager fragmentManager = getParentFragmentManager();
                                Fragment profileFragment = new UserProfileFragment(user);
                                fragmentManager.beginTransaction()
                                        .replace(id, profileFragment)
                                        .addToBackStack(null)
                                        .commit();
                                HamburgerMenuFragment.this.dismiss();
                            }
                        });
                 **/

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder.setView(view).create();
    }
}
