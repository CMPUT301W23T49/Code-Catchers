/**
 * a class
 * @author CMPUT301W23T49
 * @version 1.0
 * @since [Monday April 3]
 */
package com.example.codecatchersapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
/**
 The CommentAdapter class is an adapter for displaying a list of comments in a RecyclerView.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> commentList;

    /**
     Constructs a new CommentAdapter object with the specified list of comments.
     @param commentList the list of comments to display
     */
    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    /**
     Called when a new ViewHolder object is created.
     @param parent the parent ViewGroup
     @param viewType the view type
     @return a ViewHolder object
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_section, parent, false);
        return new ViewHolder(view);
    }

    /**
     Updates ViewHolder with new data
     @param holder the ViewHolder object to update
     @param position the position of the item in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.usernameTextView.setText(comment.getUserName());
        holder.commentTextView.setText(comment.getCommentText());
        Log.d("CommentAdapter", "Displaying comment: " + comment.getCommentText());
    }

    /**
     Returns the number of items in the list.
     @return integer number of items in the list
     */
    @Override
    public int getItemCount() {
        return commentList.size();
    }


    /**
     Represents a single item view in the RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         The TextView used to display the username.
         The TextView used to display the comment.
         */
        public TextView usernameTextView;
        public TextView commentTextView;

        /**
         Constructs a new ViewHolder object with the provided View.
         @param itemView the View used to construct a new ViewHolder
         */
        public ViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.view_comment_username);
            commentTextView = itemView.findViewById(R.id.view_mon_comment);
        }
    }
}
