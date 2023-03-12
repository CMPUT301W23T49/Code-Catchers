package com.example.codecatchersapp;

<<<<<<< HEAD
import android.app.Activity;
=======

import android.app.Activity;
import android.content.Intent;
>>>>>>> 6e01edc2621732b2d4a5e306fccc7c3533163059
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

<<<<<<< HEAD
public class MainMenuActivity extends Activity {
=======
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.auth.User;

public class MainMenuActivity extends AppCompatActivity {
>>>>>>> 6e01edc2621732b2d4a5e306fccc7c3533163059

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
<<<<<<< HEAD
=======
        Intent intent = getIntent();
>>>>>>> 6e01edc2621732b2d4a5e306fccc7c3533163059

        Button scanQrButton = findViewById(R.id.scan_qr_button);
        Button socialButton = findViewById(R.id.social_button);
        Button mapButton = findViewById(R.id.map_button);

        scanQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                // TODO: Implement scan QR code functionality
=======
                Intent scannerIntent = new Intent(MainMenuActivity.this, ScannerActivity.class);
                startActivity(scannerIntent);
>>>>>>> 6e01edc2621732b2d4a5e306fccc7c3533163059
            }
        });

        socialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement social functionality
<<<<<<< HEAD
=======
                Intent socialIntent = new Intent(MainMenuActivity.this, SocialMenuActivity.class);
                startActivity(socialIntent);
>>>>>>> 6e01edc2621732b2d4a5e306fccc7c3533163059
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement map functionality
            }
        });
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 6e01edc2621732b2d4a5e306fccc7c3533163059
