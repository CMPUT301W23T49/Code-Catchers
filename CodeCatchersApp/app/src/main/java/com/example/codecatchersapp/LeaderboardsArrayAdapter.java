/**
 * LeaderboardsArrayAdapter extends ArrayAdapter<Leaderboards>.
 * It is responsible for displaying leaderboards_layout.xml.
 */
package com.example.codecatchersapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * LeaderboardsArrayAdapter extends ArrayAdapter.
 * It is responsible for displaying leaderboards_layout.xml.
 */
public class LeaderboardsArrayAdapter extends ArrayAdapter<Leaderboards> {

    public LeaderboardsArrayAdapter(Context context, List<Leaderboards> values) {
        super(context, 0, values);
    }

    /**
     * Called when the object is made in LeaderboardsActivity.
     * Connects to the leaderboards_layout.xml and sets it as the content view.
     *
     * @param position position indicates which element in ArrayAdapter is being analyzed
     *        convertView
     *
     * @return view View of Leaderboards objects
     */
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

