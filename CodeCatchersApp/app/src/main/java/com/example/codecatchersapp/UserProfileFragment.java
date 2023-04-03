/**
 * a class
 * @author CMPUT301W23T49
 * @version 1.0
 * @since [Monday April 3]
 */
package com.example.codecatchersapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class represents the fragment displaying a user profile. It extends Fragment and implements MonsterAdapter.ItemClickListener.
 * It contains instance variables for the user account, FloatingActionButton, and TextViews and RecyclerView for the user's profile.
 * It also includes a FirebaseFirestore instance and CollectionReference for accessing the user's scanned monsters from the database.
 * The class provides a constructor that takes a UserAccount object as a parameter, as well as an onItemClick method that is called
 * when a user clicks on one of their scanned monsters.
 */
public class UserProfileFragment extends Fragment implements MonsterAdapter.ItemClickListener {
    private UserAccount user;
    private String deviceID;
    private FloatingActionButton backButton;
    private FloatingActionButton hamburgerMenuButton;
    private DialogFragment hamburgerMenuFragment;
    private TextView userName;
    private TextView userScore;
    private TextView userNumMonsters;
    private Button sortButton;

    private ArrayList<Monster> monsters;

    private RecyclerView rv_monsters;
    private MonsterAdapter monsterAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userCollection = db.collection("PlayerDB");



    /**
     * Constructor for UserProfileFragment that takes a UserAccount object as a parameter.
     * @param user the user whose profile is being displayed
     */
    public UserProfileFragment(UserAccount user) {
        this.user = user;

    }
    /**
     * Constructor for UserProfileFragment that takes a deviceID String as a parameter.
     * @param deviceID the deviceID for the user whose profile is being displayed
     */
    public UserProfileFragment(String deviceID) {
        this.deviceID = deviceID;

    }

