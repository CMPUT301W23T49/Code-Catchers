/**
 * a class
 * @author CMPUT301W23T49
 * @version 1.0
 * @since [Monday April 3]
 */
package com.example.codecatchersapp;

import android.Manifest;


import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.imperiumlabs.geofirestore.listeners.GeoQueryEventListener;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.codecatchersapp.databinding.ActivityMapDisplayBinding;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * MapActivity is an implementation of the OnMapReadyCallback interface.
 * It is responsible for displaying a Google Map with markers on it.
 */

public class MapDisplayActivity extends FragmentActivity implements OnMapReadyCallback {

    /**
     * The entry point to the Fused Location Provider.
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private ActivityMapDisplayBinding binding;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng mCurrentLocation;
    private EditText searchBox;
    private Button searchButton;

    /**
     * Called when the activity is starting.
     * Connects to the map_layout.xml and sets it as the content view.
     * Gets the intent and gets the SupportMapFragment for the map.
     * Accesses user current location if permission is granted and initializes camera in that location.
     * Registers this activity as the callback for when the map is ready.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                                                                           // Call the super class onCreate to complete the creation of activity like the view hierarchy

        Intent intent = getIntent();                                                                                   // Get the intent

        binding = ActivityMapDisplayBinding.inflate(getLayoutInflater());                                              // Connect to the map_layout.xml
        setContentView(binding.getRoot());                                                                             // Connect to the map_layout.xml and set it as the content view


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);  // map_layout.xml
        mapFragment.getMapAsync(this);                                                                         // This is the callback


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);                   // Get the current location of the device and set the position of the map.


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)                  // If permission is not granted, request it
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)         // If permission is not granted, request it
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,                                                             // Request permission
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);                                                                 // Request code
            return;
        }

        // If permission is granted, get the current location
        mFusedLocationProviderClient.getLastLocation()                                                                // Get the current location of the device and set the position of the map.
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {                               // Add a listener to the task
                    /**
                     * Called when the task is successful.
                     * Gets the location and sets the current location.
                     * Moves the camera to the current location.
                     *
                     * @param location The location of the device.
                     */
                    @Override
                    public void onSuccess(Location location) {                                                       // If the task is successful, get the location
                        if (location != null) {                                                                      // If the location is not null, set the current location
                            mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());          // Set the current location
                            moveCameraToCurrentLocation();                                                           // Move the camera to the current location
                        }
                    }
                });

        // Initialize the search box and button
        searchButton = findViewById(R.id.search_button);                                         // Initialize the search button

        // Set OnClickListener for the search button
        searchButton.setOnClickListener(v -> {                                                   // Set the OnClickListener for the search button
            DialogFragment dialog = new SearchRadiusFragment();                                  // Create a new SearchRadiusFragment
            dialog.show(getSupportFragmentManager(), "SearchRadiusDialogFragment");         // Show the SearchRadiusFragment
            Log.d("Opening Dialog", "onSearchRadiusSelected: ");                       // Log that the dialog is opening
        });

       ImageButton backButton = findViewById(R.id.back_button);                                             // Initialize the back button
        // Set an OnClickListener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate back to the main menu activity
                Intent intent = new Intent(MapDisplayActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * Called when the map is ready to be used.
     * Sets the map type to normal and enables the location layer.
     * Sets the OnMarkerClickListener for the map.
     * Sets the OnMapClickListener for the map.
     * Sets the OnMapLongClickListener for the map.
     */
    private void moveCameraToCurrentLocation() {                                                 // Move the camera to the current location
        if (mMap != null && mCurrentLocation != null) {                                          // If the map is not null and the current location is not null
            int zoomLevel = getZoomLevelFromRadius(10);                          // Get the zoom level from the radius
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, zoomLevel));     // Move the camera to the current location

        }
    }
    /**
     * Called when the map is ready to be used.
     * Sets the map type to normal and enables the location layer.
     * Sets the OnMarkerClickListener for the map.
     * Sets the OnMapClickListener for the map.
     * Sets the OnMapLongClickListener for the map.
     */
    private int getZoomLevelFromRadius(double radiusInKilometers) {                            // Get the zoom level from the radius
        double scale = radiusInKilometers * 1000 / 900;                                        // 500 is a rough estimation of the number of meters per pixel at zoom level 1
        int zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));                            // Calculate the zoom level
        return zoomLevel;                                                                      // Return the zoom level
    }
    /**
     * Called when the map is ready to be used.
     * Sets the map type to normal and enables the location layer.
     * Sets the OnMarkerClickListener for the map.
     * Sets the OnMapClickListener for the map.
     * Sets the OnMapLongClickListener for the map.
     */
    public void onSearchRadiusSelected(int radius) {                                                     // When the search radius is selected
        // Query Firestore for documents within the specified radius
        FirebaseFirestore db = FirebaseFirestore.getInstance();                                         // Get the instance of the Firestore database
        GeoFirestore geoFirestore = new GeoFirestore(db.collection("MonsterDB"));          // Create a new GeoFirestore object

        // Creates a new geoQuery object using the device's current location
        GeoPoint currentLocation = new GeoPoint(mCurrentLocation.latitude, mCurrentLocation.longitude);     // Create a new GeoPoint object
        GeoQuery geoQuery = geoFirestore.queryAtLocation(currentLocation, radius);                          // Create a new GeoQuery object

        List<Marker> markers = new ArrayList<>();                                                          // Create a new ArrayList of Markers

        // save to monsterDB
        // uncomment for testing purposes - adds a point in edmonton to db with correct format
        // GeoPoint geoloc = new GeoPoint(53.63, -113.3239);
        // geoFirestore.setLocation("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6", geoloc);

        // Begin Search Query in DB
        /**
         * Adds a GeoQueryEventListener to the GeoQuery object.
         * When a key is entered, the MonsterDB name associated with the GeoPoint is retrieved.
         * A new Score object is created using the MonsterDB name.
         * The score is retrieved from the Score object.
         * The score is used to determine the color of the marker.
         * A new Marker is created using the GeoPoint and the color.
         * The Marker is added to the ArrayList of Markers.
         */
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {                             // Add a GeoQueryEventListener to the GeoQuery object
            @Override
            public void onKeyEntered(String key, GeoPoint location) {                               // When a key is entered
                // Retrieve the MonsterDB name associated with the GeoPoint
                FirebaseFirestore db = FirebaseFirestore.getInstance();                             // Get the instance of the Firestore database
                db.collection("MonsterDB").document(key)                               // Get the document with the key
                        .get()                                                                      // Get the document
                        .addOnSuccessListener(documentSnapshot -> {                                 // Add a listener to the task
                            String monsterHash = key;                                               // Get the monster hash from the key

                            Score markerScore;                                                      // create a new score object
                            try {
                                markerScore = new Score(monsterHash);                               // create a new score object
                            } catch (NoSuchAlgorithmException e) {                                  // catch NoSuchAlgorithmException
                                throw new RuntimeException(e);                                      // throw a new RuntimeException
                            }

                            String score = markerScore.getScore();                                 // get the score from the markerScore object
                            String scoreNew = "QR Score: " + score;                               // create a new string with the score
                            Log.d("MarkerScore", "Score for " + key + " is " + score);            // log the score

                            LatLng documentLocation = new LatLng(location.getLatitude(), location.getLongitude());    // Create a new LatLng object
                            MarkerOptions markerOptions = new MarkerOptions()                                         // Create a new MarkerOptions object
                                    .position(documentLocation)                                                       // set the position of the marker
                                    .title(score)                                                                     // set the score as the marker title
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)); // set the marker icon
                            Marker marker = mMap.addMarker(markerOptions);                                            // Add the marker to the map
                            markers.add(marker);                                                                      // Add the marker to the ArrayList of markers


                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {                      // Add an OnMarkerClickListener to the map

                                /**
                                 * Called when a marker has been clicked.
                                 *
                                 * @param marker The marker that was clicked.
                                 * @return true if the listener has consumed the event (i.e., the default behavior should not occur);
                                 * false otherwise (i.e., the default behavior should occur). The default behavior is for the camera to move
                                 * such that the marker is centered and for the marker's info window to open, if it has one.
                                 */
                                @Override
                                public boolean onMarkerClick(Marker marker) {                                          // When a marker is clicked
                                    // Get the user's location
                                    Location userLocation = mMap.getMyLocation();                                      // Get the user's location
                                    if (userLocation == null) {
                                        // If the user's location is unknown, show an error message
                                        Toast.makeText(MapDisplayActivity.this, "Unable to determine your location", Toast.LENGTH_SHORT).show(); // Show a Toast with the error message
                                        return true;
                                    }

                                    float[] results = new float[1];
                                    Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),// Calculate the distance between the user and the marker
                                            marker.getPosition().latitude, marker.getPosition().longitude, results); // Calculate the distance between the user and the marker
                                    float distance = results[0] / 1000;                                              // Convert to kilometers


                                    String distanceText = String.format("Distance: %.2f km", distance);                                              // Create a new string with the distance
                                    String scoreText = marker.getTitle();                                                                            // Get the marker title
                                    Toast.makeText(MapDisplayActivity.this, scoreText + "\n" + distanceText, Toast.LENGTH_LONG).show(); // Show a Toast with the score and distance
                                    return true;
                                }
                            });
                        })
                        .addOnFailureListener(e -> {                                                        // Add a listener to the task
                        });
            }
            /**
             * Called when a key is exited from the search area.
             * @param key The key that exited.
             */
            @Override
            public void onKeyExited(String key) {                                                          // When a key is exited
            }
            /**
             * Called when a key moves within the search area.
             * @param key The key that moved.
             * @param location The new location of the key.
             */
            @Override
            public void onKeyMoved(String key, GeoPoint location) {                                        // When a key is moved
            }
            /**
             * Called when the GeoQuery is ready.
             */
            @Override
            public void onGeoQueryReady() {                                                               // When the GeoQuery is ready

                mMap.clear();                                                                             // Clear the map
                for (Marker marker : markers) {                                                           // For each marker in the ArrayList of markers
                    mMap.addMarker(new MarkerOptions().position(marker.getPosition()));                   // Add the marker to the map
                }
            }
            /**
             * Called when there is an error with the GeoQuery.
             * @param error The error message.
             */
            @Override
            public void onGeoQueryError(Exception error) {
                // Handle the error here
            }
        });
    }

    /**
     * Called when the map is ready to be used.
     * @param googleMap The GoogleMap object used to display the map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {                                                   // When the map is ready
        mMap = googleMap;                                                                           // Set the mMap variable to the GoogleMap object
        moveCameraToCurrentLocation();                                                              // Move the camera to the user's current location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;                                                                                 // Return
        }
        mMap.setMyLocationEnabled(true);                                                           // Enable the user's location on the map
    }
    /**
     * Moves the camera to the user's current location.
     */
    public void onMarkerClick(Marker marker) {                                                      // When a marker is clicked
        Location userLocation = mMap.getMyLocation();                                               // Get the user's location
        if (userLocation == null) {                                                                 // If the user's location is unknown, show an error message
            Toast.makeText(this, "Unable to determine your location", Toast.LENGTH_SHORT).show(); // Show a Toast with the error message
            return;                                                                                 // Return
        }


        float[] results = new float[1];                                                             // Create a new float array
        Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),           // Calculate the distance between the user and the marker
                marker.getPosition().latitude, marker.getPosition().longitude, results);            // Calculate the distance between the user and the marker
        float distance = results[0] / 1000;                                                         // Convert to kilometers


        String distanceText = String.format("Distance: %.2f km", distance);                         // Create a new string with the distance
        Toast.makeText(this, distanceText, Toast.LENGTH_SHORT).show();                       // Show a Toast with the distance
    }
}