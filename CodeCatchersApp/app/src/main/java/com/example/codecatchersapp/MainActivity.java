package com.example.codecatchersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class represents the MainActivity that initializes the Firebase Firestore and checks if the device ID exists
 * in the PlayerDB collection. If it does, it starts the MainMenuActivity, otherwise it starts the UserAccountActivity.
 * It also runs a batch write operation using a Handler and Runnable with exponential backoff and maximum batch count.
 * The batch write operation generates and writes a hash using the HashGenerator class.
 */
public class MainActivity extends AppCompatActivity {
    protected String qrCode;

    private static final int MAX_BATCH_COUNT = 50; // maximum number of batches to send
    private HashGenerator hashGenerator;

    private final Handler handler = new Handler();

    private int currentBackoffTime = 0; // initially set to 0 to start the write operation immediately

    private final int MAX_BACKOFF_TIME = 5000; // 5 seconds

    // Track the number of batches written
    private int batchCount = 0;

    public static final int BATCH_SIZE = 3; // maximum number of documents to write in a batch

    private final int INITIAL_BACKOFF_TIME = 500; // 0.5 seconds
    private final int BACKOFF_MULTIPLIER = 2;

    // Declare the Runnable object
    private final Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            HashGenerator.generateAndWriteHash("test qr code");
        }


    };

    /**
     * Initializes the Firebase Firestore and checks if the device ID exists in the PlayerDB collection.
     * If it does, it starts the MainMenuActivity, otherwise it starts the UserAccountActivity.
     * It also initializes the HashGenerator and starts the batch write operation with a delay using a Handler
     * and Runnable with exponential backoff and maximum batch count.
     * @param savedInstanceState the saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Checks if device is registered already or not
        String deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("PlayerDB")
                .whereEqualTo("deviceID", deviceID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // device ID exists in database
                            Intent intent = new Intent(this, MainMenuActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(this, UserAccountActivity.class);
                            startActivity(intent);
                        }
                    }
                });

        hashGenerator = new HashGenerator();

        // Start the write operation with a delay
        currentBackoffTime = INITIAL_BACKOFF_TIME;
        handler.postDelayed(myRunnable, currentBackoffTime);
    }

}
