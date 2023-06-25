package com.hanindya.ag5s;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    TextView etEmail, etPassword, btnLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etEmail = findViewById(R.id.txtLoginEmail);
        etPassword = findViewById(R.id.txtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("tunggu sebentar..");
        progressDialog.setCancelable(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEmail.getText().length() > 0 && etPassword.getText().length() > 0){
                    login(etEmail.getText().toString(), etPassword.getText().toString());
                } else {
                    Toast.makeText(Login.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void login(String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getUser() != null) {
                        reload();
                        Toast.makeText(Login.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void reload() {
        Intent dashboard = new Intent(Login.this, MainActivity.class);
        startActivity(dashboard);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }

}

//    private void login(String email, String password) {
//        progressDialog.show();
//        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//                    if (task.getResult().getUser()!=null){
//
//                        // Notes : firebaseUser Auth have duplicate data in Database Realtime Node "Users". Users have child branchUserDetail.
//                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//                        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
//                        DatabaseReference dbUsers = root.child("Users");
//                        String userRoot = firebaseUser.getUid();
//
//                        // 1. Check Username (BranchUsernameDetail), if exists, ..
//                        // 2. Check Password (BranchUsernameDetail). if exists, redirect to Main Activity.
//                        Query checkUsername = dbUsers.child(userRoot).child("branchUserDetail").orderByChild("username").equalTo(branchUserName.getText().toString());
//                        ValueEventListener valueEventListener = new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if (snapshot.exists()){
//                                    Query checkPassword = dbUsers.child(userRoot).child("branchUserDetail").orderByChild("password").equalTo(branchUserPassword.getText().toString());
//                                    ValueEventListener listener = new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            if (snapshot.exists()){
//                                                Toast.makeText(Login.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
//                                                reload();
//                                            } else {
//                                                Toast.makeText(Login.this, "Password tidak ditemukan", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//                                            throw error.toException();
//                                        }
//                                    };
//                                    checkPassword.addListenerForSingleValueEvent(listener);
//                                } else {
//                                    Toast.makeText(Login.this, "Username tidak ditemukan", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                throw error.toException();
//                            }
//                        };
//                        checkUsername.addListenerForSingleValueEvent(valueEventListener);
//                    } else {
//                        Toast.makeText(Login.this, "User not found", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    Toast.makeText(Login.this, "Something Wrong", Toast.LENGTH_SHORT).show();
//                }
//                progressDialog.dismiss();
//            }
//        });
//    }
