package com.example.codecatchersapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchUsersActivity extends AppCompatActivity implements UserAdapter.ItemClickListener {

    private SearchView searchView;
    private RecyclerView rvUsers;
    private FloatingActionButton backButton;
    private List<UserAccount> users;
    private List<UserAccount> searchedUsers;
    private UserAdapter userAdapter;


    FirebaseFirestore db;
    CollectionReference userCollection;
    Map<String, ArrayList<String>> scannedMonsters;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view
        setContentView(R.layout.search_users);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        userCollection = db.collection("PlayerDB");


        // Get the view for the searchbar
        searchView = findViewById(R.id.search_view);
        searchView.clearFocus();

        // Get the view for the back button
        backButton = findViewById(R.id.back_button);

        // Set click listener for back button

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent socialMenuIntent = new Intent(SearchUsersActivity.this, SocialMenuActivity.class);
                startActivity(socialMenuIntent);
            }
        });

        // Set query listeners
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                filterUsers(text);
                return false;
            }


            // Filter the user list with the user's query
            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return true;


            }
        });

        // Create an ArrayList for users
        users = new ArrayList<>();

        // Create an ArrayList for the searched users
        searchedUsers = new ArrayList<>();

        final Map<String, ArrayList<String>> scannedMonsters = new HashMap<>();

        // Get the users stored in the DB and add them to the list of users
        Query query = userCollection.orderBy("userName");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()) {
                    if (doc.exists()) {
                        String userName = doc.getString("userName");
                        String contactInfo = doc.getString("contactInfo");
                        users.add(new UserAccount(userName, contactInfo));
                        ArrayList<String> monsterList = (ArrayList<String>) doc.get("scannedMonsters");
                        if (!monsterList.isEmpty()) {
                            scannedMonsters.put(userName, monsterList);
                        } else {
                            scannedMonsters.put(userName, new ArrayList<>());
                        }
                    }

                }
            }
        });
        // Set up the RecycleView with UserAdapter and ClickListener
        rvUsers = findViewById(R.id.rv_users);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter((ArrayList<UserAccount>) searchedUsers);
        userAdapter.setClickListener(this);
        rvUsers.setAdapter(userAdapter);

        // Set query if the user is navigating back from the UserProfileFragment
        Intent intent = getIntent();
        CharSequence prevQuery = intent.getCharSequenceExtra("PreviousQuery");
        if (prevQuery != null) {
            searchView.setQuery(prevQuery, true);
        }


    }


    // Filters the user's search query
    private void filterUsers(String text) {

        // Check if the text is empty
        if (!text.isEmpty()) {

            // Create a new ArrayList for the filtered users
            List<UserAccount> filteredUsers = new ArrayList<>();

            for (UserAccount user : users) {
                // Check for match and add user to the filtered list
                if (user.getUsername().toLowerCase().contains(text.toLowerCase())) {
                    filteredUsers.add(user);
                }
            }



            // Update the userAdapter if the filtered list is not empty
            if (!filteredUsers.isEmpty()) {
                userAdapter.setFilterList(filteredUsers);
            }
            else {
                // Otherwise clear the list and display toast message
                userAdapter.setFilterList(new ArrayList<>());
            }
        }
    }

    // onItemClick function for the usernames
    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "User account " + userAdapter.getUser(position).getUsername() + " has been clicked");

        // Hide the keyboard before moving to UserProfileFragment
        InputMethodManager inputMethodManager = (InputMethodManager) SearchUsersActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(SearchUsersActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        // Move to the UserProfileFragment and pass the selected UserAccount and search query to it
        FragmentManager fragmentManager = getSupportFragmentManager();
        UserAccount user = userAdapter.getUser(position);
        ArrayList<String> monsters = scannedMonsters != null ? scannedMonsters.getOrDefault(user.getUsername(), new ArrayList<>()) : new ArrayList<>();
        Intent intent = new Intent(SearchUsersActivity.this, UserProfileActivity.class);
        intent.putExtra("UserAccount", user);
        intent.putExtra("Query", searchView.getQuery());
        intent.putExtra("Monsters", monsters);
        startActivity(intent);

    }
}
