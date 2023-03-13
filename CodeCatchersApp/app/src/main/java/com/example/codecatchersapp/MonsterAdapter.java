package com.example.codecatchersapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MonsterAdapter extends RecyclerView.Adapter<MonsterAdapter.ViewHolder> {

    private ArrayList<Monster> monsterList;

    // Temporary
    // Initialize MonsterAdapter
    public MonsterAdapter(ArrayList<Monster> monsters) {
            this.monsterList = monsters;
        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View monsterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.monster_item, parent, false);
        return new ViewHolder(monsterView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get Monster object from the list
        holder.bind(monsterList.get(position));
    }

    @Override
    public int getItemCount() {
        return monsterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView monsterScore;
        public ImageView monsterView;
        public TextView monsterName;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                // Get the views for the monster item
                monsterScore = itemView.findViewById(R.id.monster_score);
                monsterView = itemView.findViewById(R.id.monster_image);
                monsterName = itemView.findViewById(R.id.monster_name);
            }

            public void bind(Monster monster) {

                monsterScore.setText(String.valueOf(monster.getScore()));
                //imageView.setImageResource(monster.getImageResourceId());
                monsterName.setText(monster.getName());
            }
            // TODO: Set onClick functionality for the monster items
        }

}
