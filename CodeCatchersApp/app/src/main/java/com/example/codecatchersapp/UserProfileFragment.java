package com.example.codecatchersapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    FirebaseFirestore db;
    CollectionReference userCollection;

    // TODO: set the monster image

    public UserProfileFragment(UserAccount user, CharSequence searchQuery) {
        this.searchQuery = searchQuery;
        this.user = user;

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View view = inflater.inflate(R.layout.user_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        userCollection = db.collection("PlayerDB");
        super.onViewCreated(view, savedInstanceState);
        // Get the TextViews for the user profile and the RecyclerView for the scanned monsters
        userName = view.findViewById(R.id.user_name_label);
        userScore = view.findViewById(R.id.user_score);
        userNumMonsters = view.findViewById(R.id.num_monster);
        rv_monsters = view.findViewById(R.id.user_monster_rv);

        userName.setText(user.getUsername());

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


        // Get the users scanned monsters from the database
        monsters = new ArrayList<>();

        Query query = userCollection.whereEqualTo("userName", user.getUsername());
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Get the monster hashes from the user
                        ArrayList<Object> tempList = new ArrayList<>();
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : docs) {
                            Log.i("TAG", String.valueOf(doc));
                            tempList = (ArrayList<Object>) doc.get("scannedMonsters");
                        }
                        // Add the monster hashes to the monster list
                        for (Object hash : tempList) {
                            monsters.add(hash.toString());
                        }
                        Log.i("TAG", "Length of monsters: " + monsters.size());
                        for (String s : monsters) {
                            Log.i("TAG", s);
                        }


                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // Calculate the user's total score from their scanned monsters
                Integer tempScore = 0;
                for (String hash : monsters) {
                    try {
                        Score score = new Score(hash);
                        tempScore += Integer.parseInt(score.getScore());

                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                }

                // Set the TextViews and RecyclerView for the user's profile
                userScore.setText(tempScore.toString());
                userNumMonsters.setText(String.valueOf(monsters.size()));
                rv_monsters.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
                monsterAdapter = new MonsterAdapter(monsters);
                rv_monsters.setAdapter(monsterAdapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });








    }
}
