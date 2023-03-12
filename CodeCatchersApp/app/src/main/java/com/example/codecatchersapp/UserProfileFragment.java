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

    public UserProfileFragment(UserAccount user, CharSequence searchQuery) {
        this.searchQuery = searchQuery;
        this.user = user;
    }

    // TODO: Receive UserAccount object in onCreateView


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View view = inflater.inflate(R.layout.user_profile, container, false);
        return view;
    }

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
