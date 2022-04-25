package com.example.emongani;
//hello
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.myprofile_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyProfileSeller.class));
            }
        });

        // initialize and assign varibles
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavbar);
        // set home selected
        bottomNavigationView.setSelectedItemId(R.id.homepage);
        // perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homepage:
                        return true;
                    case R.id.chatpage:
                        startActivity(new Intent(getApplicationContext()
                                , ChatPage.class)); // change it to chat.class
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profilepage:
                        startActivity(new Intent(getApplicationContext()
                                , MyProfileSeller.class)); // change it to chat.class
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}