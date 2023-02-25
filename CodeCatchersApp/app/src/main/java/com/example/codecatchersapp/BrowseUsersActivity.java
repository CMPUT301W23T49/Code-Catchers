package com.example.codecatchersapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BrowseUsersActivity extends AppCompatActivity {

    private EditText usernameEnteredEditText;
    private Button searchButton;
    private String usernameEntered;
    private ListView usernamesListView;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_by_users);

        usernameEnteredEditText = findViewById(R.id.editTextUsernameSearch);
        searchButton = findViewById(R.id.buttonSearch);
        usernamesListView = findViewById(R.id.listViewUsers);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");

        // If user clicks on search button
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Get string user input
                usernameEntered = usernameEnteredEditText.getText().toString();

                //Query query = collectionReference.whereEqualTo("username",usernameEntered);
                // Queries database for usernames that are identical to what user entered. Will have to narrow down to include string
                collectionReference
                        .whereEqualTo("username",usernameEntered)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("Working", document.getId() + " => " + document.getData());
                                        // --! ENTER DATA INTO LISTVIEW HERE SO IT DISPLAYS WHEN RUN
                                    }
                                } else {
                                    Log.d("Not working", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

        // If user clicks on name of user after searching
        usernamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

    }
}
