package com.example.codecatchersapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.provider.Settings.Secure;

import androidx.fragment.app.Fragment;
public class CreateAccountFragment extends Fragment {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the EditText view for the username input
        EditText usernameEditText = view.findViewById(R.id.et_username);

        // Find the EditText view for the contact info input
        EditText contactInfoEditText = view.findViewById(R.id.et_contact);

        // Find the Continue button and set an OnClickListener
        Button continueButton = view.findViewById(R.id.button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the username input from the EditText view
                String username = usernameEditText.getText().toString();

                // Get the contact info input from the EditText view
                String contactInfo = contactInfoEditText.getText().toString();

                // Create a new UserAccount object with the username and contact info inputs
                UserAccount userAccount = new UserAccount(username, contactInfo);

                // TODO: Save the user account object to your app's data store
                String deviceID = Secure.getString(getContext().getContentResolver(),
                        Secure.ANDROID_ID);
                // Navigate to the next screen
            }
        });
    }

}