    /**
     * Called when the fragment is attached to the context.
     * @param context the context to which the fragment is attached
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    /**
     * Called to create the view hierarchy associated with the fragment.
     * @param inflater the LayoutInflater object that can be used to inflate any views in the fragment
     * @param container the parent view that the fragment's UI should be attached to
     * @param savedInstanceState the saved state of the fragment
     * @return the inflated view hierarchy
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile, container, false);
        return view;
    }

    /**
     * Called when the view hierarchy associated with the fragment has been created.
     * @param view the view hierarchy associated with the fragment
     * @param savedInstanceState the saved state of the fragment
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get the TextViews for the user profile and the RecyclerView for the scanned monsters
        userName = view.findViewById(R.id.user_name_label);
        userScore = view.findViewById(R.id.user_score);
        userNumMonsters = view.findViewById(R.id.num_monster);
        rv_monsters = view.findViewById(R.id.user_monster_rv);
        sortButton = view.findViewById((R.id.sort_button));


        // Set onClickListener for the back button and hamburger menu
        backButton = view.findViewById(R.id.back_button);
        hamburgerMenuButton = view.findViewById(R.id.hamburger_menu);
        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Navigates back to the SearchUsersActivity when clicked.
             *
             * @param view the clicked view.
             */
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();

            }
        });


        hamburgerMenuButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Shows the hamburger menu fragment when clicked
             *
             * @param view the clicked view.
             */
            @Override
            public void onClick(View view) {

                hamburgerMenuFragment = new HamburgerMenuFragment(R.id.user_profile);
                hamburgerMenuFragment.show(getParentFragmentManager(), "HamburgerFragment");


            }
        });

        userCollection.document(user.getDeviceID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            /**
             * Called when the user's information is successfully retrieved from the database.
             * @param task the task of retrieving the user's information
             */
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    userName.setText((String) doc.get("username"));
                    userScore.setText((String) doc.get("totalscore"));
                    deviceID =  doc.getId();
                }
            }
        });

        // Get the users scanned monsters from the database
        monsters = new ArrayList<>();


        // Get the user info by username
        CollectionReference collectionReference = userCollection.document(user.getDeviceID()).collection("Monsters");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            /**
             * Called when the user's scanned monsters are successfully retrieved from the database.
             * @param queryDocumentSnapshots the list of scanned monsters
             */
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                ArrayList<Monster> tempList = new ArrayList<>();
                for (DocumentSnapshot doc : docs) {
                    String shaHash = doc.getString("monsterSHAHash");
                    String binaryHash = doc.getString("monsterBinaryHash");
                    String monsterName = doc.getString("monsterName");
                    String monsterScore = doc.getString("monsterScore");
                    Monster currMonster = new Monster(shaHash, binaryHash, monsterName, monsterScore);
                    tempList.add(currMonster);
                }
                if (tempList != null) {
                    for (Monster monster : tempList) {
                        monsters.add(monster);
                    }

                    Collections.sort(monsters, new Comparator<Monster>() {
                        /**
                         * Compares two monsters based on their score.
                         * @param monster1 the first monster
                         * @param monster2 the second monster
                         * @return the difference between the two monsters' scores
                         */
                        @Override
                        public int compare(Monster monster1, Monster monster2) {
                            return monster2.getMonsterScore().compareTo(monster1.getMonsterScore());

                        }

                    });
                }
                //Log.i("QueryDocumentSnapshot:", queryDocumentSnapshots.getDocuments().toString());
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /**
             * Called when the user's scanned monsters are successfully retrieved from the database.
             * @param task the task that was completed
             */
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // Calculate the user's total score from their scanned monsters


                // Set the TextViews and RecyclerView for the user's profile
                userNumMonsters.setText(String.valueOf(monsters.size()));
                rv_monsters.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
                monsterAdapter = new MonsterAdapter(monsters);
                monsterAdapter.setClickListener(UserProfileFragment.this);
                rv_monsters.setAdapter(monsterAdapter);
                if (!monsters.isEmpty()) {
                    sortButton.setOnClickListener(new View.OnClickListener() {
                        /**
                         * Sorts the monsters by highest score when clicked.
                         *
                         * @param view the clicked view.
                         */
                        @Override
                        public void onClick(View view) {
                            if (sortButton.getText().toString().contains("highest")) {
                                sortButton.setText("Sort by: lowest");
                                Collections.sort(monsters, new Comparator<Monster>() {
                                    /**
                                     * Compares the monsters by their score.
                                     *
                                     * @param monster1 the first monster
                                     * @param monster2 the second monster
                                     * @return the difference between the two monsters' scores
                                     */
                                    @Override
                                    public int compare(Monster monster1, Monster monster2) {
                                        return monster1.getMonsterScore().compareTo(monster2.getMonsterScore());
                                    }
                                });

                            } else {
                                sortButton.setText("Sort by: highest");
                                Collections.sort(monsters, new Comparator<Monster>() {
                                    /**
                                     * Compares the monsters by their score.
                                     *
                                     * @param monster1 the first monster
                                     * @param monster2 the second monster
                                     * @return the difference between the two monsters' scores
                                     */
                                    @Override
                                    public int compare(Monster monster1, Monster monster2) {
                                        return monster2.getMonsterScore().compareTo(monster1.getMonsterScore());

                                    }

                                });

                            }
                            monsterAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });



    }


    /**
     * Handles the onItemClick event for the monsterAdapter. It gets the selected Monster object from
     * the monsterAdapter and then creates a new Intent that navigates to the the ViewMonProfile activity.
     * It then passes the selected monster hash, name, and score to the created intent and starts the activity.
     * @param view the clicked view
     * @param position the position of the clicked view in the adapter
     */
    @Override
    public void onItemClick(View view, int position) {
        Log.i("OnItemClick", "In onItemClick");
        // Get the selected monster from the adapter
        Monster monster = monsterAdapter.getMonster(position);

        // Create a new intent for the ViewMinProfile activity and pass the user and monster's info to it
        Intent monsterIntent = new Intent(getContext(), ViewMonProfile.class);
        monsterIntent.putExtra("userName", user.getUsername());
        monsterIntent.putExtra("shaHash", monster.getMonsterSHAHash());
        monsterIntent.putExtra("binaryHash", monster.getMonsterBinaryHash());
        monsterIntent.putExtra("monsterName", monster.getMonsterName());
        monsterIntent.putExtra("monsterScore", monster.getMonsterScore());
        monsterIntent.putExtra("monsterScore", monster.getMonsterScore());
        monsterIntent.putExtra("userID", deviceID);

        startActivity(monsterIntent);
    }
}
