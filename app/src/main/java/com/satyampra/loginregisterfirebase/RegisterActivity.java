package com.satyampra.loginregisterfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterActivity extends AppCompatActivity {
    private EditText etRegName,etRegEmail,etRegMobile,
                        etRegPass,etConfPass;

    private Button btnRegister;

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://login-register-firebase-267c9-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etRegName=findViewById(R.id.etRegName);
        etRegEmail=findViewById(R.id.etRegEmail);
        etRegMobile=findViewById(R.id.etRegMobile);
        etRegPass=findViewById(R.id.etRegPass);
        etConfPass=findViewById(R.id.etConfPass);
        btnRegister=findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String txtName=etRegName.getText().toString().trim();
                final String txtEmail=etRegEmail.getText().toString().trim();
                final String txtMobile=etRegMobile.getText().toString().trim();
                final String txtPassword=etRegPass.getText().toString().trim();
                final String txtConfPass=etConfPass.getText().toString().trim();

                if(txtName.isEmpty()||txtEmail.isEmpty()||txtMobile.isEmpty()||txtPassword.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Fields Can't be Empty",Toast.LENGTH_SHORT).show();
                }
                else if(!txtPassword.equals(txtConfPass)){
                        Toast.makeText(RegisterActivity.this,"Password are not matching",Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(txtMobile)){
                                Toast.makeText(RegisterActivity.this,"Mobile No.- "+txtMobile+" is already Registered.",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //We are using phone number as unique identity of User
                                //so other details of user will come under Phone number
                                databaseReference.child("users").child(txtMobile).child("fullname").setValue(txtName);
                                databaseReference.child("users").child(txtMobile).child("email").setValue(txtEmail);
                                databaseReference.child("users").child(txtMobile).child("password").setValue(txtPassword);

                                Toast.makeText(RegisterActivity.this, "Registration Completed", Toast.LENGTH_SHORT).show();
                                finish();
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
}