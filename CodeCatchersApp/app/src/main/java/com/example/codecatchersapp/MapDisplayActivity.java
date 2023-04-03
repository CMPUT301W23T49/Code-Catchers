package com.example.codecatchersapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.codecatchersapp.databinding.ActivityMapDisplayBinding;


import android.util.Log;

import android.widget.Button;
import android.widget.EditText;

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
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.listeners.GeoQueryEventListener;

import java.util.ArrayList;
import java.util.List;

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

            // Add a purple marker for the current location
            float huePurple = 290;
            MarkerOptions currentLocationMarker = new MarkerOptions()
                    .position(mCurrentLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(huePurple));
            mMap.addMarker(currentLocationMarker);
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

        // Begin Search Query in DB
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoPoint location) {
                // Retrieve the MonsterDB name associated with the GeoPoint
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("monsters").document(key)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            String monsterName = key;
                            // Create LatLng objects for each document within the radius
                            LatLng documentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(documentLocation)
                                    .title(monsterName) // set the MonsterDB name as the marker title
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                            markers.add(mMap.addMarker(markerOptions));
                            Log.d("MyApp", "Marker title set to: " + monsterName);
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
        public void onMapReady(GoogleMap googleMap){
            mMap = googleMap;
            moveCameraToCurrentLocation();
        }
    }