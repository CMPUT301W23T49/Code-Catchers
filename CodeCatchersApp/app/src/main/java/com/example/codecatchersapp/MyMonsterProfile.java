package com.example.codecatchersapp;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMonsterProfile extends AppCompatActivity {
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;

    private RelativeLayout relativeLayout;

    private List<Comment> comments = new ArrayList<>();

    private CommentAdapter commentAdapter;

    private TextView monsterName;
    private MonsterView monsterView;
    private Button monsterSettings;
    private String shaHash;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String newTotalScore;
    String newMonsterCount;
    String newHighestMonsterScore;
    // Create an ArrayList for comments

    /**
     Sets up the RecyclerView to display the comments and fetches them from the database.
     @param savedInstanceState A Bundle object containing the activity's saved state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_monster);
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String deviceID = intent.getStringExtra("deviceID");

        this.shaHash = intent.getStringExtra("shaHash");
        String binaryHash = intent.getStringExtra("binaryHash");

        String selectedMonsterName = intent.getStringExtra("monsterName");
        String selectedMonsterScore = intent.getStringExtra("monsterScore");

        // Set up the RecycleView with UserAdapter and ClickListener
        RecyclerView rvComments = findViewById(R.id.rv_comments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        CommentAdapter commentAdapter = new CommentAdapter((ArrayList<Comment>) comments);
        rvComments.setAdapter(commentAdapter);


        monsterName = findViewById(R.id.monster_name_monster_profile);
        monsterView = findViewById(R.id.monster_image);
        monsterSettings = findViewById(R.id.mon_settings_button);

        // comment stuff
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String myUserName = sharedPreferences.getString("username", "");
        String userID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);;
        monsterName.setText(selectedMonsterName);
        monsterView.setBinaryHash(binaryHash);

        FloatingActionButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        EditText commentEditText = findViewById(R.id.new_comment_my_monster_text);
        FloatingActionButton sendCommentButton = findViewById(R.id.send_comment_my_monster_button);

        // SAVE ANY NEW COMMENT TO DATABASE
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveComment();
                commentEditText.setText("");
            }

            public void saveComment() {
                CollectionReference collectionReference = db.collection("PlayerDB/" + deviceID + "/Monsters/" + shaHash + "/comments");
                final String ogComment = commentEditText.getText().toString();
                HashMap<String, String> data = new HashMap<>();
                if (ogComment.length() > 0) {
                    // TODO: change SomeUserID to current user's ID, change someMonsterID to monster hash
                    data.put("userName", myUserName);
                    collectionReference
                            .document(ogComment)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("Success", "Comment added successfully!");
                                    comments.add(new Comment(myUserName, ogComment));
                                    commentAdapter.notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Failure", "Comment addition failed" + e.toString());
                                }
                            });

                }
            }
        });

        monsterSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new dialog object
                Dialog dialog = new Dialog(MyMonsterProfile.this);
                dialog.setContentView(R.layout.fragment_mon_settings);

                // Set the dialog window properties
                Window window = dialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams params = window.getAttributes();
                params.dimAmount = 0.7f; // Set the amount of dimness you want
                window.setAttributes(params);
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

                // Show the dialog
                dialog.show();

                Button deleteButton = dialog.findViewById(R.id.delete_mon_settings);
                Button returnButton = dialog.findViewById(R.id.return_mon_settings);

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // TODO: delete monster from playerDB
                        CollectionReference collectionReference = db.collection("PlayerDB/" + deviceID + "/Monsters");
                        collectionReference.document(shaHash).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(MyMonsterProfile.this, MyProfileActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(MyMonsterProfile.this, "Monster deleted successfully", Toast.LENGTH_SHORT).show();
                                    updateLeaderboardFields(selectedMonsterScore);
                                } else {
                                    Toast.makeText(MyMonsterProfile.this, "Failed to delete monster", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();

                            }
                        });

                    }});


                returnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss(); // Close the dialog
                    }
                });

            }
        });




        CollectionReference collectionReference = db.collection("PlayerDB/" + deviceID + "/Monsters/" + shaHash + "/comments");




        // Get the users stored in the DB and add them to the list of users
        Query query = collectionReference.orderBy("userName");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String userName = document.getString("userName");
                        String commentId = document.getId();
                        Comment comment = new Comment(userName, commentId);
                        comments.add(comment);
                    }
                    commentAdapter.notifyDataSetChanged();
                    Log.d("CommentAdapter", "Number of comments retrieved: " + comments.size());
                } else {
                    Log.d("ERROR", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /**
     * This updates the user's score fields in the leaderboard based on the given score.
     * retrieves the user's information, calculates a new total score, monster count, and
     * highest monster score, and updates the user's record in the database.
     *
     * @param scoreString The score value to be subtracted from the user's total score.
     */

    public void updateLeaderboardFields(String scoreString) {
        String userID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        DocumentReference documentReferenceUserScoreField = db.collection("PlayerDB/").document(userID);

        documentReferenceUserScoreField.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> data = documentSnapshot.getData();

                            String oldTotalScore = (String) data.get("totalscore");
                            System.out.println("\n\n\n" + scoreString + "    " + oldTotalScore);
                            String oldMonsterCount = (String) data.get("monstercount");
                            String oldHighestMonsterScore = (String) data.get("highestmonsterscore");

                            newTotalScore = String.valueOf(Integer.parseInt(oldTotalScore) - Integer.parseInt(scoreString));
                            newMonsterCount = String.valueOf(Integer.parseInt(oldMonsterCount) - 1);
                            newHighestMonsterScore = "0";


                            // Calculate highest score by iterating through all of user's monsters
                            CollectionReference collectionReference = db.collection("PlayerDB/" + userID + "/Monsters");
                            collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String individualScore = document.getString("monsterScore");
                                            if (Integer.valueOf(individualScore) > Integer.valueOf(newHighestMonsterScore)){
                                                newHighestMonsterScore = individualScore;
                                            }
                                        }

                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                            Map<String, Object> newLeaderboardInfo = new HashMap<>();
                            newLeaderboardInfo.put("totalscore", newTotalScore);
                            newLeaderboardInfo.put("monstercount", newMonsterCount);
                            newLeaderboardInfo.put("highestmonsterscore", newHighestMonsterScore);

                            for (Map.Entry<String, Object> entry : newLeaderboardInfo.entrySet()) {
                                System.out.println("ENTERED LOOP");
                                System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
                            }

                            documentReferenceUserScoreField.update(newLeaderboardInfo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.e("E", "UPDATED FIELDS");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("E", "COULD NOT UPDATE FIELDS");
                                        }
                                    });

                        } else {
                            Log.e("E", "DOCUMENT DOES NOT EXIST");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("E", "ERROR GETTING DOCUMENT");
                    }
                });

    }
}
