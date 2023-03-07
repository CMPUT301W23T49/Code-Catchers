package com.example.codecatchersapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class SearchUsersActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView rvUsers;
    private List<UserAccount> users;
    private List<UserAccount> searchedUsers;
    private UserAdapter userAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view
        setContentView(R.layout.search_users);

        // Get the view for the searchbar
        searchView = findViewById(R.id.search_view);
        searchView.clearFocus();

        // Set query listeners
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            // Filter the user list with the user's query
            // TODO: Replace with firebase queries
            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return true;
            }
        });

        // Temporary:
        // Create an ArrayList of users (for testing purposes)
        users = new ArrayList<>();
        users.add(new UserAccount("User123", "1234"));
        users.add(new UserAccount("CoolUser537", "5672"));
        users.add(new UserAccount("CodeCatcher4Ever", "6272829"));
        users.add(new UserAccount("Pikachu", "6527288929"));

        // Create an ArrayList for the searched users
        searchedUsers = new ArrayList<>();

        // Set up the RecycleView with the UserAdapter
        rvUsers = findViewById(R.id.rv_users);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter((ArrayList<UserAccount>) searchedUsers);
        rvUsers.setAdapter(userAdapter);

    }


    // Temporary:
    // Filters the user's search query
    private void filterUsers(String text) {

        // Check if the text is empty
        if (!text.isEmpty()) {

            // Create a new ArrayList for the filtered users
            List<UserAccount> filteredUsers = new ArrayList<>();

            // Loop through the test ArrayList of users
            for (UserAccount user : users) {
                // Check for match and add user to the filtered list
                if (user.getUsername().toLowerCase().contains(text.toLowerCase())) {
                    filteredUsers.add(user);
                }
            }

            // Update the userAdapter if the filtered list is not empty
            if (!filteredUsers.isEmpty()) {
                userAdapter.setFilterList(filteredUsers);
            } else {
                // Otherwise clear the list and display toast message
                userAdapter.setFilterList(new ArrayList<>());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchUsersActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
                    }
                }, 200);
            }
        }
    }


}
