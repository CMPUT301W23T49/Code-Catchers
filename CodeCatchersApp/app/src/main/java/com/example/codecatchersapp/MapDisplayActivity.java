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
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import org.imperiumlabs.geofirestore.listeners.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.codecatchersapp.databinding.ActivityMapDisplayBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MapActivity is an implementation of the OnMapReadyCallback interface.
 * It is responsible for displaying a Google Map with markers on it.
 */

public class MapDisplayActivity extends FragmentActivity implements OnMapReadyCallback{

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

    private int latitude;
    private int longitude;


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

        // Connect to  map_layout.xml
        setContentView(R.layout.activity_map_display);
        Intent intent = getIntent();

        binding = ActivityMapDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapView mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


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
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Set the current location variable and move the camera
                            mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 10));
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

    public void onSearchRadiusSelected(int radius) {
        // Query Firestore for documents within the specified radius
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        GeoFirestore geoFirestore = new GeoFirestore(db.collection("MonsterDB"));

        // Creates a new geoQuery object using the device's current location
        GeoPoint currentLocation = new GeoPoint(mCurrentLocation.latitude, mCurrentLocation.longitude);
        GeoQuery geoQuery = geoFirestore.queryAtLocation(currentLocation, radius);

        List<Marker> markers = new ArrayList<>();


        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoPoint location) {
                // Create LatLng objects for each document within the radius
                LatLng documentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(documentLocation);
                markers.add(mMap.addMarker(markerOptions));
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
    }

}