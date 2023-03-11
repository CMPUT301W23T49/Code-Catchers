package com.example.codecatchersapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MonInfoActivity extends Fragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        EditText commentEditText = view.findViewById(R.id.editTextNewMonComment);

        Switch geolocationToggle = view.findViewById(R.id.geolocation_switch);
        Switch locationPhotoToggle = view.findViewById(R.id.photo_switch);

        Button continueMonSettings = view.findViewById(R.id.continue_photo_button);

        continueMonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ogComment = commentEditText.getText().toString();
                // ADD COMMENT TO DATABASE

                Boolean geolocationToggleState = geolocationToggle.isChecked();
                // IF TRUE, RECORD LOCATION
                Boolean locationPhotoToggleState = locationPhotoToggle.isChecked();
                // IF TRUE, GO TO CAMERA AFTER CONTINUE CLICKED
            }
        });


    }
}
