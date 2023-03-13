package com.example.codecatchersapp;


import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class UserAdapterTest {

    private UserAdapter userAdapter;
    private ArrayList<UserAccount> userList;

    @Before
    public void setUp() {
        // Create a sample list of UserAccounts
        userList = new ArrayList<>();
        userList.add(new UserAccount("user1", "user1@example.com"));
        userList.add(new UserAccount("user2", "user2@example.com"));

        // Create a new UserAdapter with the sample list of UserAccounts
        userAdapter = new UserAdapter(userList);
    }

    @Test
    public void testGetItemCount() {
        // Assert that getItemCount() returns the correct number of items in the list
        Assert.assertEquals(userList.size(), userAdapter.getItemCount());
    }

    @Test
    public void testOnCreateViewHolder() {
        // Get the context from the ApplicationProvider
        final View itemView = userAdapter.onCreateViewHolder(new RecyclerView(ApplicationProvider.getApplicationContext()), 0).itemView;

        // Assert that the itemView is not null
        Assert.assertNotNull(itemView);

        // Assert that the itemView contains a TextView for the user name
        final TextView userName = itemView.findViewById(R.id.user_name_item);
        Assert.assertNotNull(userName);
        
    }

    @Test
    public void testOnBindViewHolder() {
        // Get a ViewHolder from the UserAdapter
        final UserAdapter.ViewHolder viewHolder = userAdapter.onCreateViewHolder(new RecyclerView(ApplicationProvider.getApplicationContext()), 0);

        // Bind a UserAccount object to the ViewHolder
        final int position = 0;
        userAdapter.onBindViewHolder(viewHolder, position);

        // Assert that the ViewHolder's userName TextView contains the correct user name
        final TextView userName = viewHolder.userName;
        Assert.assertEquals(userList.get(position).getUsername(), userName.getText().toString());
    }

    @Test
    public void testGetUser() {
        // Get a UserAccount object from the UserAdapter
        final int position = 1;
        final UserAccount user = userAdapter.getUser(position);

        // Assert that the UserAccount object returned by getUser() is the correct one
        Assert.assertEquals(userList.get(position), user);
    }

    // TODO: Implement tests for setClickListener() and setFilterList()
}