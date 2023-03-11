package com.example.codecatchersapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class SocialMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_menu);
        Button browse_users = findViewById(R.id.browse_users_button);
        Button browse_qr_codes = findViewById(R.id.browse_QR_codes_button);
        Button leaderboards = findViewById(R.id.leaderboards_button);
        Button my_profile = findViewById(R.id.my_profile_button);

        browse_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.search_users);
            }
        });

        browse_qr_codes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.search_qr_codes);
            }
        });

        leaderboards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.leaderboards);
            }
        });

        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.my_profile);
            }
        });
    }
}
