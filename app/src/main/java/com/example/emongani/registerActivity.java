package com.example.emongani;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emongani.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.Utils;

import java.util.HashMap;
import java.util.Map;

public class registerActivity extends AppCompatActivity {
//hello
    private Button registerButton;
    private EditText InputFirstName, InputLastName, InputEmailAddress, InputMobileNumber, InputUsername, InputPassword;
    //private CheckBox InputSeller, InputBuyer;
    private ProgressDialog loadingBar;
    Users users;

    FirebaseAuth firebaseAuth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = (Button) findViewById(R.id.register_btn);
        InputFirstName = (EditText) findViewById(R.id.register_first_name_input);
        InputLastName = (EditText) findViewById(R.id.register_last_name_input);
        InputEmailAddress = (EditText) findViewById(R.id.register_email_address_input);
        InputMobileNumber = (EditText) findViewById(R.id.register_mobile_number_input);
        InputUsername = (EditText) findViewById(R.id.register_username_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        //InputSeller = (CheckBox) findViewById(R.id.seller_chkb);
        //InputBuyer = (CheckBox) findViewById(R.id.buyer_chkb);
        loadingBar = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance(); //added making firebase connection 4.25.22

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String firstName = InputFirstName.getText().toString().trim();
        String lastName = InputLastName.getText().toString();
        String emailAddress = InputEmailAddress.getText().toString();
        String mobileNumber = InputMobileNumber.getText().toString();
        String username = InputUsername.getText().toString();
        String password = InputPassword.getText().toString();
        //String accountType = InputSeller.getText().toString();
        //String accountType2 = InputBuyer.getText().toString();

        /*users.setFirstName(firstName);
        users.setLastName(lastName);
        users.setEmailAddress(emailAddress);
        users.setMobileNumber(mobileNumber);
        users.setUsername(username);
        users.setPassword(password);*/

        /*if(InputSeller.isChecked()){
            users.setAccountType(accountType);
        }else if(InputBuyer.isChecked()){
            users.setAccountType(accountType2);
        }*/


        if (TextUtils.isEmpty(firstName)){
            Toast.makeText(this, "Please write your First Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(lastName)){
            Toast.makeText(this, "Please write your Last Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(emailAddress)){
            Toast.makeText(this, "Please write your Email Address", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(mobileNumber)){
            Toast.makeText(this, "Please write your Mobile Number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please write your Username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please write your Password", Toast.LENGTH_SHORT).show();
        }
        else{
        // further connecting sa firebase ning sa baba 4.25.22
        loadingBar.setTitle("Create Account");
        loadingBar.setMessage("Please wait, while we are checking the credentials.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show(); //murag progress bar sa pagcreate sa account

            firebaseAuth.createUserWithEmailAndPassword(emailAddress, password) // OG: firebaseAuth.createUserWithEmailAndPassword(InputEmailAddress.getText().toString(), InputPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) { // task will help us determine if successful of not
                            if (task.isSuccessful()){
                                Toast.makeText(registerActivity.this, "Registered successfully",
                                        Toast.LENGTH_SHORT).show();

                                loadingBar.dismiss();

                                Intent intent = new Intent(registerActivity.this, MainActivity.class);
                                startActivity(intent); // leads them back sa main page to login
                                InputEmailAddress.setText("");
                                InputPassword.setText("");

                                // CONT TOMORROW: here kay mag call og method to add all these data to the firebase
                                validateData(firstName, lastName, emailAddress, mobileNumber, username, password);  /*, accountType*/

                            }else{
                                Toast.makeText(registerActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    } //end of CreateAccount() method

    private void validateData(String firstName, String lastName, String emailAddress, String mobileNumber, String username, String password /*, String accountType*/ ) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("users").child(emailAddress).exists())){ //or "Users"
                    HashMap<String, Object>userdataMap = new HashMap<>();
                    userdataMap.put("firstName", firstName);
                    userdataMap.put("lastName", lastName);
                    userdataMap.put("emailAddress", emailAddress);
                    userdataMap.put("mobileNumber", mobileNumber);
                    userdataMap.put("username", username);
                    userdataMap.put("password", password);
                    //userdataMap.put("accountType", accountType);
                    // the problem is unsaon nako ni if naay 2 types of user lol

                    RootRef.child("users").child(emailAddress).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(registerActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(registerActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        loadingBar.dismiss();
                                        Toast.makeText(registerActivity.this, "Network Error: Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else{
                    Toast.makeText(registerActivity.this, "This " + emailAddress + "already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(registerActivity.this, "Please", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(registerActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    //EVERYTHING BELOW IS OLD CODE, COMMENT OUT LANG
/*
    private void CreateAccount() {
        String firstName = InputFirstName.getText().toString();
        String lastName = InputLastName.getText().toString();
        String emailAddress = InputEmailAddress.getText().toString();
        String mobileNumber = InputMobileNumber.getText().toString();
        String username = InputUsername.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(firstName)){
            Toast.makeText(this, "Please write your First Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(lastName)){
            Toast.makeText(this, "Please write your Last Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(emailAddress)){
            Toast.makeText(this, "Please write your Email Address", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(mobileNumber)){
            Toast.makeText(this, "Please write your Mobile Number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please write your Username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please write your Password", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateemailAddress(firstName, lastName, emailAddress, mobileNumber, username, password);
        }


    }

    private void ValidateemailAddress(String firstName, String lastName, String emailAddress, String mobileNumber, String username, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot ) {

                String encodedEmail = Utils.encodeString(emailAddress); //suggestion ni syd

                if (!(snapshot.child("Users").child(encodedEmail).exists())){ // if (!(snapshot.child("Users").child(encodedEmail).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("emailAddress", emailAddress);
                    userdataMap.put("firstName", firstName);
                    userdataMap.put("lastName", lastName);
                    userdataMap.put("mobileNumber", mobileNumber);
                    userdataMap.put("username", username);
                    userdataMap.put("password", password);

                    RootRef.child("Users").child(emailAddress).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(registerActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(registerActivity.this, loginActivity.class);
                                        startActivity(intent);
                                    }else{
                                        loadingBar.dismiss();
                                        Toast.makeText(registerActivity.this, "Network Error: Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else{
                    Toast.makeText(registerActivity.this, "This " + emailAddress + "already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(registerActivity.this, "Please try again using another email address.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(registerActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    } */
}