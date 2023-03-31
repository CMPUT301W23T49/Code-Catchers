package com.example.codecatchersapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity to display QR code saving options, such as geolocation, picture, and comment
 * Does not yet connect to true user's account/their collection in the database
 * Does not connect to specific monster's comment collection yet
 */
public class QROptionsActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     Takes in choices for photo and geolocation, saves comment to database
     @param savedInstanceState A Bundle object containing the activity's saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_options);
        Intent intent = getIntent();
        EditText commentEditText = findViewById(R.id.editTextNewMonComment);
        Switch geolocationToggle = findViewById(R.id.geolocation_switch);
        Switch locationPhotoToggle = findViewById(R.id.photo_switch);
        Button continueMonSettings = findViewById(R.id.continue_photo_button);

        double latitude = 0;
        double longitude = 0;
        final int PERMISSIONS_REQUEST_CODE = 123;

        // TODO: Can be made better by only prompting when user toggles for geo-location in future, but issues arise due to use of "this" in line 50
        // This if statement prompts the user for permission to access their location, if not already granted.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE);
            Log.d("Success","Used has not already enabled location, and so is prompted to share it.");
        }
        // This if statement gets location, only if access already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                latitude = lastKnownLocation.getLatitude();
                longitude = lastKnownLocation.getLongitude();
                Log.d("Success","Latitude and longitude data have been updated");
            }
        }

        // some boilerplate code for adding coordinates into firebase
        double finalLatitude = latitude;
        double finalLongitude = longitude;
        continueMonSettings.setOnClickListener(new View.OnClickListener() {
            /**
             * Triggers options to save their respective data to the database
             * @param view view that was clicked (continue button)
             */
            @Override
            public void onClick(View view) {

                // Adds comment to firebase


                // TODO: ADD COMMENT TO DATABASE
                saveComment();

                // Adds geolocation data to firebase

                Boolean geolocationToggleState = geolocationToggle.isChecked();
                if (geolocationToggleState == true) {
                    saveGeolocation();
                    //
                }

                Boolean locationPhotoToggleState = locationPhotoToggle.isChecked();
                // TODO: IF TRUE, GO TO CAMERA AFTER CONTINUE CLICKED, ELSE GO MAIN MENU?
                if (locationPhotoToggleState == false){
                    goMainMenu();
                }
                else{
                    Intent intent = new Intent(QROptionsActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
            }

            /**
             * Retrieves user's geolocation and saves it in the database
             */
            public void saveGeolocation() {
                // TODO: change SomeUserID to current user's ID, change someMonsterID to monster hash
                CollectionReference collectionReferenceGeoLocation = db.collection("PlayerDB/someUserID1/Monsters/someMonsterID/geolocationData");

                Map<String, Object> coordinates = new HashMap<>();
                coordinates.put("Latitude", finalLatitude);
                coordinates.put("Longitude", finalLongitude);

                Monster monster = new Monster("someMonsterID", finalLatitude, finalLongitude);

                db = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = db.collection("MonsterDB");

                DocumentReference documentReference = collectionReference.document("someMonsterID");
                documentReference.set(monster);

                collectionReferenceGeoLocation
                        .document("Location Data")
                        .update(coordinates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess (Void unused){
                                Log.d("Success", "LOCATION ADDED SUCCESSFULLY");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener(){
                            @Override
                            public void onFailure(@NonNull Exception e){
                                Log.d("Failure", "Location addition failed"+ e.toString());
                            }
                        });
            }

            /**
             * Saves the users comment to the database
             */
            public void saveComment() {
                CollectionReference collectionReference = db.collection("PlayerDB/someUserID1/Monsters/someMonsterID/comments");
                final String ogComment = commentEditText.getText().toString();
                HashMap<String,String> data = new HashMap<>();//aa
                if (ogComment.length() > 0){
                    // TODO: change SomeUserID to current user's ID, change someMonsterID to monster hash
                    data.put("userName","myUser");
                    collectionReference
                            .document(ogComment)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess (Void unused){
                                    Log.d("Success", "Comment added successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener(){
                                @Override
                                public void onFailure(@NonNull Exception e){
                                    Log.d("Failure", "Comment addition failed"+ e.toString());
                                }
                            });

                }
            }
        });

    }

    /**
     * Directs activity to main menu
     */
    public void goMainMenu(){
        // Change MainActivity.class to MainMenuActivity.class once merged
        Intent intent = new Intent(QROptionsActivity.this, MainMenuActivity.class);
        startActivity(intent);
    }
}