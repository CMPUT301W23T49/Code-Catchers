/**
 * The LeaderboardsActivity class contains methods for
 * operations that display a leaderboard
 * @author [Noah Silva]
 * @version 1.0
 * @since [Monday March 13 2023]
 */
package com.example.codecatchersapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * LeaderboardsActivity extends AppCompatActivity.
 * It is responsible for displaying leaderboards.xml.
 */
public class LeaderboardsActivity extends AppCompatActivity {
    FirebaseFirestore db;                                                     // The database
    private LeaderboardsArrayAdapter leaderboardsArrayAdapter;                // The adapter for the list view

    private FloatingActionButton backButton;                                  // Button to navigate back to the social menu
    private FloatingActionButton hamburgerMenuButton;                         // Button to display the hamburger menu
    private DialogFragment hamburgerMenuFragment;                             // The hamburger menu fragment
    private ListView leaderboardsList;                                        // The list view for the leaderboard
    private ArrayList<Leaderboards> dataList = new ArrayList<>();             // The list of leaderboard data
    private ArrayList<String> testScannedMonstersList = new ArrayList<>();    // The list of scanned monsters
    private Integer usersScore;                                               // The user's score
    private Button highestTotalScoreButton;                                   // Button to sort by highest total score
    private Button mostMonsters;                                              // Button to sort by most monsters
    private Button highestIndividualMonsterButton;                            // Button to sort by highest individual monster score

    // TODO: Sort highest->lowest. Sort by most unique monsters
    /**
     * Called when the activity is starting.
     * Connects to the leaderboards.xml and sets it as the content view.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {                     // Called when the activity is starting
        super.onCreate(savedInstanceState);                                  // Call the superclass onCreate method
        setContentView(R.layout.leaderboards);                               // Set the content view to leaderboards.xml
        db = FirebaseFirestore.getInstance();                                // Get the database instance
        dataList = new ArrayList<>();                                        // Initialize the dataList

        leaderboardsList = findViewById(R.id.leaderboard_list);             // Get the list view
        leaderboardsArrayAdapter = new LeaderboardsArrayAdapter(this, dataList); // Initialize the adapter
        leaderboardsList.setAdapter(leaderboardsArrayAdapter);             // Set the adapter for the list view
        backButton = findViewById(R.id.back_button);                       // Get the back button
        hamburgerMenuButton = findViewById(R.id.hamburger_menu);           // Get the hamburger menu button
        highestTotalScoreButton = findViewById(R.id.totalScore);           // Get the highest total score button
        mostMonsters = findViewById(R.id.mostMonsters);                    // Get the most monsters button
        highestIndividualMonsterButton = findViewById(R.id.highestScoringMonster); // Get the highest individual monster button


        displayLeaderboard("totalscore");                          // Display the leaderboard sorted by total score


        backButton.setOnClickListener(new View.OnClickListener() {          // Set click listener for back button
            /**
             * Navigates back to the SocialMenuActivity when clicked.
             * @param view the clicked view.
             */
            @Override
            public void onClick(View view) {                              // When the back button is clicked
                Intent socialMenuIntent = new Intent(LeaderboardsActivity.this, SocialMenuActivity.class); // Create an intent to navigate to the SocialMenuActivity
                startActivity(socialMenuIntent);                          // Start the intent
            }
        });

        hamburgerMenuButton.setOnClickListener(new View.OnClickListener() { // Set click listener for hamburger menu button
            /**
             * Shows the hamburger menu fragment when clicked
             * @param view the clicked view.
             */
            @Override
            public void onClick(View view) {

                hamburgerMenuFragment =  new HamburgerMenuFragment(R.id.leaderboards);            // Create a new hamburger menu fragment
                hamburgerMenuFragment.show(getSupportFragmentManager(), "HamburgerFragment"); // Show the hamburger menu fragment


            }
        });

        highestTotalScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLeaderboard("totalscore");
            }
        });

        mostMonsters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLeaderboard("monstercount");
            }
        });

        highestIndividualMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLeaderboard("highestmonsterscore");
            }
        });
    }

    private void displayLeaderboard(String fieldName){                                                   // Displays the leaderboard sorted by the given field name
        leaderboardsArrayAdapter.clear();                                                                // Clear the adapter
        CollectionReference collectionReference = db.collection("PlayerDB/");               // Get the collection reference
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {          // Get the collection
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {                                // If the collection is successfully retrieved
                List<String> userIDsList = new ArrayList<>();                                            // Create a list of user IDs
                List<String> listOfMonsters = new ArrayList<>();                                         // Create a list of monsters
                List<Leaderboards> listOfLeaderboardEntries = new ArrayList<>();                         // Create a list of leaderboard entries

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {                  // For each document in the collection
                    String userID = documentSnapshot.getId();                                            // Get the user ID

                    String username = documentSnapshot.getString("username");                       // Get the username
                    Log.e("Success", "USERNAME: " + username);                                 // Log the username

                    Map<String, Object> usersData = documentSnapshot.getData();                          // Get the user's data

                    System.out.println("\n");                                                            // Print a new line

                    // Iterates through every field for every userID
                    for (String key : usersData.keySet()) {                                              // key can be userName, contactInfo, or scannedMonsters.
                        ArrayList<String> testScannedMonstersList = new ArrayList<>();                   // Create a list of scanned monsters
                        String temporaryScore;                                                           // Create a temporary score
                        Integer totalScore = 0;                                                          // Create a total score

                        // Get user's total score
                        if (key.compareTo(fieldName) == 0) {                                             // If the key is the field name
                            System.out.println("User "+ username + " has a score " + usersData.get(key) ); // Print the user's score
                            usersScore = Integer.parseInt((String)usersData.get(key));                   // Get the user's score

                            System.out.println();

                            listOfLeaderboardEntries.add(new Leaderboards(username, usersData.get(key).toString())); // Add the user's score to the leaderboard entries list
                        }
                    }
                }

                Collections.sort(listOfLeaderboardEntries);                                           // Sort the list of leaderboard entries by their score
                for(Leaderboards item:listOfLeaderboardEntries){                                      // For each leaderboard entry
                    leaderboardsArrayAdapter.add(item);                                               // Add the leaderboard entry to the adapter
                }

                leaderboardsArrayAdapter.notifyDataSetChanged();                                     // Notify the adapter that the data set has changed
            }
        }).addOnFailureListener(new OnFailureListener() {                                             // If the collection is not successfully retrieved
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Failure", "Error getting documents: ", e);
            }
        });
    }


}
