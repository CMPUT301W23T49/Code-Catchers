/**
 * The LeaderboardsActivity class contains methods for
 * operations that display a leaderboard
 * @author [Noah Silva]
 * @version 1.0
 * @since [Monday March 13 2023]
 */
package com.example.codecatchersapp;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private ListView leaderboardsList;
    private ArrayList<Leaderboards> dataList = new ArrayList<>();
    private ArrayList<String> testScannedMonstersList = new ArrayList<>();

    // TODO: Sort highest->lowest. Sort by most unique monsters
    /**
     * Called when the activity is starting.
     * Connects to the leaderboards.xml and sets it as the content view.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboards);
        db = FirebaseFirestore.getInstance();
        dataList = new ArrayList<>();

        leaderboardsList = findViewById(R.id.leaderboard_list);
        leaderboardsArrayAdapter = new LeaderboardsArrayAdapter(this, dataList);
        leaderboardsList.setAdapter(leaderboardsArrayAdapter);

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
                    String username = documentSnapshot.getString("userName");
                    //Log.e("Success", "USERNAME: " + username);

                    // String:Object, as fields can contain a string, number, or list of Strings
                    Map<String, Object> usersData = documentSnapshot.getData();

                    System.out.println("\n");

                    // Iterates through every field for every userID
                    for (String key : usersData.keySet()) {
                        // key can be userName, contactInfo, or scannedMonsters.
                         ArrayList<String> testScannedMonstersList = new ArrayList<>();
                        String temporaryScore;
                        Integer totalScore = 0;

                        // Get all monster IDs of monsters scanned from userID
                        if (key.compareTo("scannedMonsters") == 0) {
                            // When in this if statement, usersData.get(key) is an array containing all monsters collected by user
                            testScannedMonstersList = (ArrayList<String>) usersData.get(key);

                            System.out.println("User "+ username + " has a field '" + key + "' that contains the arraylist of monsterIDs: " + usersData.get(key));
                            System.out.print("List of monster IDs: ");

                            // Iterate through all monster IDs
                            for (String currMonsterID : testScannedMonstersList){
                                System.out.print(currMonsterID + ", ");

                                try {
                                    Score score = new Score(currMonsterID);
                                    temporaryScore = score.getScore();
                                    totalScore += Integer.parseInt(temporaryScore);
                                } catch (NoSuchAlgorithmException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                            System.out.println();

                            listOfLeaderboardEntries.add(new Leaderboards(username, totalScore.toString()));
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
