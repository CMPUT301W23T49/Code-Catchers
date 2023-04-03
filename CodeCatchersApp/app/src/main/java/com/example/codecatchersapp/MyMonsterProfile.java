/**
 * a class
 * @author CMPUT301W23T49
 * @version 1.0
 * @since [Monday April 3]
 */
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMonsterProfile extends AppCompatActivity {
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;

    private RelativeLayout relativeLayout;

    private List<Comment> comments;

    private CommentAdapter commentAdapter;

    private TextView monsterName;
    private MonsterView monsterView;
    private Button monsterSettings;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String newTotalScore;
    String newMonsterCount;
    String newHighestMonsterScore;

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

        String shaHash = intent.getStringExtra("shaHash");
        String binaryHash = intent.getStringExtra("binaryHash");

        String selectedMonsterName = intent.getStringExtra("monsterName");
        String selectedMonsterScore = intent.getStringExtra("monsterScore");


        monsterName = findViewById(R.id.monster_name_monster_profile);
        monsterView = findViewById(R.id.monster_image);
        monsterSettings = findViewById(R.id.mon_settings_button);

        // comment stuff
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String myUserName = sharedPreferences.getString("username", "");
        //String userID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //String shaHash = intent.getStringExtra("monsterHash");
        monsterName.setText(selectedMonsterName);

        monsterView.setBinaryHash(String.valueOf(selectedMonsterHash));

        monsterView.setBinaryHash(binaryHash);


        FloatingActionButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Goes back to the previous activity.
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        EditText commentEditText = findViewById(R.id.new_comment_my_monster_text);
        FloatingActionButton sendCommentButton = findViewById(R.id.send_comment_my_monster_button);

        /**
         * Saves a new comment to the database.
         */


        CollectionReference collectionReference = db.collection("PlayerDB/" + userID + "/Monsters/" + selectedMonsterHash + "/comments");
        // Create an ArrayList for comments
        comments = new ArrayList<>();

        // Set up the RecycleView with UserAdapter and ClickListener
        RecyclerView rvComments = findViewById(R.id.rv_comments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        CommentAdapter commentAdapter = new CommentAdapter((ArrayList<Comment>) comments);
        rvComments.setAdapter(commentAdapter);


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


        // SAVE ANY NEW COMMENT TO DATABASE
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveComment();
            }
            /**
             * Saves a new comment to the database.
             */
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
                                /**
                                 * Logs a success message.
                                 * @param unused Unused.
                                 */
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("Success", "Comment added successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                /**
                                 * Logs a failure message.
                                 * @param e The exception that was thrown.
                                 */
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Failure", "Comment addition failed" + e.toString());
                                }
                            });

                }
                commentAdapter.notifyDataSetChanged();
                commentEditText.setText("");

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
        });
        /**
         * Sets up the RecyclerView to display the comments and fetches them from the database.
         */
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

                /**
                 * Deletes the monster from the database.
                 */

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        // TODO: delete monster from playerDB
                        CollectionReference collectionReference = db.collection("PlayerDB/" + deviceID + "/Monsters/" + shaHash + "/comments");


                        // Reference to the document with the SHA hash to delete
                        DocumentReference docRef = collectionReference.document(shaHash);
                        docRef.delete()
                                /**
                                 * Logs a success message.
                                 * @param aVoid Unused.
                                 */
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        onBackPressed();
                                        updateLeaderboardFields(selectedMonsterScore);
                                    }
                                })
                                /**
                                 * Logs a failure message.
                                 * @param e The exception that was thrown.
                                 */
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });
                    }
                });
                /**
                 * Closes the dialog.
                 */
                returnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss(); // Close the dialog
                    }
                });

            }
        });



        CollectionReference collectionReference = db.collection("PlayerDB/" + deviceID + "/Monsters/" + shaHash + "/comments");

        // Create an ArrayList for comments
        comments = new ArrayList<>();

        // Set up the RecycleView with UserAdapter and ClickListener
        RecyclerView rvComments = findViewById(R.id.rv_comments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        CommentAdapter commentAdapter = new CommentAdapter((ArrayList<Comment>) comments);
        rvComments.setAdapter(commentAdapter);


        // Get the users stored in the DB and add them to the list of users
        Query query = collectionReference.orderBy("userName");
        /**
         * Fetches the comments from the database.
         */
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
     * Updates the user's score fields so that the leaderboards correctly display their scores.
     * @param scoreString
     */
    public void updateLeaderboardFields(String scoreString) {
        String userID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        DocumentReference documentReferenceUserScoreField = db.collection("PlayerDB/").document(userID);

        documentReferenceUserScoreField.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    /**
                     * Updates the user's score fields.
                     * @param documentSnapshot The document snapshot of the user.
                     */
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
                                /**
                                 * Iterates through all of the user's monsters and calculates the highest score.
                                 * @param task The task that was completed.
                                 */
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
                                        /**
                                         * Logs a success message.
                                         * @param unused Unused.
                                         */
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.e("E", "UPDATED FIELDS");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        /**
                                         * Logs a failure message.
                                         * @param e The exception that was thrown.
                                         */
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
                    /**
                     * Logs a failure message.
                     * @param e The exception that was thrown.
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("E", "ERROR GETTING DOCUMENT");
                    }
                });

    }
}
