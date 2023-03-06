package com.example.codecatchersapp;

import static android.content.ContentValues.TAG;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_STORAGE = 112;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster);

        // Initialize the Firebase Firestore instance
        db = FirebaseFirestore.getInstance();

        // Check if we have permission to write to external storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        } else {
            saveMonsterImage();
        }

        // Set up the "Draw Monster" button
        Button buttonDrawMonster = findViewById(R.id.button_draw_monster);
        buttonDrawMonster.setOnClickListener(v -> {
            ViewMonster viewMonster = findViewById(R.id.monster_canvas);
            viewMonster.drawMonster();
            Bitmap bitmap = Bitmap.createBitmap(viewMonster.getBitmap());
            saveBitmapToFile(bitmap);
        });

        // Set up the "Generate Monster" button
        ConstraintLayout layout = findViewById(R.id.monster_layout);
        Button button_Generate = findViewById(R.id.button_generate_monster);
        button_Generate.setOnClickListener(v->generateMonster(layout));
    }

    private void generateMonster(ConstraintLayout layout) {
        // Create a new ViewMonster object and add it to the layout
        ViewMonster viewMonster = new ViewMonster(this, null);
        layout.addView(viewMonster);


        // Get the canvas object from the ViewMonster
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth(), layout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        viewMonster.setmCanvas(canvas);

        // Create a new MonsterDB object and generate a random monster
        Paint paint = new Paint();
        MonsterDB monsterDB = new MonsterDB(canvas, paint);
        MonsterDB.generateRandomMonster(canvas, paint);

        // Save the monster features to Firebase Firestore
        saveMonsterToFirestore(monsterDB);
    }

    private void saveMonsterImage() {
        ViewMonster viewMonster = findViewById(R.id.monster_canvas);

        Paint paint = new Paint();
        MonsterDB.generateRandomMonster(viewMonster.getCanvas(), paint);

        Bitmap bitmap = Bitmap.createBitmap(viewMonster.getBitmap());
        saveBitmapToFile(bitmap);
    }

    private void saveBitmapToFile(Bitmap bitmap) {
        if (isExternalStorageWritable()) {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "my_monster.png");
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Log.e("MonsterActivity", "External storage is not writable");
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void saveMonsterToFirestore(MonsterDB monsterDB) {
        // Create a new document in the "monsters" collection with a random ID
        DocumentReference docRef = db.collection("monsters").document();

        // Create a map to store the monster features
        Map<String, Object> monster = new HashMap<>();
        monster.put("eyes", monsterDB.getEyes());
        monster.put("eyebrows", monsterDB.getEyebrows());
        monster.put("face", monsterDB.getFace());
        monster.put("nose", monsterDB.getNose());
        monster.put("mouth", monsterDB.getMouth());
        monster.put("ears", monsterDB.getEars());

        // Add the map to the document and listen for success/failure
        docRef.set(monster)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Monster saved to Firebase Firestore"))
                .addOnFailureListener(e -> Log.w(TAG, "Error saving monster to Firebase Firestore", e));
    }
}

