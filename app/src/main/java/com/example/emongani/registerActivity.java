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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class registerActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText InputFirstName, InputLastName, InputEmailAddress, InputMobileNumber, InputUsername, InputPassword;
    private ProgressDialog loadingBar;

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
        loadingBar = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                CreateAccount();
            }
        });
    }

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
            public void onDataChange(@NonNull DataSnapshot Datasnapshot) {
                if (!(Datasnapshot.child("Users").child(emailAddress).exists())){
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
    }
}