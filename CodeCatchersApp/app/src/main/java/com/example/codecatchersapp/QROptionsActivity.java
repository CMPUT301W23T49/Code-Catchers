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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class QROptionsActivity extends AppCompatActivity {
    FirebaseFirestore db;
    Bitmap picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_actions);
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
            @Override
            public void onClick(View view) {

                // Adds comment to firebase
                CollectionReference collectionReference = db.collection("PlayerDB/someUserID1/Monsters/someMonsterID/comment");
                // TODO: ADD COMMENT TO DATABASE
                final String ogComment = commentEditText.getText().toString();
                HashMap<String,String> data = new HashMap<>();//aa
                if (ogComment.length() > 0){
                    // TODO: change SomeUserID to current user's ID, change someMonsterID to monster hash
                    data.put("UserID","SomeUserID");
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

                // Adds geolocation data to firebase
                Boolean geolocationToggleState = geolocationToggle.isChecked();
                if (geolocationToggleState == true) {
                    // TODO: change SomeUserID to current user's ID, change someMonsterID to monster hash
                    CollectionReference collectionReferenceGeoLocation = db.collection("PlayerDB/someUserID1/Monsters/someMonsterID/geolocationData");

                    Map<String, Object> coordinates = new HashMap<>();
                    coordinates.put("Latitude", finalLatitude);
                    coordinates.put("Longitude", finalLongitude);

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
                                    Log.d("Failure", "Comment addition failed"+ e.toString());
                                }
                            });
                }

                Boolean locationPhotoToggleState = locationPhotoToggle.isChecked();
                // TODO: IF TRUE, GO TO CAMERA AFTER CONTINUE CLICKED, ELSE GO MAIN MENU?
                if (locationPhotoToggleState == false){
                    goMainMenu();
                } else{
                     // TODO: OPEN CAMERA, SAVED TO DB
                    Intent SwapIntent = new Intent(QROptionsActivity.this, CameraActivity.class);
                    startActivity(SwapIntent);

                }
            }
        });
    }
    public void goMainMenu(){
        // Change MainActivity.class to MainMenuActivity.class once merged
        Intent intent = new Intent(QROptionsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
