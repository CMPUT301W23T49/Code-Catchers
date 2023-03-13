package com.example.codecatchersapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class LeaderboardsArrayAdapter extends ArrayAdapter<Leaderboards> {

    public LeaderboardsArrayAdapter(Context context, List<Leaderboards> values) {
        super(context, 0, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(super.getContext()).inflate(R.layout.leaderboards_layout, parent, false);
        } else {
            view = convertView;
        }
        Leaderboards leaderboards = super.getItem(position);
        TextView username = view.findViewById(R.id.username_text);
        TextView score = view.findViewById(R.id.leaderboard_score_text);

        username.setText(leaderboards.getUsername());
        score.setText(leaderboards.getScoreStringRepresentation());

        return view;
    }
}

