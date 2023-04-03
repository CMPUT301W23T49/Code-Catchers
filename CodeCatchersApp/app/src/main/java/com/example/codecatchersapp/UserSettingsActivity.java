package com.example.codecatchersapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 UserSettingsActivity allows the user to change their contact info.
 */
public class UserSettingsActivity extends AppCompatActivity {
    private FloatingActionButton backButton;
    private EditText editUserName;
    private EditText editContactInfo;
    private Button cancelButton;
    private Button saveButton;
    private String deviceID;
    private String userName;
    private String contactInfo;
    /**
     * onCreate sets the content view, gets the intent extras, and sets the text views.
     * It also sets the on click listeners for the back button, cancel button, and save button.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view
        setContentView(R.layout.user_settings);



        // Get intent extras
        Intent intent = getIntent();
        deviceID = intent.getStringExtra("deviceID");
        userName = intent.getStringExtra("userName");
        contactInfo = intent.getStringExtra("contactInfo");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("PlayerDB");

        // Get the views for UserSettingsActivity
        backButton = findViewById(R.id.back_button);
        editUserName = findViewById(R.id.edit_username);
        editContactInfo = findViewById(R.id.edit_contact);
        cancelButton = findViewById(R.id.cancel_button);
        saveButton = findViewById(R.id.save_button);

        // Set the text views
        editUserName.setText(userName);
        editContactInfo.setText(contactInfo);

        // Set on click listeners
        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When the back button is clicked, the user is taken back to the MyProfileActivity.
             * @param view
             */
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When the cancel button is clicked, the user is taken back to the MyProfileActivity.
             * @param view
             */
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When the save button is clicked, the user's contact info is updated in the database.
             * @param view
             */
            @Override
            public void onClick(View view) {
                collectionReference.document(deviceID).update("contactInfo", editContactInfo.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            /**
                             * When the update is complete, the user is taken back to the MyProfileActivity.
                             * @param task
                             */
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(UserSettingsActivity.this, MyProfileActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(UserSettingsActivity.this, "Contact info updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UserSettingsActivity.this, "Failed to update contact info", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}