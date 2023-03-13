package com.example.codecatchersapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {
    private UserAccount user;
    private CharSequence searchQuery;
    private FloatingActionButton backButton;
    private TextView userName;
    private TextView userScore;
    private TextView userNumMonsters;

    private ArrayList<String> scannedMonsters;
    private ArrayList<Monster> monsters;

    private RecyclerView rv_monsters;
    private MonsterAdapter monsterAdapter;


    // TODO: Receive UserAccount object in onCreateView

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        this.user = (UserAccount) intent.getSerializableExtra("UserAccount");
        this.searchQuery = intent.getCharSequenceExtra("Query");
        this.scannedMonsters = intent.getStringArrayListExtra("Monsters");
        // Set content view
        setContentView(R.layout.user_profile);


        monsters = new ArrayList<>();
        for (String monsterHash : scannedMonsters) {
           monsters.add(new Monster(monsterHash));
        }
        rv_monsters = findViewById(R.id.user_monster_rv);
        rv_monsters.setLayoutManager(new LinearLayoutManager(this));
        //rv_monsters.setLayoutManager(new GridLayoutManager(UserProfileActivity.this, 2));
        monsterAdapter = new MonsterAdapter(monsters);
        rv_monsters.setAdapter(monsterAdapter);


        // Set onClickListener for the back button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, SearchUsersActivity.class);
                intent.putExtra("PreviousQuery", searchQuery);
                startActivity(intent);

            }
        });
        // Get user data TextViews
        userName = findViewById(R.id.user_name_label);
        userScore = findViewById(R.id.user_score);
        userNumMonsters = findViewById(R.id.num_monster);

        // TODO: Set the user TextViews with the user's data
        userName.setText(user.getUsername());



    }

}
