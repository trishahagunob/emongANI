package com.example.emongani;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class registerActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText InputFirstName, InputLastName, InputEmailAddress, InputMobileNumber, InputUsername, InputPassword;


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
    }
}