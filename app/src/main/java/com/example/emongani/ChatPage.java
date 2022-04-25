package com.example.emongani;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChatPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        // initialize and assign varibles
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavbar);
        // set chatpage selected
        bottomNavigationView.setSelectedItemId(R.id.chatpage);
        // perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homepage:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class)); // change it to real homePage.class
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.chatpage: // change it to real chat page
                        return true;
                    case R.id.profilepage:
                        startActivity(new Intent(getApplicationContext()
                                , MyProfileSeller.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}