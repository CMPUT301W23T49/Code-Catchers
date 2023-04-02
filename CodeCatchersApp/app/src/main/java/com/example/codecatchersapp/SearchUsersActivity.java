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
import androidx.fragment.app.DialogFragment;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents the Search Users Activity in the Code Catchers app. SearchUsersActivity
 * allows the user to search for other users by typing in their username into a search bar.
 * The activity also displays a list of all users in the app and filters the list as the user types
 * into the search bar.
 */
public class SearchUsersActivity extends AppCompatActivity implements UserAdapter.ItemClickListener {

    private SearchView searchView;
    private RecyclerView rvUsers;
    private FloatingActionButton backButton;
    private FloatingActionButton hamburgerMenuButton;
    private DialogFragment hamburgerMenuFragment;
    private List<UserAccount> users;
    private List<UserAccount> searchedUsers;
    private UserAdapter userAdapter;
    FirebaseFirestore db;
    CollectionReference userCollection;

    /**
     * Called when the activity is starting or restarting. It initializes the Firebase instance
     * and sets up the views, adapters, and listeners for the SearchUsersActivity.
     * @param savedInstanceState the bundle containing the saved state of the activity
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view
        setContentView(R.layout.search_users);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        userCollection = db.collection("PlayerDB");

        // Get the views for for searchUsersActivity and set the layout manager for the RecyclerView
        searchView = findViewById(R.id.search_view);
        searchView.clearFocus();
        backButton = findViewById(R.id.back_button);
        hamburgerMenuButton = findViewById(R.id.hamburger_menu);

        rvUsers = findViewById(R.id.rv_users);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        // Set click listener for back button and hamburger menu button
        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Navigates back to the SocialMenuActivity when clicked.
             * @param view the clicked view.
             */
            @Override
            public void onClick(View view) {
                Intent socialMenuIntent = new Intent(SearchUsersActivity.this, SocialMenuActivity.class);
                startActivity(socialMenuIntent);
            }
        });

        hamburgerMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hamburgerMenuFragment =  new HamburgerMenuFragment(R.id.search_users);
                hamburgerMenuFragment.show(getSupportFragmentManager(), "HamburgerFragment");


            }
        });

        // Set query listeners
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /**
             * Called when the user submits the search query. Filters the user list with the user's query.
             * @param text the search query entered by the user
             * @return false if the query is submitted successfully
             */
            @Override
            public boolean onQueryTextSubmit(String text) {
                filterUsers(text);
                return false;
            }

            /**
             * Called when the user changes the search query. Filters the user list with the user's query.
             * @param newText the new search query entered by the user
             * @return true if the query is changed successfully
             */
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

        // Get the users stored in the DB and add them to the list of users
        Query query = userCollection.orderBy("username");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()) {
                    users.add(new UserAccount(doc.getString("username"), doc.getString("contactInfo")));
                }
                // Set the searched users equal to the found users
                searchedUsers = users;
                // Set the user adapter with the users from the database
                userAdapter = new UserAdapter((ArrayList<UserAccount>) searchedUsers);
                userAdapter.setClickListener(SearchUsersActivity.this);
                rvUsers.setAdapter(userAdapter);
                Log.i("TAG", "In onCreate");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SearchUsersActivity.this, "Failed to load users from database", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * Filters the user's search query by checking each user's username for a match.
     * If the search query is not empty, searchedUsers is set to a new ArrayList and all the usernames
     * that match the query are added to the list and the userAdapter is updated to reflect this filtered list.
     * Otherwise, the userAdapter is set to the list of all users
     * @param text the user's search query
     */
    private void filterUsers(String text) {

        // Check if the text is empty
        if (!text.isEmpty()) {

            // Create a new ArrayList for the filtered users
            searchedUsers = new ArrayList<>();

            for (UserAccount user : users) {
                // Check for match and add user to the filtered list
                if (user.getUsername().toLowerCase().contains(text.toLowerCase())) {
                    searchedUsers.add(user);
                }
            }

            // Update the userAdapter if the searchedUsers list is not empty
            if (!searchedUsers.isEmpty()) {
                userAdapter.setFilterList(searchedUsers);
            }
        } else {
            // Otherwise update the userAdapter to the users list
            userAdapter.setFilterList(users);
        }
    }

    /**
     * Overrides the onBackPressed method to handle back navigation. If there are fragments in the back stack,
     * it pops the top one. Otherwise, it calls the super method.
     */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    /**
     * Handles the onItemClick event for the userAdapter. Ensures that the keyboard is hidden.
     * It then creates a new UserProfileFragment and passes the selected UserAccount to it.
     * Replaces the current fragment with the new UserProfileFragment and adds it to the back stack.
     * @param view the clicked view
     * @param position the position of the clicked view in the adapter
     */
    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "User account " + userAdapter.getUser(position).getUsername() + " has been clicked");

        // Hide the keyboard before moving to UserProfileFragment
        InputMethodManager inputMethodManager = (InputMethodManager) SearchUsersActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (SearchUsersActivity.this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(SearchUsersActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        // Move to the UserProfileFragment and pass the selected UserAccount to it
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment profileFragment = new UserProfileFragment(userAdapter.getUser(position));
        fragmentManager.beginTransaction()
                .replace(R.id.search_users, profileFragment)
                .addToBackStack(null)
                .commit();
    }
}
