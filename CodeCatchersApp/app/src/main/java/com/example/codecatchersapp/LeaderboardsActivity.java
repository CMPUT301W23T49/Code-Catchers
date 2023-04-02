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
import android.provider.Settings;
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

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * LeaderboardsActivity extends AppCompatActivity.
 * It is responsible for displaying leaderboards.xml.
 */
public class LeaderboardsActivity extends AppCompatActivity {
    FirebaseFirestore db;
    private LeaderboardsArrayAdapter leaderboardsArrayAdapter;

    private FloatingActionButton backButton;
    private FloatingActionButton hamburgerMenuButton;
    private DialogFragment hamburgerMenuFragment;
    private ListView leaderboardsList;
    private ArrayList<Leaderboards> dataList = new ArrayList<>();
    private ArrayList<String> testScannedMonstersList = new ArrayList<>();
    private Integer usersScore;
    private Button highestTotalScoreButton;
    private Button mostMonsters;
    private Button highestIndividualMonsterButton;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboards);
        db = FirebaseFirestore.getInstance();
        dataList = new ArrayList<>();

        leaderboardsList = findViewById(R.id.leaderboard_list);
        leaderboardsArrayAdapter = new LeaderboardsArrayAdapter(this, dataList);
        leaderboardsList.setAdapter(leaderboardsArrayAdapter);
        backButton = findViewById(R.id.back_button);
        hamburgerMenuButton = findViewById(R.id.hamburger_menu);
        highestTotalScoreButton = findViewById(R.id.totalScore);
        mostMonsters = findViewById(R.id.mostMonsters);
        highestIndividualMonsterButton = findViewById(R.id.highestScoringMonster);

        // Set click listener for back button and hamburger menu button
        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Navigates back to the SocialMenuActivity when clicked.
             * @param view the clicked view.
             */
            @Override
            public void onClick(View view) {
                Intent socialMenuIntent = new Intent(LeaderboardsActivity.this, SocialMenuActivity.class);
                startActivity(socialMenuIntent);
            }
        });

        hamburgerMenuButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Shows the hamburger menu fragment when clicked
             * @param view the clicked view.
             */
            @Override
            public void onClick(View view) {

                hamburgerMenuFragment =  new HamburgerMenuFragment(R.id.leaderboards);
                hamburgerMenuFragment.show(getSupportFragmentManager(), "HamburgerFragment");


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

    private void displayLeaderboard(String fieldName){
        leaderboardsArrayAdapter.clear();
        CollectionReference collectionReference = db.collection("PlayerDB/");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> userIDsList = new ArrayList<>();
                List<String> listOfMonsters = new ArrayList<>();
                List<Leaderboards> listOfLeaderboardEntries = new ArrayList<>();

                // Iterates through every userID in PlayerDB collection
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String userID = documentSnapshot.getId();

                    String username = documentSnapshot.getString("username");
                    Log.e("Success", "USERNAME: " + username);

                    // String:Object, as fields can contain a string, number, or list of Strings
                    Map<String, Object> usersData = documentSnapshot.getData();

                    System.out.println("\n");

                    // Iterates through every field for every userID
                    for (String key : usersData.keySet()) {
                        // key can be userName, contactInfo, or scannedMonsters.
                        ArrayList<String> testScannedMonstersList = new ArrayList<>();
                        String temporaryScore;
                        Integer totalScore = 0;

                        // Get user's total score
                        if (key.compareTo(fieldName) == 0) {
                            System.out.println("User "+ username + " has a score " + usersData.get(key) );
                            usersScore = Integer.parseInt((String)usersData.get(key));

                            System.out.println();

                            listOfLeaderboardEntries.add(new Leaderboards(username, usersData.get(key).toString()));
                        }
                    }
                }

                // Sort the list of leaderboard entries by their score
                Collections.sort(listOfLeaderboardEntries);
                for(Leaderboards item:listOfLeaderboardEntries){
                    leaderboardsArrayAdapter.add(item);
                }
                // Update leaderboards adapter to display users and their scores
                leaderboardsArrayAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Failure", "Error getting documents: ", e);
            }
        });
    }


}
