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
/**
 * The UserAdapter class is an implementation of RecyclerView.Adapter
 * that is responsible for displaying a list of UserAccounts in a RecyclerView.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<UserAccount> userList;

    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    /**
     * Constructs a new UserAdapter with the specified list of UserAccount objects.
     * @param users The list of UserAccount objects to display.
     */
    public UserAdapter(ArrayList<UserAccount> users) {
        this.userList = users;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     * @param parent The parent ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate the user item layout
        View userView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);

        // Return a new ViewHolder
        return new ViewHolder(userView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder to update.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the UserAccount object from the list
        UserAccount user = userList.get(position);

        // Get the username of the UserAccount
        String userName = user.getUsername();

        // Set the text for the user item layout
        holder.userName.setText(userName);

    }

    /**
     * Returns the total number of items in the user list.
     * @return The total number of items in the user list.
     */
    @Override
    public int getItemCount() {
        return userList.size();
    }

    /**
     * ViewHolder class that represents a user item view.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView userName;
        public CardView userCard;

        /**
         * Constructs a new ViewHolder with the specified user item view.
         * @param itemView The user item view to hold.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Get the user name view and set the OnClickListener for the view
            userName = itemView.findViewById(R.id.user_name_item);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when the user item view is clicked.
         * @param view The View that was clicked.
         */
        @Override
        public void onClick(View view) {
            // Invoke onItemClick if the itemClickListener is not null
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    /**
     * Sets the ItemClickListener for the UserAdapter.
     * @param clickListener The ItemClickListener to set.
     */
    void setClickListener(ItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    /**
     * Returns the UserAccount object at the specified position in the userList.
     * @param pos The position of the UserAccount object to return.
     * @return The UserAccount object at the specified position in the userList.
     */
    UserAccount getUser(int pos) {
        return userList.get(pos);
    }

    /**
     * The ItemClickListener interface defines a method that will be called
     * when an item in the RecyclerView is clicked.
     */
    public interface ItemClickListener {
        /**
         * Called when an item in the RecyclerView is clicked.
         * @param view The clicked view
         * @param position The position of the item that was clicked
         */
        void onItemClick(View view, int position);
    }

    /**
     * Updates the user list with a new list of filtered users and notifies the adapter that the data set has changed.
     * @param filteredUsers A List of UserAccount objects representing the filtered user list.
     */
    public void setFilterList(List<UserAccount> filteredUsers) {
        this.userList = filteredUsers;
        notifyDataSetChanged();
    }

}
