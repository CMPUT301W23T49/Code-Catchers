package com.example.codecatchersapp;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeaderboardsActivity extends AppCompatActivity {
    FirebaseFirestore db;
    private LeaderboardsArrayAdapter leaderboardsArrayAdapter;
    private ListView leaderboardsList;
    private ArrayList<Leaderboards> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboards);
        db = FirebaseFirestore.getInstance();
        dataList = new ArrayList<>();

        leaderboardsList = findViewById(R.id.leaderboard_list);
        leaderboardsArrayAdapter = new LeaderboardsArrayAdapter(this, dataList);
        leaderboardsList.setAdapter(leaderboardsArrayAdapter);

        // TODO: Change from just someUserID1 to only "PlayerDB"
        CollectionReference collectionReference = db.collection("PlayerDB/");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> userIDsList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String userID = documentSnapshot.getId();
                    // String:Object, as fields can contain a string, number, or list of Strings
                    Map<String, Object> usersData = documentSnapshot.getData();
                    for (String key : usersData.keySet()) {
                        Log.e("Success", "Collection key" + key + ": " + usersData.get(key));
                        Leaderboards tempLeaderboards = new Leaderboards(userID, key);
                        leaderboardsArrayAdapter.add(tempLeaderboards);
                    }
                    Log.e("Success", "GOT DATA " + userID);

                    userIDsList.add(userID);
                }
                System.out.println("----------------------------" + userIDsList);

                // Call a method to display the list in a ListView
                //displayList(dataList);

                // Update leaderboards adapter to display users
                leaderboardsArrayAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Failure", "Error getting documents: ", e);
            }
        });
    }
    /*
    private void displayList(List<String> dataList) {
        ListView listView = findViewById(R.id.list_view);
        MyListAdapter adapter = new MyListAdapter(this, dataList);
        listView.setAdapter(adapter);
    }
     */
}
