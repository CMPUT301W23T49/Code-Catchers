
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

public class MonsterAdapter extends RecyclerView.Adapter<MonsterAdapter.ViewHolder> {

    private List<String> monsterList;

    // Temporary
    // Initialize MonsterAdapter
    public MonsterAdapter(ArrayList<String> monsters) {
            this.monsterList = monsters;
        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View userView = LayoutInflater.from(parent.getContext()).inflate(R.layout.monster_item, parent, false);
        return new ViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the monster hash from the list
        String monsterHash = monsterList.get(position);
        String monsterName = new MonsterNameGenerator().generateName(monsterHash);
        holder.monsterName.setText(monsterName);
        String monsterScore;
        // Get the score for the monster
        try {
            Score score = new Score(monsterHash);
            monsterScore = score.getScore();
            holder.monsterScore.setText(monsterScore);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return monsterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView monsterName;
        public TextView monsterScore;
        public ImageView monsterImage;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                monsterName = itemView.findViewById(R.id.monster_name);
                monsterScore = itemView.findViewById(R.id.monster_score);
                monsterImage = itemView.findViewById(R.id.monster_image);
            }
        }

}
