package com.example.codecatchersapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 ViewMonProfile displays the image of a monster along with its comments from the database.
 Currently settings button is not implemented, back button is not implemented, and new comment functionality
 is not implemented.
 */
public class ViewMonProfile extends AppCompatActivity {

    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;

    private RelativeLayout relativeLayout;

    private List<Comment> comments = new ArrayList<>();

    private CommentAdapter commentAdapter;

    private TextView monsterName;
    private MonsterView monsterView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     Sets up the RecyclerView to display the comments and fetches them from the database.
     @param savedInstanceState A Bundle object containing the activity's saved state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_monster);
        Intent intent = getIntent();
        String shaHash = intent.getStringExtra("shaHash");
        String binaryHash = intent.getStringExtra("binaryHash");
        String selectedMonsterName = intent.getStringExtra("monsterName");
        String selectedMonsterScore = intent.getStringExtra("monsterScore");



        // Set up the RecycleView with UserAdapter and ClickListener
        RecyclerView rvComments = findViewById(R.id.rv_comments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        CommentAdapter commentAdapter = new CommentAdapter((ArrayList<Comment>) comments);
        rvComments.setAdapter(commentAdapter);



        // comment stuff
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String myUserName = sharedPreferences.getString("username", "");
        String userID = intent.getStringExtra("userID");
        EditText commentEditText = findViewById(R.id.new_comment_other_user_text);
        commentEditText.clearFocus();
        FloatingActionButton sendCommentButton = findViewById(R.id.send_comment_button);

        // SAVE ANY NEW COMMENT TO DATABASE
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            /**
             Saves the comment to the database.
             @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                saveComment();
            }
            /**
             Saves the comment to the database.
             */
            public void saveComment() {
                CollectionReference collectionReference = db.collection("PlayerDB/" + userID + "/Monsters/" + shaHash + "/comments");
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
                                 Displays a message to the user if the comment was successfully saved.
                                 @param unused
                                 */
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("Success", "Comment added successfully!");
                                    comments.add(new Comment(myUserName, ogComment));
                                    commentAdapter.notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                /**
                                 Displays a message to the user if the comment was not successfully saved.
                                 @param e The exception that was thrown.
                                 */
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Failure", "Comment addition failed" + e.toString());
                                }
                            });

                }
            }
        });




        monsterName = findViewById(R.id.monster_name_monster_profile);
        monsterView = findViewById(R.id.monster_image);

        monsterName.setText(selectedMonsterName);
        monsterView.setBinaryHash(binaryHash);

        FloatingActionButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             Returns to the previous activity.
             @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        CollectionReference collectionReference = db.collection("PlayerDB/" + userID + "/Monsters/" + shaHash +"/comments");



        // Get the users stored in the DB and add them to the list of users
        Query query = collectionReference.orderBy("userName");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /**
             Retrieves the comments from the database and adds them to the list of comments.
             @param task The task that was completed.
             */
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

}