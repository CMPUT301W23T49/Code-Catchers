package com.example.codecatchersapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyProfileActivity extends AppCompatActivity implements MonsterAdapter.ItemClickListener{
    private UserAccount user;
    private String deviceID;
    private TextView userName;
    private TextView userScore;
    private TextView userNumMonsters;
    private RecyclerView rv_monsters;
    private Button sortButton;
    private FloatingActionButton backButton;
    private FloatingActionButton settingsButton;
    private ArrayList<Monster> monsters;
    private MonsterAdapter monsterAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userCollection = db.collection("PlayerDB");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view
        setContentView(R.layout.my_profile);

        // Get the views for MyProfileActivity
        userName = findViewById(R.id.user_name_label);
        userScore = findViewById(R.id.user_score);
        userNumMonsters = findViewById(R.id.num_monster);
        rv_monsters = findViewById(R.id.user_monster_rv);
        sortButton = findViewById(R.id.sort_button);
        backButton = findViewById(R.id.back_button);
        settingsButton = findViewById(R.id.user_settings);

        // Get deviceID
        deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // Set click listener for back button

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        // Get the users scanned monsters from the database
        monsters = new ArrayList<>();

        // Get the user info by deviceID
        userCollection.whereEqualTo("deviceID", deviceID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                            Log.i("User Info", doc.toString());
                            String name = (String) doc.get("username");
                            String contact = (String) doc.get("contactInfo");
                            this.user = new UserAccount(name, contact, deviceID);
                            // Get the monster hashes from the user
                            ArrayList<Object> tempList = new ArrayList<>();
                            tempList = (ArrayList<Object>) doc.get("scannedMonsters");

                            if (tempList != null) {
                                // Add the monster hashes to the monster list
                                for (Object hash : tempList) {
                                    Monster currMonster = new Monster(hash.toString());
                                    monsters.add(currMonster);
                                    System.out.println("Monster score: " + currMonster.getMonsterScore());
                                }

                                Log.i("TAG", "Length of monsters: " + monsters.size());
                                Collections.sort(monsters, new Comparator<Monster>() {
                                    @Override
                                    public int compare(Monster monster1, Monster monster2) {
                                        return monster2.getMonsterScore().compareTo(monster1.getMonsterScore());

                                    }

                                });
                            }
                            // Calculate the user's total score from their scanned monsters
                            Integer tempScore = 0;
                            for (Monster monster : monsters) {
                                tempScore += Integer.parseInt(monster.getMonsterScore());

                            }

                            // Set the TextViews and RecyclerView for the user's profile
                            userScore.setText(tempScore.toString());
                            userNumMonsters.setText(String.valueOf(monsters.size()));
                            rv_monsters.setLayoutManager(new GridLayoutManager(this, 2));
                            monsterAdapter = new MonsterAdapter(monsters);
                            monsterAdapter.setClickListener(this);
                            rv_monsters.setAdapter(monsterAdapter);
                            userName.setText(user.getUsername());
                            sortButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (sortButton.getText().toString().contains("highest")) {
                                        sortButton.setText("Sort by: lowest");
                                        Collections.sort(monsters, new Comparator<Monster>() {
                                            @Override
                                            public int compare(Monster monster1, Monster monster2) {
                                                return monster1.getMonsterScore().compareTo(monster2.getMonsterScore());
                                            }
                                        });

                                    } else {
                                        sortButton.setText("Sort by: highest");
                                        Collections.sort(monsters, new Comparator<Monster>() {
                                            @Override
                                            public int compare(Monster monster1, Monster monster2) {
                                                return monster2.getMonsterScore().compareTo(monster1.getMonsterScore());

                                            }

                                        });

                                    }
                                    monsterAdapter.notifyDataSetChanged();
                                }
                            });
                            settingsButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent userSettingsIntent = new Intent(MyProfileActivity.this, UserSettingsActivity.class);
                                    userSettingsIntent.putExtra("userName", user.getUsername());
                                    userSettingsIntent.putExtra("contactInfo", user.getContactInfo());
                                    userSettingsIntent.putExtra("deviceID", deviceID);
                                    startActivity(userSettingsIntent);
                                }
                            });
                        }

                    }
                });

    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("OnItemClick", "In onItemClick");
        // Get the selected monster from the adapter
        Monster monster = monsterAdapter.getMonster(position);

        // Create a new intent for the ViewMinProfile activity and pass the user and monster's info to it
        Intent monsterIntent = new Intent(MyProfileActivity.this, MyMonsterProfile.class);
        monsterIntent.putExtra("userName", user.getUsername());
        monsterIntent.putExtra("deviceID", deviceID);
        monsterIntent.putExtra("monsterHash", monster.getMonsterHash());
        monsterIntent.putExtra("monsterName", monster.getMonsterName());
        monsterIntent.putExtra("monsterScore", monster.getMonsterScore());
        startActivity(monsterIntent);

    }
}
