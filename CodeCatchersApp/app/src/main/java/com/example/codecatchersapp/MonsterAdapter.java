
package com.example.codecatchersapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
/**
 * The MonsterAdapter class is an implementation of RecyclerView.Adapter
 * that is responsible for displaying a list of monsters in a RecyclerView.
 */
public class MonsterAdapter extends RecyclerView.Adapter<MonsterAdapter.ViewHolder> {

    private List<Monster> monsterList;
    private ItemClickListener itemClickListener;


    /**
     * Constructs a new MonsterAdapter with a list of monsters.
     * @param monsters The list of monsters to display
     */
    public MonsterAdapter(ArrayList<Monster> monsters) {
            this.monsterList = monsters;
        }

    /**
     * Creates a new ViewHolder instance for the monster view.
     * @param parent The parent ViewGroup
     * @param viewType The view type
     * @return A new ViewHolder for the item view
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View monsterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.monster_item, parent, false);
        return new ViewHolder(monsterView);
    }

    /**
     * Binds the data for the position to the ViewHolder.
     * @param holder The ViewHolder instance
     * @param position The position of the monster in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the monster from the list and get its info
        Monster monster = monsterList.get(position);
        String monsterHash = monster.getMonsterBinaryHash();
        String monsterName = monster.getMonsterName();
        String monsterScore = monster.getMonsterScore();

        // Set the TextViews and the MonsterView for the monster
        holder.monsterName.setText(monsterName);
        holder.monsterScore.setText(monsterScore);
        if (holder.monsterImage != null) {
            holder.monsterImage.setBinaryHash(monsterHash);
        }
    }

    /**
     * Returns the number of monsters in the list.
     * @return The number of monsters in the list
     */
    @Override
    public int getItemCount() {
        return monsterList.size();
    }

    /**
     * The ViewHolder class represents a single item view in the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView monsterName;
        public TextView monsterScore;
        public MonsterView monsterImage;

        /**
         * Constructs a new ViewHolder instance for the specified item view.
         * @param itemView The item view
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Set the TextViews and OnClickListener
            monsterName = itemView.findViewById(R.id.monster_name);
            monsterScore = itemView.findViewById(R.id.monster_score);
            monsterImage = itemView.findViewById(R.id.monster_image);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when the item view is clicked.
         * @param view The clicked view
         */
        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    /**
     * Returns the monster at the specified position in the list.
     * @param pos The position of the monster in the list
     * @return The monster at the specified position
     */
    Monster getMonster(int pos) {
        return monsterList.get(pos);
    }

    /**
     * Sets the ItemClickListener for this adapter.
     * @param clickListener The ItemClickListener to set
     */
    void setClickListener(ItemClickListener clickListener) {
        this.itemClickListener = clickListener;
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


}
