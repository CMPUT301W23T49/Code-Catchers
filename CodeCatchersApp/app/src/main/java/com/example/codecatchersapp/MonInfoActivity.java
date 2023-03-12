package com.example.codecatchersapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
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

public class MonInfoActivity extends AppCompatActivity {
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        System.out.println("--------------------------------------------------TEST");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_options);
        Intent intent = getIntent();
        EditText commentEditText = findViewById(R.id.editTextNewMonComment);
        final int PERMISSIONS_REQUEST_CODE = 123;
        double latitude = 0;
        double longitude = 0;

        Switch geolocationToggle = findViewById(R.id.geolocation_switch);
        Switch locationPhotoToggle = findViewById(R.id.photo_switch);

        Button continueMonSettings = findViewById(R.id.continue_photo_button);

        // Can be made better by only prompting when user toggles for geo-location in future
        // This if statement prompts the user for permission to access their location, if not already granted.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE);
        }
        // This if statement gets location, if access already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                latitude = lastKnownLocation.getLatitude();
                longitude = lastKnownLocation.getLongitude();
            }
        }

        double finalLatitude = latitude;
        double finalLongitude = longitude;
        continueMonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                }
                //else{
                    // TODO: OPEN CAMERA, SAVED TO DB
                //}
            }
        });


    }
    public void goMainMenu(){
        // Change MainActivity.class to MainMenuActivity.class once merged
        Intent intent = new Intent(MonInfoActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
