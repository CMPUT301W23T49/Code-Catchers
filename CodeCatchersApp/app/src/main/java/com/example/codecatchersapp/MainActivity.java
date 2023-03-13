/**
 MainActivity is the starting activity of the app. It launches the {@link UserAccountActivity}.
 The activity also includes the functionality for generating and writing hashes for QR codes in batches.
 The batch writes are done with exponential backoff in case of errors.
 @author [Noah, Zhashe, Kyle, and Josie]
 @version 1.0
 @since [Thurs Feb 23]
 */
package com.example.codecatchersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.codecatchersapp.HashGenerator;
import com.example.codecatchersapp.UserAccountActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.NoSuchAlgorithmException;

import retrofit2.http.HEAD;

public class MainActivity extends AppCompatActivity {
    protected String qrCode;
    //FirebaseFirestore db;
    /**
     * The maximum number of batches to write.
     * This value is set to 50 to avoid the "exceeded rate limits" error.
     */
    private static final int MAX_BATCH_COUNT = 50; // maximum number of batches to send

    private HashGenerator hashGenerator;

    private final Handler handler = new Handler();

    private int currentBackoffTime = 0; // initially set to 0 to start the write operation immediately

    private final int MAX_BACKOFF_TIME = 5000; // 5 seconds

    // Track the number of batches written
    private int batchCount = 0;
    /**
     * The maximum number of documents to write in a batch.
     * This value is set to 500 to avoid the "exceeded rate limits" error.
     */
    public static final int BATCH_SIZE = 3; // maximum number of documents to write in a batch

    private final int INITIAL_BACKOFF_TIME = 500; // 0.5 seconds
    private final int BACKOFF_MULTIPLIER = 2;

    // Declare the Runnable object
    private final Runnable myRunnable = new Runnable() {
        /**
         * This method is called when the activity is first created.
         * It sets the content view to the QR actions layout, finds views by their IDs, and sets click listeners for the buttons.
         *
         * @param savedInstanceState  The saved instance state.
         */
        @Override
        public void run() {
            try {
                hashGenerator.generateAndWriteHash("test qr code");

                Log.d("Firestore", "Batch write successful.");

                // Reset the backoff time
                currentBackoffTime = 0;

                // Increment the batch count
                batchCount++;

                // Create a new batch write operation if the batch count is less than the maximum batch count
                if (batchCount < MAX_BATCH_COUNT) {
                    // Commit the new batch write operation after a delay
                    handler.postDelayed(myRunnable, currentBackoffTime);
                }
            } catch (NoSuchAlgorithmException e) {
                Log.e("Firestore", "Error generating hash.", e);
                // Increment the backoff time
                currentBackoffTime = Math.min(currentBackoffTime * BACKOFF_MULTIPLIER, MAX_BACKOFF_TIME);

                // Commit the batch write operation after a delay
                handler.postDelayed(myRunnable, currentBackoffTime);
            }
        }
    };
    /**
     * This method is called when the activity is first created.
     * It sets the content view to the QR actions layout, finds views by their IDs, and sets click listeners for the buttons.
     *
     * @param savedInstanceState  The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.map_layout);

        Intent userAccountIntent = new Intent(this, UserAccountActivity.class);
        startActivity(userAccountIntent);



        hashGenerator = new HashGenerator();

        // Start the write operation with a delay
        currentBackoffTime = INITIAL_BACKOFF_TIME;
        handler.postDelayed(myRunnable, currentBackoffTime);
    }
}
