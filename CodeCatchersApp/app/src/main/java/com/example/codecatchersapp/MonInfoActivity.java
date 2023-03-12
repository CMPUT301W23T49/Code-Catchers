package com.example.codecatchersapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

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

        Switch geolocationToggle = findViewById(R.id.geolocation_switch);
        Switch locationPhotoToggle = findViewById(R.id.photo_switch);

        Button continueMonSettings = findViewById(R.id.continue_photo_button);
        continueMonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference collectionReference = db.collection("PlayerDB/someUserID1/Monsters/someMonsterID/comment");
                // TODO: ADD COMMENT TO DATABASE
                final String ogComment = commentEditText.getText().toString();
                HashMap<String,String> data = new HashMap<>();
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
                // TODO: IF TRUE, RECORD LOCATION


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
        Intent intent = new Intent(MonInfoActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
