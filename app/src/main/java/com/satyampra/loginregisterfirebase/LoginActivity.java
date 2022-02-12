package com.satyampra.loginregisterfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.Inet4Address;

public class LoginActivity extends AppCompatActivity {
    private EditText etLoginMobile, etLoginPassword;
    private Button btnLogin;

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://login-register-firebase-267c9-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etLoginMobile=findViewById(R.id.etLoginMobile);
        etLoginPassword =findViewById(R.id.etLoginPassword);
        btnLogin=findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phoneText=etLoginMobile.getText().toString().trim();
                final String passwordText= etLoginPassword.getText().toString().trim();
                if (phoneText.isEmpty()||passwordText.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Fields can't be Empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(phoneText)){
                                final String getPassword=snapshot.child(phoneText).child("password").getValue(String.class);
                                if (getPassword.equals(passwordText)){
                                    Toast.makeText(LoginActivity.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                }
                                else {
                                    Toast.makeText(LoginActivity.this,"Password is wrong",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(LoginActivity.this,"Mobile No. is wrong",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    public void Reg(View view) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }
}