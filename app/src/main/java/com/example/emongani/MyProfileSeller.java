package com.example.emongani;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyProfileSeller extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_seller);

        Button mySalesbtn = findViewById(R.id.mysales_btn);
        mySalesbtn.setOnClickListener(view ->
                startActivity(new Intent(MyProfileSeller.this, MySales.class)));
        Button addPalaybtn = findViewById(R.id.addpalay_btn);
            addPalaybtn.setOnClickListener(view -> {
                Intent intent = new Intent(MyProfileSeller.this,AddPalay.class);
                startActivity(intent);
            });
                    // initialize and assign varibles
                    BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavbar);
        // set profilepage selected
        bottomNavigationView.setSelectedItemId(R.id.profilepage);
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
                    case R.id.chatpage:
                        startActivity(new Intent(getApplicationContext()
                                , ChatPage.class)); // change it to real chatpage.class
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profilepage:
                        return true;
                }
                return false;
            }
        });
    }
}
