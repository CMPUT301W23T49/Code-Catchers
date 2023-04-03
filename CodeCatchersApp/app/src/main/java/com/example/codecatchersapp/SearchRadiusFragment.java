/**
 * a class
 * @author CMPUT301W23T49
 * @version 1.0
 * @since [Monday April 3]
 */
package com.example.codecatchersapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

/**
 * Creates a new instance of the SearchRadiusFragment.
 *
 * @return a new instance of the SearchRadiusFragment
 */
public class SearchRadiusFragment extends DialogFragment {
    private SeekBar seekBar;
    private TextView valueTextView;


    public static SearchRadiusFragment newInstance() {
        return new SearchRadiusFragment();
    }


    /**
     * Called to create a dialog to be displayed by this fragment.
     *
     * @param savedInstanceState The saved instance state of the fragment.
     * @return A new Dialog instance to be displayed by the fragment.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.search_radius_fragment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        // Initialize the seek bar and text view
        seekBar = view.findViewById(R.id.search_radius_seekbar);
        valueTextView = view.findViewById(R.id.search_radius_value_textview);

        // Display a toast message indicating that the radius has been selected
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
                // This method is intentionally left blank
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // This method is intentionally left blank
            }
        });

        // Set up the "Go" button
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the radius selected by the user
                int radius = seekBar.getProgress();
                // Log the selected radius
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