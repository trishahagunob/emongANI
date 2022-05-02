package com.example.emongani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emongani.Model.Users;
import com.example.emongani.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class loginActivity extends AppCompatActivity {

    private EditText InputEmailAddress, InputPassword;
    private Button loginButton;
    private ProgressDialog loadingBar;

    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_btn);
        InputEmailAddress = (EditText) findViewById(R.id.login_emailaddress_input);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String emailAddress = InputEmailAddress.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(emailAddress)){
            Toast.makeText(this, "Please write your Email Address", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please write your Password", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(emailAddress, password);
        }

    }

    private void AllowAccessToAccount(String emailAddress, String password) {
        if(chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserEmailKey, emailAddress);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("parentDbName").child(emailAddress).exists()){
                    Users usersData = snapshot.child(parentDbName).child(emailAddress).getValue(Users.class);

                    if (usersData.getEmailAddress().equals(emailAddress)){
                        if (usersData.getPassword().equals(password)){
                            Toast.makeText(loginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(loginActivity.this, HomeActivity.class); //just change the home activity class
                            startActivity(intent);
                        }else{
                            loadingBar.dismiss();
                            Toast.makeText(loginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(loginActivity.this, "Account with this " + emailAddress + " number do not exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}