/**
 * This class is the fragment that displays the user's profile
 * It is called when the user clicks on a user in the search results
 * It displays the user's username, score, and monsters
 * It also has a back button that returns the user to the search results
 *  @author [Mathew Maki]
 *  @version 1.0
 *  @since [Saturday March 11 2023]
 */
package com.example.codecatchersapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codecatchersapp.MonsterAdapter;
import com.example.codecatchersapp.UserAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserProfileFragment extends Fragment {
    private UserAccount user;
    private CharSequence searchQuery;
    private FloatingActionButton backButton;
    private TextView userName;
    private TextView userScore;
    private TextView userNumMonsters;
    private ArrayList<String> monsters;
    private RecyclerView rv_monsters;
    private MonsterAdapter monsterAdapter;
    /**
     * Constructor that receives a `UserAccount` object and a `CharSequence` object representing
     * the search query.
     *
     * @param user The `UserAccount` object representing the user's profile.
     * @param searchQuery The `CharSequence` object representing the search query.
     */
    public UserProfileFragment(UserAccount user, CharSequence searchQuery) {
        this.searchQuery = searchQuery;
        this.user = user;
    }

    // TODO: Receive UserAccount object in onCreateView

    /**
     * This method is called to create the view hierarchy associated with the fragment. It inflates
     * the layout for the user's profile and returns the view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A bundle that can be used to save the state of the fragment.
     *
     * @return The view hierarchy associated with the fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View view = inflater.inflate(R.layout.user_profile, container, false);
        return view;
    }
    /**
     * This method is called after `onCreateView` to complete the fragment's UI setup. It sets the
     * `onClickListener` for the back button and initializes the user's data TextViews, monsters
     * list, and monster recycler view.
     *
     * @param view The view hierarchy associated with the fragment.
     * @param savedInstanceState A bundle that can be used to save the state of the fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set onClickListener for the back button
        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchUsersActivity.class);
                intent.putExtra("PreviousQuery", searchQuery);
                startActivity(intent);

            }
        });
        // Get user data TextViews
        userName = view.findViewById(R.id.user_name_label);
        userScore = view.findViewById(R.id.user_score);
        userNumMonsters = view.findViewById(R.id.num_monster);

        monsters = new ArrayList<>();
        rv_monsters = view.findViewById(R.id.user_monster_rv);
        rv_monsters.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        monsterAdapter = new MonsterAdapter(monsters);
        rv_monsters.setAdapter(monsterAdapter);
        // TODO: Set the user TextViews with the user's data
        userName.setText(user.getUsername());


    }
}
