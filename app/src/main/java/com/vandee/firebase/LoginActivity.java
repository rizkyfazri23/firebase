package com.vandee.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvRegister;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        progressDialog = new ProgressDialog(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Login");
                progressDialog.setMessage("Please wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                if(!validateUsername() || !validatePassword()){

                }else {
                    checkUser();
                    Toast.makeText(LoginActivity.this, "Masuk", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean validateUsername(){
        String val = etUsername.getText().toString();
        if(val.isEmpty()){
            etUsername.setError("Username Empty");
            return false;
        }else {
            etUsername.setError(null);
            return true;
        }
    }

    public boolean validatePassword(){
        String val = etPassword.getText().toString();
        if(val.isEmpty()){
            etPassword.setError("Password Empty");
            return false;
        }else {
            etPassword.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(username);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    etUsername.setError(null);
                    String passwordFromDB = snapshot.child(username).child("password")
                            .getValue(String.class);
                    if(!Objects.equals(passwordFromDB, etPassword)){
                        etUsername.setError(null);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else {
                        etPassword.setError("Invalid Credential");
                        etPassword.requestFocus();
                    }
                }else {
                    etUsername.setError("User does not match");
                    etUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}