package com.example.codecatchersapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<UserAccount> userList;

    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    // Initialize the UserAdapter
    public UserAdapter(ArrayList<UserAccount> users) {
        this.userList = users;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate the user item layout
        View userView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);

        // Return a new ViewHolder
        return new ViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the UserAccount object from the list
        UserAccount user = userList.get(position);

        // Get the username of the UserAccount
        String userName = user.getUsername();

        // Set the text for the user item layout
        holder.userName.setText(userName);

    }

    // Return the size of the user list
    @Override
    public int getItemCount() {
        return userList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView userName;
        public CardView userCard;

        // Initialize the ViewHolder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Get the user name view and set the OnClickListener for the view
            userName = itemView.findViewById(R.id.user_name_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Invoke onItemClick if the itemClickListener is not null
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getAbsoluteAdapterPosition());
            }
        }
    }

    // ClickListener setter
    void setClickListener(ItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    // Returns UserAccount object from the userList
    UserAccount getUser(int id) {
        return userList.get(id);
    }

    // Implementation for the SearchUsersActivity
    public interface ItemClickListener {
      void onItemClick(View view, int position);
    }

    // Update the user list when a query is made
    public void setFilterList(List<UserAccount> filteredUsers) {
        this.userList = filteredUsers;
        notifyDataSetChanged();
    }

}
