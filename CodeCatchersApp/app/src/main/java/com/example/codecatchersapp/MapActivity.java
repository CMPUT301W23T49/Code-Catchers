/**
 * This class is used to display a map with markers on it.
 * The markers are randomly generated and placed on the map.
 * The map is displayed in map_layout.xml
 * @author [Josie Matalski]
 * @version 1.0
 * @since [Sunday March 7 2021]
 */
package com.example.codecatchersapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * MapActivity is an implementation of the OnMapReadyCallback interface.
 * It is responsible for displaying a Google Map with markers on it.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    /**
     * The GoogleMap object used to display the map.
     */
    private GoogleMap mMap;
    /**
     * Called when the activity is starting.
     * Connects to the map_layout.xml and sets it as the content view.
     * Gets the intent and gets the SupportMapFragment for the map.
     * Registers this activity as the callback for when the map is ready.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect to  map_layout.xml
        setContentView(R.layout.map_layout);
        Intent intent = getIntent();
        // Get the SupportMapFragment for the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Called when the map is ready to be used.
     * Adds markers for five fixed locations and ten randomly generated locations.
     * Moves the camera to one of the random locations.
     *
     * @param googleMap The GoogleMap object used to display the map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Random random = new Random();

        LatLng location1 = new LatLng(37.7749, -122.4194);
        LatLng location2 = new LatLng(40.7128, -74.0060);
        LatLng location3 = new LatLng(51.5074, -0.1278);
        LatLng location4 = new LatLng(35.6895, 139.6917);
        LatLng location5 = new LatLng(-33.8688, 151.2093);

        mMap.addMarker(new MarkerOptions().position(location1).title("Location 1"));
        mMap.addMarker(new MarkerOptions().position(location2).title("Location 2"));
        mMap.addMarker(new MarkerOptions().position(location3).title("Location 3"));
        mMap.addMarker(new MarkerOptions().position(location4).title("Location 4"));
        mMap.addMarker(new MarkerOptions().position(location5).title("Location 5"));

        // Generate random latitude and longitude values
        for (int i = 0; i < 10; i++) {
            double latitude = random.nextDouble() * 180 - 90;
            double longitude = random.nextDouble() * 360 - 180;

            LatLng randomLocation = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(randomLocation).title("Random Location " + i));
        }

        // Move the camera to one of the random locations
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mMap.getCameraPosition().target));
    }
}





