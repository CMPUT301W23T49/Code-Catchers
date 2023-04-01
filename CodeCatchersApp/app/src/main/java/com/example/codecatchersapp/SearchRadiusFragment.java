package com.example.codecatchersapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class SearchRadiusFragment extends DialogFragment {
    private SeekBar seekBar;
    private TextView valueTextView;



    public static SearchRadiusFragment newInstance() {
        return new SearchRadiusFragment();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.search_radius_fragment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        seekBar = view.findViewById(R.id.search_radius_seekbar);
        valueTextView = view.findViewById(R.id.search_radius_value_textview);

        Toast.makeText(getContext(), "Radius selected: ", Toast.LENGTH_SHORT).show();

        // Set up the seek bar
        seekBar.setMax(100);
        seekBar.setProgress(50);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String valueText = progress + " km";
                valueTextView.setText(valueText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Set up the "Go" button
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int radius = seekBar.getProgress();
                Log.d("CapturingInput", "onSearchRadiusSelected: ");

                // Set the value in MapDisplayActivity (parent activity)
                ((MapDisplayActivity)getActivity()).onSearchRadiusSelected(radius);
            }

        });

        // Set up the "Cancel" button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle "Cancel" button click
                Log.d("Closing Dialog", "onSearchRadiusSelected: ");
            }

        });

        return builder.create();
    }


}