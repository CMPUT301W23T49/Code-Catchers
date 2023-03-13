package com.example.codecatchersapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewMonProfile extends AppCompatActivity {

    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;

    private RelativeLayout relativeLayout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_monster);
        Intent intent = getIntent();


        Button deleteButton = findViewById(R.id.mon_delete_button);
        TextView username = findViewById(R.id.view_comment_username);
        TextView comment = findViewById(R.id.view_mon_comment);

        CollectionReference collectionReference = db.collection("PlayerDB/someUserID1/Monsters/someMonsterID/comment");
        //collectionReference.document();

        /*
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.fragment_mon_settings, null);

                popupWindow = new PopupWindow(container, 400,400, true);
                relativeLayout = (RelativeLayout) findViewById(R.id.mon_settings_layout);
                popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, 500,500);

                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popupWindow.dismiss();
                        return true;
                    }
                });

            }
        });*/
    }
}