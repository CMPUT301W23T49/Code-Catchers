package com.example.codecatchersapp;

import android.Manifest;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;

import android.util.Log;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.imperiumlabs.geofirestore.listeners.GeoQueryEventListener;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.codecatchersapp.databinding.ActivityMapDisplayBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MapActivity is an implementation of the OnMapReadyCallback interface.
 * It is responsible for displaying a Google Map with markers on it.
 */

public class MapDisplayActivity extends FragmentActivity implements OnMapReadyCallback {

    /**
     * The GoogleMap object used to display the map.
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
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        binding = ActivityMapDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get the FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Request permission to access the user's location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // If permission is granted, get the current location
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            moveCameraToCurrentLocation();
                        }
                    }
                });

        // Initialize the search box and button
        searchButton = findViewById(R.id.search_button);

        // Set OnClickListener for the search button
        searchButton.setOnClickListener(v -> {
            DialogFragment dialog = new SearchRadiusFragment();
            dialog.show(getSupportFragmentManager(), "SearchRadiusDialogFragment");
            Log.d("Opening Dialog", "onSearchRadiusSelected: ");
        });

    }

    private void moveCameraToCurrentLocation() {
        if (mMap != null && mCurrentLocation != null) {
            int zoomLevel = getZoomLevelFromRadius(10);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, zoomLevel));

        }
    }

    private int getZoomLevelFromRadius(double radiusInKilometers) {
        double scale = radiusInKilometers * 1000 / 900; // 500 is a rough estimation of the number of meters per pixel at zoom level 1
        int zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        return zoomLevel;
    }

    public void onSearchRadiusSelected(int radius) {
        // Query Firestore for documents within the specified radius
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        GeoFirestore geoFirestore = new GeoFirestore(db.collection("MonsterDB"));

        // Creates a new geoQuery object using the device's current location
        GeoPoint currentLocation = new GeoPoint(mCurrentLocation.latitude, mCurrentLocation.longitude);
        GeoQuery geoQuery = geoFirestore.queryAtLocation(currentLocation, radius);

        List<Marker> markers = new ArrayList<>();

        // save to monsterDB
        // uncomment for testing purposes - adds a point in edmonton to db with correct format
        // GeoPoint geoloc = new GeoPoint(53.63, -113.3239);
        // geoFirestore.setLocation("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6", geoloc);

        // Begin Search Query in DB
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoPoint location) {
                // Retrieve the MonsterDB name associated with the GeoPoint
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("MonsterDB").document(key)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            String monsterHash = key;

                            // creates a new score object to calculate marker score
                            Score markerScore;
                            try {
                                markerScore = new Score(monsterHash);
                            } catch (NoSuchAlgorithmException e) {
                                throw new RuntimeException(e);
                            }

                            String score = markerScore.getScore();
                            String scoreNew = "QR Score: " + score;
                            Log.d("MarkerScore", "Score for " + key + " is " + score);

                            // Create LatLng objects for each document within the radius
                            LatLng documentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(documentLocation)
                                    .title(score) // set the score as the marker title
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                            Marker marker = mMap.addMarker(markerOptions);
                            markers.add(marker);

                            // Add an OnMarkerClickListener to the marker
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    // Get the user's location
                                    Location userLocation = mMap.getMyLocation();
                                    if (userLocation == null) {
                                        // If the user's location is unknown, show an error message
                                        Toast.makeText(MapDisplayActivity.this, "Unable to determine your location", Toast.LENGTH_SHORT).show();
                                        return true;
                                    }

                                    // Calculate the distance between the user's location and the marker
                                    float[] results = new float[1];
                                    Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
                                            marker.getPosition().latitude, marker.getPosition().longitude, results);
                                    float distance = results[0] / 1000; // Convert to kilometers

                                    // Show a Toast with the distance and score
                                    String distanceText = String.format("Distance: %.2f km", distance);
                                    String scoreText = marker.getTitle();
                                    Toast.makeText(MapDisplayActivity.this, scoreText + "\n" + distanceText, Toast.LENGTH_LONG).show();
                                    return true;
                                }
                            });
                        })
                        .addOnFailureListener(e -> {
                            // handle any errors retrieving the MonsterDB name
                        });
            }

            @Override
            public void onKeyExited(String key) {
            }

            @Override
            public void onKeyMoved(String key, GeoPoint location) {
            }

            @Override
            public void onGeoQueryReady() {
                // Clear any existing markers on the map and add the new markers
                mMap.clear();
                for (Marker marker : markers) {
                    mMap.addMarker(new MarkerOptions().position(marker.getPosition()));
                }
            }

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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        moveCameraToCurrentLocation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void onMarkerClick(Marker marker) {
        // Get the user's location
        Location userLocation = mMap.getMyLocation();
        if (userLocation == null) {
            // If the user's location is unknown, show an error message
            Toast.makeText(this, "Unable to determine your location", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate the distance between the user's location and the marker
        float[] results = new float[1];
        Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
                marker.getPosition().latitude, marker.getPosition().longitude, results);
        float distance = results[0] / 1000; // Convert to kilometers

        // Show a Toast with the distance
        String distanceText = String.format("Distance: %.2f km", distance);
        Toast.makeText(this, distanceText, Toast.LENGTH_SHORT).show();
    }
}