package com.example.codecatchersapp;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.imperiumlabs.geofirestore.GeoFirestore;

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
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String myUserName = sharedPreferences.getString("username", "");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_options);
        Intent intent = getIntent();
        String monsterHash = "lols";//intent.getStringExtra("monsterHash");
        String userID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
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
             *
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
                } else {
                    Monster monster = new Monster("someMonsterID");
                    CollectionReference collectionReference = db.collection("MonsterDB");
                    DocumentReference documentReference = collectionReference.document("someMonsterID");
                    documentReference.set(monster);
                }

                Boolean locationPhotoToggleState = locationPhotoToggle.isChecked();
                // TODO: IF TRUE, GO TO CAMERA AFTER CONTINUE CLICKED, ELSE GO MAIN MENU?
                if (locationPhotoToggleState == false) {
                    goMainMenu();
                } else {
                    Intent intent = new Intent(QROptionsActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
            }

            /**
             * Retrieves user's geolocation and saves it in the database
             */
            public void saveGeolocation() {
                // TODO: change SomeUserID to current user's ID, change someMonsterID to monster hash
                db = FirebaseFirestore.getInstance();

                // save to monsterDB
                GeoFirestore geoFirestore = new GeoFirestore(db.collection("MonsterDB"));
                GeoPoint geoloc = new GeoPoint(finalLatitude, finalLongitude);
                geoFirestore.setLocation(monsterHash, geoloc);

                // save to playerDB
                CollectionReference collectionReferenceGeoLocation = db.collection("PlayerDB/" + userID + "/Monsters/ " + "monsterHash" + "/geolocationData");
                Map<String, Object> coordinates = new HashMap<>();
                coordinates.put("geoPoint", geoloc);

                DocumentReference docReference = collectionReferenceGeoLocation.document("geoPoint");
                docReference.set(coordinates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Location added to Firestore");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding location to Firestore", e);
                            }
                        });
            }


            /**
             * Saves the users comment to the database
             */
            String userName;

            public void saveComment() {
                CollectionReference collectionReference = db.collection("PlayerDB/" + userID + "/Monsters/" + "monsterHash" + "/comments");
                final String ogComment = commentEditText.getText().toString();
                HashMap<String, String> data = new HashMap<>();

                if (ogComment.length() > 0) {
                                    // TODO: change SomeUserID to current user's ID, change someMonsterID to monster hash
                                    data.put("userName", myUserName);
                                    collectionReference
                                            .document(ogComment)
                                            .set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("Success", "Comment added successfully!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("Failure", "Comment addition failed" + e.toString());
                                                }
                                            });

                                }
                            }
                        });

            }

            /**
             * Directs activity to main menu
             */
            public void goMainMenu() {
                // Change MainActivity.class to MainMenuActivity.class once merged
                Intent intent = new Intent(QROptionsActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        }